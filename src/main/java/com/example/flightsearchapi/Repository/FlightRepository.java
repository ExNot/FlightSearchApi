package com.example.flightsearchapi.Repository;

import com.example.flightsearchapi.Model.Airport;
import com.example.flightsearchapi.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureAirport = :departureAirport AND f.arrivalAirport = :arrivalAirport AND f.departureDateTime = :departureDateTime")
    List<Flight> findOneWay(@Param("departureAirport") Airport departureAirport,
                                  @Param("arrivalAirport") Airport arrivalAirport,
                                  @Param("departureDateTime") LocalDateTime departureDateTime
                                  );

}