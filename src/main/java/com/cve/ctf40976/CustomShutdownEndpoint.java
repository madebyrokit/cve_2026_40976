package com.cve.ctf40976;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "custom-shutdown")
public class CustomShutdownEndpoint {

    @WriteOperation
    public Map<String, String> shutdown() {
        Map<String, String> response = new HashMap<>();

        // 플래그를 응답에 포함 (서버 종료 전에)
        String flag = System.getenv("FLAG");
        if (flag == null) {
            flag = "cve_2026_40976_bypassed";
        }

        response.put("message", "Shutting down...");
        response.put("flag", "DH{" + flag + "}");
        response.put("status", "success");

        // 비동기로 서버 종료 (응답을 보낸 후)
        new Thread(() -> {
            try {
                Thread.sleep(100);
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return response;
    }
}
