package com.example.iot_system;

import org.springframework.stereotype.Service;

@Service
public class AIService {
    public String getResponse(String input) {
        // 模拟 AI 模型推理
        return "AI response for temperature: " + input;
    }
}