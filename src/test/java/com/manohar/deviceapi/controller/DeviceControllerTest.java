package com.manohar.deviceapi.controller;

import com.manohar.deviceapi.model.Device;
import com.manohar.deviceapi.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceControllerTest {

    @Autowired
    private DeviceService deviceService;

    // Add device
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        device.setCreationTime(LocalDateTime.now());
        Device savedDevice = deviceService.addDevice(device);
        return new ResponseEntity<>(savedDevice, HttpStatus.CREATED);
    }

    // Get device by ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Optional<Device> device = deviceService.getDeviceById(id);
        return device.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // List all devices
    @GetMapping
    public List<Device> listAllDevices() {
        return deviceService.listAllDevices();
    }

    // Update device (full)
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device updatedDevice) {
        try {
            Device device = deviceService.updateDevice(id, updatedDevice);
            return ResponseEntity.ok(device);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Partial update device (name, brand)
    @PatchMapping("/{id}")
    public ResponseEntity<Device> partialUpdateDevice(@PathVariable Long id,
                                                      @RequestBody Device device) {
        try {
            Device updatedDevice = deviceService.partialUpdateDevice(id, device.getName(), device.getBrand());
            return ResponseEntity.ok(updatedDevice);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete device
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search devices by brand
    @GetMapping("/search")
    public List<Device> searchByBrand(@RequestParam String brand) {
        return deviceService.searchByBrand(brand);
    }
}
