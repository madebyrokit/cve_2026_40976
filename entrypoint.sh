#!/bin/sh
# flag를 환경변수에 남겨둔다(env 열람형이므로 unset 금지).
# 중첩 ${} 치환의 중괄호 오염을 피하려고 단계적으로 처리한다.
if [ -z "$GZCTF_FLAG" ]; then
  if [ -n "$FLAG" ]; then
    GZCTF_FLAG="$FLAG"
  else
    GZCTF_FLAG="WINS{placeholder}"
  fi
fi
export GZCTF_FLAG
exec java -jar /app/app.jar