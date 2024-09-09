package com.manohar.deviceapi.service;

import com.manohar.deviceapi.model.Device;
import com.manohar.deviceapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito and inject mock dependencies
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDevice() {
        Device device = new Device("Phone", "Apple", LocalDateTime.now());
        when(deviceRepository.save(device)).thenReturn(device);

        Device result = deviceService.addDevice(device);

        assertNotNull(result);
        assertEquals("Phone", result.getName());
        assertEquals("Apple", result.getBrand());
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void testGetDeviceById() {
        Device device = new Device("Laptop", "Dell", LocalDateTime.now());
        device.setId(1L);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        Optional<Device> result = deviceService.getDeviceById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        assertEquals("Dell", result.get().getBrand());
        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDeviceById_NotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Device> result = deviceService.getDeviceById(1L);

        assertFalse(result.isPresent());
        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    void testListAllDevices() {
        Device device1 = new Device("Phone", "Samsung", LocalDateTime.now());
        Device device2 = new Device("Tablet", "Apple", LocalDateTime.now());
        when(deviceRepository.findAll()).thenReturn(Arrays.asList(device1, device2));

        List<Device> result = deviceService.listAllDevices();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDevice() {
        Device existingDevice = new Device("Phone", "Samsung", LocalDateTime.now());
        existingDevice.setId(1L);
        Device updatedDevice = new Device("Phone", "Apple", LocalDateTime.now());

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(existingDevice)).thenReturn(existingDevice);

        Device result = deviceService.updateDevice(1L, updatedDevice);

        assertNotNull(result);
        assertEquals("Apple", result.getBrand());
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(existingDevice);
    }

    @Test
    void testUpdateDevice_NotFound() {
        Device updatedDevice = new Device("Phone", "Apple", LocalDateTime.now());

        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> deviceService.updateDevice(1L, updatedDevice));
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(0)).save(any(Device.class));
    }

    @Test
    void testPartialUpdateDevice() {
        Device existingDevice = new Device("Phone", "Samsung", LocalDateTime.now());
        existingDevice.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(existingDevice)).thenReturn(existingDevice);

        Device result = deviceService.partialUpdateDevice(1L, "Phone X", null);

        assertNotNull(result);
        assertEquals("Phone X", result.getName());
        assertEquals("Samsung", result.getBrand());  // Brand should remain unchanged
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(existingDevice);
    }

    @Test
    void testDeleteDevice() {
        doNothing().when(deviceRepository).deleteById(1L);

        deviceService.deleteDevice(1L);

        verify(deviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchByBrand() {
        Device device1 = new Device("Phone", "Apple", LocalDateTime.now());
        Device device2 = new Device("Laptop", "Apple", LocalDateTime.now());
        when(deviceRepository.findByBrandIgnoreCase("Apple")).thenReturn(Arrays.asList(device1, device2));

        List<Device> result = deviceService.searchByBrand("Apple");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(deviceRepository, times(1)).findByBrandIgnoreCase("Apple");
    }
}
