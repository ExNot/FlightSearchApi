package com.example.flightsearchapi.Controller;

import com.example.flightsearchapi.Model.Airport;
import com.example.flightsearchapi.Repository.AirportRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airports")
@Validated
public class AirportController {
    @Autowired
    private AirportRepository airportRepository;

    //Add Airport (CREATE)
    @PostMapping
    public ResponseEntity<Airport> addAirport(@Valid @RequestBody Airport airport){
        Airport savedAirport = airportRepository.save(airport);
        return new ResponseEntity<>(savedAirport, HttpStatus.CREATED);
    }


    // Fetch an airport with Id (READ)
    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long id){
        Optional<Airport> airport = airportRepository.findById(id);
        return airport.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //all airports (READ)
    @GetMapping
    public ResponseEntity<List<Airport>> getAllAirports(){
        List<Airport> airports = airportRepository.findAll();
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    //Update an airport (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @Valid @RequestBody Airport updatedAirport){
        if (airportRepository.existsById(id)){
            updatedAirport.setId(id);
            Airport savedAirport = airportRepository.save(updatedAirport);
            return new ResponseEntity<>(savedAirport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete an airport (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id){
        if (airportRepository.existsById(id)){
            airportRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}




















