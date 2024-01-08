package com.example.flightsearchapi.ScheduledJob;

import com.example.flightsearchapi.Model.Flight;
import com.example.flightsearchapi.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlightDataUpdater {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private RestTemplate restTemplate;


    @Scheduled(cron = "0 0 * * *")
    public void updateFlightData() {
        List<Flight> mockFlightData = getMockFlightData();


        for (Flight flight : mockFlightData) {
            flightRepository.save(flight);
        }
    }

    private List<Flight> getMockFlightData() {
        // Mock veri üretimi
        List<Flight> mockFlightData = new ArrayList<>();

        String apiUrl = "https://mockflight.free.beeceptor.com";
        ResponseEntity<List<Flight>> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Flight>>() {
                }
        );
        mockFlightData = responseEntity.getBody();

        return mockFlightData;
    }
}
