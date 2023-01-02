package com.store.carsales.controller;

import com.store.carsales.domain.Car;
import com.store.carsales.request.CreateCarRequest;
import com.store.carsales.service.CarService;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cars")
public class CarController {

    @Autowired
    private CarService service;

    @GetMapping
    public Page<Car> findAll(@ParameterObject Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/all")
    public List<Car> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Car findOne(@PathVariable UUID id) {
        return service.findOne(id);
    }

    @GetMapping("/find")
    public List<Car> findByName(@RequestParam String name) {
        return service.findByName(name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Car save(@Valid @RequestBody CreateCarRequest createCarRequest) {
        return service.save(createCarRequest);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PutMapping
    public void replace(@RequestBody Car car) {
        service.replace(car);
    }

}
