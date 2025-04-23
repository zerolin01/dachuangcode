package com.example.iot_system;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Autowired
    private DeviceDataRepository deviceDataRepository;

    @Autowired
    private AIService aiService;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic.device.temperature}")
    private String topic;

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(brokerUrl, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        mqttClient.connect(options);

        mqttClient.subscribe(topic, (topic, message) -> {
            String payload = new String(message.getPayload());
            System.out.println("Received data: " + payload);

            // 存储到数据库
            DeviceData data = new DeviceData();
            data.setTemperature(payload);
            data.setTimestamp(String.valueOf(System.currentTimeMillis()));
            deviceDataRepository.save(data);

            // 调用 AI 模型（模拟）
            String aiResult = aiService.getResponse(payload);

            // 发布 AI 结果到 MQTT
            mqttClient.publish("device/result", new MqttMessage(aiResult.getBytes()));
        });

        return mqttClient;
    }
}