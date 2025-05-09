package com.example.iot_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceDataController {

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    @GetMapping("/device-data")
    public List<DeviceData> getAllDeviceData() {
        return deviceDataRepository.findAll();
    }
}