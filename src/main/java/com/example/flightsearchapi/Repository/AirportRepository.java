package com.example.flightsearchapi.Repository;

import com.example.flightsearchapi.Model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {

}
