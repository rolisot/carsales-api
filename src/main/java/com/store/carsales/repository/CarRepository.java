package com.store.carsales.repository;

import com.store.carsales.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    List<Car> findByName(String name);

    List<Car> findAllByOrderByNameAsc();
}
