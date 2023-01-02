package com.store.carsales.fixtures;

import com.store.carsales.request.CreateCarRequest;

public class CreateCarRequestFixture {

    public static CreateCarRequest createCarRequest() {
        return CreateCarRequest.builder().name("Car for test 1").build();
    }

}