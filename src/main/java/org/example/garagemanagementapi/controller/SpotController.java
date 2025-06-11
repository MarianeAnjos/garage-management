package org.example.garagemanagementapi.controller;

import org.example.garagemanagementapi.model.Spot;
import org.example.garagemanagementapi.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/spots")
public class SpotController {

    private final SpotRepository spotRepository;

    public SpotController(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @GetMapping
    public List<Spot> getAllSpots() {
        return spotRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable Long id){
        Optional<Spot> spotOptional = spotRepository.findById(id);
        if (spotOptional.isPresent()){
            return new ResponseEntity<>(spotOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Spot> creatSpot (@RequestBody Spot spot){
        Spot novoSpot = spotRepository.save(spot);
        return new ResponseEntity<>(novoSpot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Spot> updateSpot(@PathVariable long id, @RequestBody Spot spotDetails){
        Optional<Spot> spotOptional = spotRepository.findById(id);
        if(spotOptional.isPresent()) {
            Spot spot = spotOptional.get();
            spot.setLicensePlate(spotDetails.getLicensePlate());
            spot.setLat(spotDetails.getLat());
            spot.setLongi(spotDetails.getLongi());
            spot.setOccupied(spotDetails.isOccupied());

            Spot updatedSpot = spotRepository.save(spot);
            return new ResponseEntity<>(updatedSpot, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id){
        if (spotRepository.existsById(id)){
            spotRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
