package com.example.flightsearchapi.Controller;

import com.example.flightsearchapi.Model.Airport;
import com.example.flightsearchapi.Model.Flight;
import com.example.flightsearchapi.Repository.FlightRepository;
import com.example.flightsearchapi.ScheduledJob.FlightDataUpdater;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/flights")
@Validated
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;

    //CREATE (POST)
    @PostMapping("/add")
    public ResponseEntity<Flight> addFlight(@RequestBody @Valid Flight flight){
        Flight savedFlight = flightRepository.save(flight);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    //READ (GET): All Flights
    @GetMapping("/all")
    public ResponseEntity<List<Flight>> getAllFlights(){
        List<Flight> flights = flightRepository.findAll();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    //READ (GET): Fetch flight by ID
    @GetMapping("/{flightId}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long flightId){
        Optional<Flight> optionalFlight = flightRepository.findById(flightId); // Used Optional<> because we don't know about is it exist or not.
        return optionalFlight.map(flight -> new ResponseEntity<>(flight, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //UPDATE (PUT): Update a flight
    @PutMapping("/update/{flightId}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long flightId, @Valid @RequestBody Flight updateFlight){
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()){
            Flight existingFlight = optionalFlight.get();

            existingFlight.setDepartureAirport(updateFlight.getDepartureAirport());
            existingFlight.setArrivalAirport(updateFlight.getArrivalAirport());
            existingFlight.setDepartureDateTime(updateFlight.getDepartureDateTime());
            existingFlight.setReturnDateTime(updateFlight.getReturnDateTime());
            existingFlight.setPrice(updateFlight.getPrice());

            Flight savedFlight = flightRepository.save(existingFlight);
            return new ResponseEntity<>(savedFlight, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //DELETE: Delete a flight
    @DeleteMapping("/delete/{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long flightId){
        if (flightRepository.existsById(flightId)){
            flightRepository.deleteById(flightId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //SEARCH FOR FLIGHTS
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam Airport departureAirport,
            @RequestParam Airport arrivalAirport,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDate returnDate
    ) {

        try {
            LocalDateTime startOfDay = departureDate.atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);


            if (returnDate == null){
                //One way
                List<Flight> oneWayFlight = flightRepository.findFlight(departureAirport, arrivalAirport, startOfDay, endOfDay);
                return new ResponseEntity<>(oneWayFlight, HttpStatus.OK);
            }else {
                //Round trip
                List<Flight> departureFlight = flightRepository.findFlight(departureAirport, arrivalAirport, startOfDay, endOfDay);

                LocalDateTime returnStartOfDay = returnDate.atStartOfDay();
                LocalDateTime returnEndOfDay = returnStartOfDay.plusDays(1).minusSeconds(1);
                List<Flight> arrivalFlight = flightRepository.findFlight(arrivalAirport, departureAirport, returnStartOfDay, returnEndOfDay);
                List<Flight> roundTripFlights = Stream.concat(departureFlight.stream(), arrivalFlight.stream())
                        .collect(Collectors.toList());
                return new ResponseEntity<>(roundTripFlights, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

























}
