package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {
	
	@Autowired
	ParkingSpotRepository repository;
	
	@Transactional
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return repository.save(parkingSpotModel);
	}
	
	public boolean existsByLicensePlateCar(String licensePlateCar) {
		return repository.existsByLicensePlateCar(licensePlateCar);
	}
	
	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return repository.existsByParkingSpotNumber(parkingSpotNumber);
	}
	
	public boolean existsByApartmentAndBlock(String apartment, String block) {
		return repository.existsByApartmentAndBlock(apartment,block);
	}
	
	public List<ParkingSpotModel> findAll() {
		return repository.findAll();
	}
	
	public Optional<ParkingSpotModel> findById(UUID id) {
		return repository.findById(id);
	}
}
