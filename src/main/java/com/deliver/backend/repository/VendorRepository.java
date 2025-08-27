package com.deliver.backend.repository;

import com.deliver.backend.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByServiceTypeAndStatus(String serviceType, Vendor.VendorStatus status);

    List<Vendor> findByServiceType(String serviceType);
}
