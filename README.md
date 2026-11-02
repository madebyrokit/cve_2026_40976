<div align="center">

# 🔍 Leaky Management

**`CTF40976`**

`Web` · `Information Disclosure` · **Difficulty: Easy**

*열려 있는 관리 기능 뒤에, 나오면 안 될 값이 숨어 있다.*

</div>

---

## 📖 Description

> 개발 중이던 서비스가 그대로 운영에 올라간 것 같습니다.
> 편의를 위해 켜 둔 **관리·모니터링 기능**이 외부에 열린 채 방치되어 있습니다.
>
> 열려 있는 것들을 하나씩 들여다보면, 밖으로 나오면 안 되는 값이
> 그 안에 섞여 있을지도 모릅니다.

```
🌐  http://<HOST>:<PORT>/
🚩  WINS{...}
```

---

## 🏷️ Challenge Info

| | |
| --- | --- |
| **Category** | Web |
| **Type** | Security Misconfiguration (OWASP A05:2021) |
| **Difficulty** | Easy |
| **Flag Format** | `WINS{...}` |
| **Deploy** | 공유(Shared) — 읽기 전용, 상태 변경 없음 |

---

## ⚙️ 배포 정보 *(운영진 입력용)*

| 항목 | 값 |
| --- | --- |
| 🐳 이미지 이름 | `ctf40976` |
| 🔌 내부 포트 | `8080` |
| 🌍 호스트 포트 | `10018` *(공유 범위 10000–19999)* |
| 🧠 메모리 | `256MB` |
| 📦 분류 | 공유 *(정보 노출 / env 열람, 읽기 전용)* |
| 🔑 플래그 변수 | `FLAG` *(공유 배포)* · `GZCTF_FLAG` 도 대응 |
| 🏷️ 플래그 템플릿 | `WINS{[GUID]}` |
| 🌐 네트워크 | 외부 인터넷 불필요 |

### ✅ 운영진 확인 체크리스트

- [ ] **🖥️ 아키텍처** — 로컬 검수는 x86(`amd64`)에서 수행됨. 대회 서버(ARM/Ampere)에서 빌드 확인 필요
  ```bash
  docker build --platform linux/arm64 -t ctf40976-arm .
  ```
- [ ] **👤 실행 사용자** — 현재 컨테이너가 `root`로 동작. 정책상 비루트 실행이 필요하면 전환 권장
  *(env 열람형이라 플래그 획득에는 영향 없음)*
- [ ] **🧠 메모리** — 기동 후 `docker stats`로 256MB 이내 확인. 초과 시 `JAVA_TOOL_OPTIONS`로 힙 조정

---

## 🚀 배포 방법 *(공유 문제 · compose 상시 실행)*

```bash
# 플래그는 환경변수로 주입 — 이미지에 굽지 않는다
FLAG="WINS{...실제_GUID...}" docker compose up -d --build
```

`docker-compose.yml` 의 `environment.FLAG` 값을 배정된 플래그로 바꿔 실행합니다.
플래그는 하드코딩·빌드 시점 주입이 아니라 **런타임 환경변수**로만 들어갑니다.

---

## 🧪 로컬 검수

```bash
docker compose up -d --build
python poc.py -u http://localhost:10018
```

> `poc.py` 가 `WINS{...}` 를 출력하면 정상입니다.

---

## 📂 Files

| 파일 | 설명 |
| --- | --- |
| `Dockerfile` | 멀티스테이지 빌드 (ARM/x86) |
| `docker-compose.yml` | 공유 배포 · 로컬 검수용 |
| `entrypoint.sh` | 플래그 주입 (`FLAG` / `GZCTF_FLAG`) |
| `poc.py` | 익스플로잇 자동화 |
| `WRITEUP.md` | 풀이 및 출제 설계 노트 |
