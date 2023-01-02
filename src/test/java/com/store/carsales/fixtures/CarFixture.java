package com.store.carsales.fixtures;

import com.store.carsales.domain.Car;

import java.util.UUID;

public class CarFixture {
    public static Car createCar(){
        return Car.builder()
                .id(UUID.fromString("8fceac01-534a-44c5-b7c4-01c50f31a0ca"))
                .name("Car for test 1")
                .build();
    }
}
