package com.deliver.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Common resource not found methods
    public static ResourceNotFoundException forUser(Long userId) {
        return new ResourceNotFoundException("User", "id", userId);
    }
    
    public static ResourceNotFoundException forUser(String email) {
        return new ResourceNotFoundException("User", "email", email);
    }
    
    public static ResourceNotFoundException forVendor(Long vendorId) {
        return new ResourceNotFoundException("Vendor", "id", vendorId);
    }
    
    public static ResourceNotFoundException forProduct(Long productId) {
        return new ResourceNotFoundException("Product", "id", productId);
    }
    
    public static ResourceNotFoundException forOrder(Long orderId) {
        return new ResourceNotFoundException("Order", "id", orderId);
    }
    
    public static ResourceNotFoundException forOrder(String orderNumber) {
        return new ResourceNotFoundException("Order", "orderNumber", orderNumber);
    }
}
