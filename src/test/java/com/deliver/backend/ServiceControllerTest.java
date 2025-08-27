package com.deliver.backend;

import com.deliver.backend.controller.ServiceController;
import com.deliver.backend.repository.ServiceRepository;
import com.deliver.backend.service.ServiceService;
import com.deliver.backend.dto.response.ServiceCategoryListResponse;
import com.deliver.backend.dto.response.ServiceCategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ServiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceService serviceService;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceController serviceController;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    public void testGetServices() throws Exception {
        // Mock data - services variable is not used directly, only for building response

        // ServiceService'i mock'luyoruz
        ServiceCategoryListResponse response = ServiceCategoryListResponse.builder()
                .serviceCategories(Arrays.asList(
                    ServiceCategoryListResponse.ServiceCategorySummaryResponse.builder()
                        .id(1L)
                        .name("DeliVerTech")
                        .displayName("DeliVer Tech")
                        .isActive(true)
                        .build(),
                    ServiceCategoryListResponse.ServiceCategorySummaryResponse.builder()
                        .id(2L)
                        .name("DeliverPet")
                        .displayName("Deliver Pet")
                        .isActive(true)
                        .build()
                ))
                .totalCount(2)
                .hasMore(false)
                .build();

        when(serviceService.getAllServiceCategories()).thenReturn(response);

        // Test endpoint
        mockMvc.perform(get("/api/v1/services/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceCategories[0].id").value(1))
                .andExpect(jsonPath("$.serviceCategories[0].name").value("DeliVerTech"))
                .andExpect(jsonPath("$.serviceCategories[1].id").value(2))
                .andExpect(jsonPath("$.serviceCategories[1].name").value("DeliverPet"));
    }

    @Test
    public void testGetServiceById() throws Exception {
        // Service variable is not used directly, only for building response

        ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                .id(1L)
                .name("DeliVerTech")
                .displayName("DeliVer Tech")
                .isActive(true)
                .build();

        when(serviceService.getServiceCategoryDetails("DeliVerTech", null, null, null, 0, 20)).thenReturn(response);

        mockMvc.perform(get("/api/v1/services/categories/DeliVerTech")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("DeliVerTech"));
    }

    @Test
    public void testGetServicesByCategory() throws Exception {
        // Service and services variables are not used directly, only for building response

        ServiceCategoryResponse response = ServiceCategoryResponse.builder()
                .id(1L)
                .name("DeliVerTech")
                .displayName("DeliVer Tech")
                .isActive(true)
                .build();

        when(serviceService.getServiceCategoryDetails("Electronics", null, null, null, 0, 20)).thenReturn(response);

        mockMvc.perform(get("/api/v1/services/categories/Electronics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("DeliVerTech"));
    }
}