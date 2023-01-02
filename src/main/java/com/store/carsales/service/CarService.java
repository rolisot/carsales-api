package com.store.carsales.service;

import com.store.carsales.domain.Car;
import com.store.carsales.exception.BadRequestException;
import com.store.carsales.mapper.CarMapper;
import com.store.carsales.repository.CarRepository;
import com.store.carsales.request.CreateCarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarService {
    @Autowired
    private CarRepository repository;

    public Page<Car> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Car> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    public List<Car> findByName(String name) {
        return repository.findByName(name);
    }

    public Car findOne(UUID id) {
        Car orElseThrow = repository.findById(id).orElseThrow(() -> new BadRequestException("Car not found"));
        return orElseThrow;
    }

    public Car save(CreateCarRequest createCarRequest) {
        Car car = CarMapper.Instance.toCarEntity(createCarRequest);
        car.setId(UUID.randomUUID());
        return repository.save(car);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public void replace(Car car) {
    }
}
