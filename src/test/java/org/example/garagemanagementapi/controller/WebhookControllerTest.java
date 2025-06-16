/*package org.example.garagemanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.garagemanagementapi.dto.WebhookEvent;
import org.example.garagemanagementapi.model.Vehicle;
import org.example.garagemanagementapi.repository.VehicleRepository;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebhookController.class)
public class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleRepository vehicleRepository;

    @MockBean
    private SpotRepository spotRepository;

    // Supondo que seu endpoint trate o evento "ENTRY"
    @Test
    public void testHandleEntryEvent() throws Exception {
        WebhookEvent event = new WebhookEvent();
        event.setEvent("ENTRY");
        event.setLicensePlate("ABC123");
        // Configure os demais atributos se necessário

        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setLicensePlate("ABC123");
        savedVehicle.setEntryTime(LocalDateTime.now());
        Mockito.when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Vehicle entry recorded")));
    }
}
*/