package com.cve.ctf40976;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "flag")
public class FlagEndpoint {

    @ReadOperation
    public Map<String, String> flag() {
        // 대회: GZCTF_FLAG / 로컬 검수: FLAG / 최후 fallback
        String flag = System.getenv("GZCTF_FLAG");
        if (flag == null) flag = System.getenv("FLAG");
        if (flag == null) flag = "WINS{placeholder}";

        Map<String, String> response = new HashMap<>();
        response.put("message", "Access granted");
        response.put("flag", flag);   // 이미 WINS{...} 형태이므로 감싸지 않음
        response.put("status", "success");
        return response;
    }
}