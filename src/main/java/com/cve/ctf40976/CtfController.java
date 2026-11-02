package com.cve.ctf40976;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CtfController {
    @GetMapping("/")
    public String home() {
        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>🚀 CVE-2026-40976 - 셧다운 쇼다운</title>
                    <style>
                        /* ===== 글로벌 스타일 ===== */
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                
                        body {
                            font-family: 'Segoe UI', 'Nanum Gothic', 'Malgun Gothic', sans-serif;
                            min-height: 100vh;
                            background: linear-gradient(135deg, #0a0e1a 0%, #1a1f35 50%, #0d1117 100%);
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            padding: 20px;
                            position: relative;
                            overflow-x: hidden;
                        }
                
                        /* ===== 배경 애니메이션 효과 ===== */
                        body::before {
                            content: '';
                            position: fixed;
                            top: -50%;
                            left: -50%;
                            width: 200%;
                            height: 200%;
                            background:\s
                                radial-gradient(ellipse at 20% 50%, rgba(0, 255, 100, 0.05) 0%, transparent 60%),
                                radial-gradient(ellipse at 80% 50%, rgba(0, 150, 255, 0.05) 0%, transparent 60%);
                            animation: rotateBg 30s linear infinite;
                            z-index: 0;
                        }
                
                        @keyframes rotateBg {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                
                        /* ===== 메인 카드 ===== */
                        .container {
                            position: relative;
                            z-index: 1;
                            max-width: 820px;
                            width: 100%;
                            background: rgba(255, 255, 255, 0.03);
                            backdrop-filter: blur(20px);
                            -webkit-backdrop-filter: blur(20px);
                            border: 1px solid rgba(255, 255, 255, 0.06);
                            border-radius: 32px;
                            padding: 50px 45px;
                            box-shadow:\s
                                0 30px 80px rgba(0, 0, 0, 0.6),
                                inset 0 1px 0 rgba(255, 255, 255, 0.05);
                            transition: all 0.3s ease;
                        }
                
                        .container:hover {
                            box-shadow:\s
                                0 40px 100px rgba(0, 0, 0, 0.8),
                                inset 0 1px 0 rgba(255, 255, 255, 0.08);
                        }
                
                        /* ===== 상단 배지 ===== */
                        .badge {
                            display: inline-block;
                            background: linear-gradient(135deg, #ff6b35, #f7931e);
                            color: #fff;
                            font-size: 11px;
                            font-weight: 700;
                            letter-spacing: 1.5px;
                            padding: 6px 16px;
                            border-radius: 100px;
                            text-transform: uppercase;
                            margin-bottom: 24px;
                            box-shadow: 0 4px 20px rgba(247, 147, 30, 0.3);
                        }
                
                        /* ===== 제목 ===== */
                        .title {
                            font-size: 48px;
                            font-weight: 800;
                            background: linear-gradient(135deg, #f0f4ff 0%, #8ec5fc 50%, #6dd5fa 100%);
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            background-clip: text;
                            margin-bottom: 8px;
                            line-height: 1.2;
                        }
                
                        .subtitle {
                            font-size: 20px;
                            font-weight: 300;
                            color: rgba(255, 255, 255, 0.5);
                            margin-bottom: 30px;
                            letter-spacing: 0.5px;
                        }
                
                        /* ===== 취약점 정보 ===== */
                        .vuln-info {
                            display: flex;
                            flex-wrap: wrap;
                            gap: 12px;
                            margin-bottom: 30px;
                        }
                
                        .vuln-tag {
                            background: rgba(255, 71, 87, 0.12);
                            border: 1px solid rgba(255, 71, 87, 0.2);
                            color: #ff6b7a;
                            padding: 6px 16px;
                            border-radius: 100px;
                            font-size: 13px;
                            font-weight: 600;
                            letter-spacing: 0.3px;
                        }
                
                        .vuln-tag.cve {
                            background: rgba(255, 215, 0, 0.08);
                            border-color: rgba(255, 215, 0, 0.2);
                            color: #ffd700;
                        }
                
                        .vuln-tag.spring {
                            background: rgba(0, 200, 100, 0.08);
                            border-color: rgba(0, 200, 100, 0.2);
                            color: #6fcf97;
                        }
                
                        /* ===== 설명 영역 ===== */
                        .description {
                            background: rgba(0, 0, 0, 0.3);
                            border-radius: 16px;
                            padding: 25px 28px;
                            margin-bottom: 30px;
                            border-left: 3px solid #ff6b35;
                        }
                
                        .description p {
                            color: rgba(255, 255, 255, 0.75);
                            font-size: 16px;
                            line-height: 1.8;
                            margin-bottom: 12px;
                        }
                
                        .description p:last-child {
                            margin-bottom: 0;
                        }
                
                        .highlight {
                            color: #ffd700;
                            font-weight: 600;
                        }
                
                        .highlight-green {
                            color: #6fcf97;
                            font-weight: 600;
                        }
                
                        /* ===== 힌트 박스 ===== */
                        .hint-box {
                            background: linear-gradient(135deg, rgba(0, 150, 255, 0.05), rgba(0, 255, 150, 0.05));
                            border: 1px solid rgba(0, 200, 255, 0.12);
                            border-radius: 16px;
                            padding: 22px 28px;
                            margin-bottom: 30px;
                        }
                
                        .hint-box h3 {
                            color: #8ec5fc;
                            font-size: 14px;
                            font-weight: 700;
                            letter-spacing: 1px;
                            text-transform: uppercase;
                            margin-bottom: 14px;
                        }
                
                        .hint-box ul {
                            list-style: none;
                            padding: 0;
                        }
                
                        .hint-box ul li {
                            color: rgba(255, 255, 255, 0.65);
                            font-size: 15px;
                            padding: 6px 0;
                            padding-left: 24px;
                            position: relative;
                            line-height: 1.6;
                        }
                
                        .hint-box ul li::before {
                            content: '▸';
                            position: absolute;
                            left: 0;
                            color: #6dd5fa;
                            font-weight: 700;
                        }
                
                        .hint-box ul li strong {
                            color: rgba(255, 255, 255, 0.9);
                        }
                
                        .hint-box ul li .code {
                            font-family: 'Consolas', 'Courier New', monospace;
                            background: rgba(255, 255, 255, 0.06);
                            padding: 2px 10px;
                            border-radius: 6px;
                            color: #ffd700;
                            font-size: 13px;
                        }
                
                        /* ===== 버전 정보 ===== */
                        .version-info {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            flex-wrap: wrap;
                            gap: 12px;
                            padding-top: 20px;
                            border-top: 1px solid rgba(255, 255, 255, 0.05);
                        }
                
                        .version {
                            color: rgba(255, 255, 255, 0.25);
                            font-size: 13px;
                            font-weight: 300;
                            letter-spacing: 0.5px;
                        }
                
                        .version strong {
                            color: rgba(255, 255, 255, 0.4);
                            font-weight: 600;
                        }
                
                        .ctf-flag-format {
                            color: rgba(255, 255, 255, 0.3);
                            font-size: 13px;
                            font-family: 'Consolas', 'Courier New', monospace;
                            background: rgba(255, 255, 255, 0.04);
                            padding: 4px 14px;
                            border-radius: 100px;
                            border: 1px solid rgba(255, 255, 255, 0.04);
                        }
                
                        /* ===== 터미널 스타일 (선택적 데코) ===== */
                        .terminal-decoration {
                            margin-top: 20px;
                            display: flex;
                            gap: 8px;
                            opacity: 0.3;
                        }
                
                        .terminal-decoration span {
                            display: inline-block;
                            width: 12px;
                            height: 12px;
                            border-radius: 50%;
                        }
                
                        .terminal-decoration .red { background: #ff5f56; }
                        .terminal-decoration .yellow { background: #ffbd2e; }
                        .terminal-decoration .green { background: #27c93f; }
                
                        /* ===== 반응형 ===== */
                        @media (max-width: 600px) {
                            .container {
                                padding: 30px 20px;
                            }
                            .title {
                                font-size: 30px;
                            }
                            .subtitle {
                                font-size: 16px;
                            }
                            .description {
                                padding: 18px 18px;
                            }
                            .description p {
                                font-size: 14px;
                            }
                            .hint-box {
                                padding: 16px 18px;
                            }
                            .hint-box ul li {
                                font-size: 13px;
                            }
                            .version-info {
                                flex-direction: column;
                                align-items: flex-start;
                            }
                        }
                
                        /* ===== 폴링 애니메이션 ===== */
                        @keyframes pulse {
                            0%, 100% { opacity: 1; }
                            50% { opacity: 0.5; }
                        }
                
                        .pulse-dot {
                            display: inline-block;
                            width: 8px;
                            height: 8px;
                            background: #6fcf97;
                            border-radius: 50%;
                            margin-right: 8px;
                            animation: pulse 1.5s ease-in-out infinite;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <!-- 상단 배지 -->
                        <div class="badge">⚡ CTF Challenge</div>
                
                        <!-- 제목 -->
                        <h1 class="title">Actuator 취약점</h1>
                        <p class="subtitle">Spring Boot Actuator 인증 우회 취약점을 찾아라</p>
                
                        <!-- 취약점 정보 태그 -->
                        <div class="vuln-info">
                            <span class="vuln-tag cve">📌 CVE-2026-40976</span>
                            <span class="vuln-tag">🔓 인증 우회</span>
                            <span class="vuln-tag spring">🍃 Spring Boot 4.0.0</span>
                            <span class="vuln-tag">⚠️ CVSS 9.1</span>
                        </div>
                
                        <!-- 설명 -->
                        <div class="description">
                            <p>
                                이 서비스는 <span class="highlight">Spring Boot 4.0.0</span>으로 구동되고 있습니다.
                                특정 조건에서 <span class="highlight-green">Actuator 엔드포인트</span>에 대한
                                인증이 무력화되는 취약점이 존재합니다.
                            </p>
                            <p>
                                🎯 <strong style="color: #fff;">목표:</strong> 취약점을 악용하여\s
                                <span class="highlight">플래그(Flag)</span>를 획득하세요!
                            </p>
                        </div>
                
                        <!-- 힌트 박스 -->
                        <div class="hint-box">
                            <h3>💡 탐색 힌트</h3>
                            <ul>
                                <li>
                                    <strong>1.</strong> 모든 Actuator 엔드포인트는\s
                                    <span class="code">/actuator</span> 경로 아래에 위치합니다.
                                </li>
                                <li>
                                    <strong>2.</strong> 인증 없이 접근 가능한 엔드포인트를 찾아보세요.
                                </li>
                                <li>
                                    <strong>3.</strong> <span class="code">특정</span> 엔드포인트를 호출하면 플래그가 출력됩니다.
                                </li>
                                <li style="margin-top: 8px; color: rgba(255,255,255,0.4); font-size: 13px;">
                                    ⚡ 힌트: <span class="code">/actuator</span> 링크를 먼저 확인해보세요.
                                </li>
                            </ul>
                        </div>
                
                        <!-- 하단 정보 -->
                        <div class="version-info">
                            <div class="version">
                                <span class="pulse-dot"></span>
                                <strong>Spring Boot</strong> 4.0.0 &nbsp;·&nbsp; CVE-2026-40976
                            </div>
                            <div class="ctf-flag-format">
                                🚩 DH{...}
                            </div>
                        </div>
                
                    </div>
                </body>
                </html>
            """;
    }
}
