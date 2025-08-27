package com.deliver.backend.exception;

public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Common bad request scenarios
    public static BadRequestException invalidEmail() {
        return new BadRequestException("Invalid email format");
    }
    
    public static BadRequestException weakPassword() {
        return new BadRequestException("Password must be at least 8 characters long and contain uppercase, lowercase, number and special character");
    }
    
    public static BadRequestException emailAlreadyExists() {
        return new BadRequestException("Email address is already registered");
    }
    
    public static BadRequestException phoneAlreadyExists() {
        return new BadRequestException("Phone number is already registered");
    }
    
    public static BadRequestException invalidOrderStatus() {
        return new BadRequestException("Invalid order status transition");
    }
    
    public static BadRequestException insufficientStock() {
        return new BadRequestException("Insufficient stock for the requested quantity");
    }
    
    public static BadRequestException invalidPaymentMethod() {
        return new BadRequestException("Invalid payment method");
    }
    
    public static BadRequestException orderCannotBeCancelled() {
        return new BadRequestException("Order cannot be cancelled at this stage");
    }
    
    public static BadRequestException invalidPromoCode() {
        return new BadRequestException("Invalid or expired promo code");
    }
    
    public static BadRequestException vendorNotActive() {
        return new BadRequestException("Vendor is not currently accepting orders");
    }
    
    public static BadRequestException outsideDeliveryArea() {
        return new BadRequestException("Delivery address is outside vendor's delivery area");
    }
}
