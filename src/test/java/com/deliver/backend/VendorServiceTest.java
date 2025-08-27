package com.deliver.backend;

import com.deliver.backend.dto.response.VendorResponse;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.VendorRepository;
import com.deliver.backend.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private User testUser;
    private Vendor testVendor;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("vendor@example.com")
                .username("vendoruser")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .roles(Set.of(User.UserRole.VENDOR))
                .status(User.UserStatus.ACTIVE)
                .build();

        testVendor = Vendor.builder()
                .id(1L)
                .user(testUser)
                .businessName("Test Restaurant")
                .description("Best restaurant in town")
                .category("Restaurant")
                .address("123 Main Street")
                .phoneNumber("+1234567890")
                .deliveryFee(new BigDecimal("2.99"))
                .deliveryRadiusKm(new BigDecimal("5.0"))
                .estimatedDeliveryTimeMinutes(30)
                .minimumOrderAmount(new BigDecimal("15.00"))
                .status(Vendor.VendorStatus.ACTIVE)
                .taxId("TAX123456")
                .businessLicenseNumber("BLN123456")
                .build();
    }

    @Test
    void testGetAllVendors() {
        List<Vendor> vendors = Arrays.asList(testVendor);
        when(vendorRepository.findAll()).thenReturn(vendors);

        List<VendorResponse> result = vendorService.getAllVendors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Restaurant", result.get(0).getBusinessName());
        verify(vendorRepository).findAll();
    }

    @Test
    void testGetVendorById() {
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(testVendor));

        VendorResponse result = vendorService.getVendorById(1L);

        assertNotNull(result);
        assertEquals("Test Restaurant", result.getBusinessName());
        assertEquals("123 Main Street", result.getAddress());
        verify(vendorRepository).findById(1L);
    }

    @Test
    void testGetVendorByIdNotFound() {
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> vendorService.getVendorById(1L));
        assertNotNull(exception);
        verify(vendorRepository).findById(1L);
    }

    @Test
    void testGetVendorsByCategory() {
        List<Vendor> vendors = Arrays.asList(testVendor);
        when(vendorRepository.findByCategoryIgnoreCase("Restaurant")).thenReturn(vendors);

        List<VendorResponse> result = vendorService.getVendorsByCategory("Restaurant");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Restaurant", result.get(0).getCategory());
        verify(vendorRepository).findByCategoryIgnoreCase("Restaurant");
    }

    @Test
    void testGetVendorsByCategoryEmpty() {
        when(vendorRepository.findByCategoryIgnoreCase("NonExistent")).thenReturn(Arrays.asList());

        List<VendorResponse> result = vendorService.getVendorsByCategory("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vendorRepository).findByCategoryIgnoreCase("NonExistent");
    }

    @Test
    void testVendorResponseMapping() {
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(testVendor));

        VendorResponse result = vendorService.getVendorById(1L);

        assertNotNull(result);
        assertEquals(testVendor.getId(), result.getId());
        assertEquals(testVendor.getBusinessName(), result.getBusinessName());
        assertEquals(testVendor.getDescription(), result.getDescription());
        assertEquals(testVendor.getCategory(), result.getCategory());
        assertEquals(testVendor.getAddress(), result.getAddress());
        assertEquals(testVendor.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testVendor.getDeliveryFee(), result.getDeliveryFee());
        assertEquals(testVendor.getMinimumOrderAmount(), result.getMinimumOrderAmount());
        assertEquals(testVendor.getStatus().toString(), result.getStatus().toString());
    }
}
