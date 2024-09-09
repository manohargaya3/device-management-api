package com.manohar.deviceapi.service;

import com.manohar.deviceapi.model.Device;
import com.manohar.deviceapi.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    // Add device
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    // Get device by ID
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    // List all devices
    public List<Device> listAllDevices() {
        return deviceRepository.findAll();
    }

    // Update device (full)
    public Device updateDevice(Long id, Device newDevice) {
        return deviceRepository.findById(id).map(device -> {
            device.setName(newDevice.getName());
            device.setBrand(newDevice.getBrand());
            return deviceRepository.save(device);
        }).orElseThrow(() -> new RuntimeException("Device not found with id " + id));
    }

    // Partial update (name, brand)
    public Device partialUpdateDevice(Long id, String name, String brand) {
        return deviceRepository.findById(id).map(device -> {
            if (name != null) device.setName(name);
            if (brand != null) device.setBrand(brand);
            return deviceRepository.save(device);
        }).orElseThrow(() -> new RuntimeException("Device not found with id " + id));
    }

    // Delete device
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    // Search device by brand
    public List<Device> searchByBrand(String brand) {
        return deviceRepository.findByBrandIgnoreCase(brand);
    }
}
