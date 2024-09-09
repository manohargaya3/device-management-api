package com.manohar.deviceapi.repository;

import com.manohar.deviceapi.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author manohar
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrandIgnoreCase(String brand);
}
