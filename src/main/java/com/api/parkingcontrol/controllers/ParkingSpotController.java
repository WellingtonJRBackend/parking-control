package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotDTO;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	@Autowired
	ParkingSpotService service;
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){
		if(service.existsByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use! ");
		}
		if (service.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		if(service.existsByApartmentAndBlock(parkingSpotDTO.getApartment(),parkingSpotDTO.getBlock())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registration for this apartment/block!");
		}
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDTO , parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(parkingSpotModel));
	}
	@GetMapping
	public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
		if(!parkingSpotModelOptional.isPresent()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable UUID id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
		if(!parkingSpotModelOptional.isPresent()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
		}
		service.delete(parkingSpotModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
	}
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable UUID id,@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){
		Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
		if(!parkingSpotModelOptional.isPresent()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
		}
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDTO,parkingSpotModel);
		parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
		return ResponseEntity.status(HttpStatus.OK).body(service.save(parkingSpotModel));
	}
}
