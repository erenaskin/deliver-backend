package com.deliver.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyServiceResponse {
    
    private Long id;
    private String name;
    private String description;
    private String emergencyText;
    private String buttonText;
    private String backgroundColor;
    private String borderColor;
    private String iconName;
    private Boolean isActive;
    private Integer displayOrder;
    private String phoneNumber;
    private String actionUrl;
}

