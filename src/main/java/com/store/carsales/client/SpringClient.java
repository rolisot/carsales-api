package com.store.carsales.client;

import com.store.carsales.domain.Car;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        Car entity = new RestTemplate()
                .getForObject("http://localhost:8080/cars/{id}", Car.class, "757b37a4-cede-4a16-9c4e-96095393bdd5");
        log.info(entity);

        ResponseEntity<List<Car>> exchange = new RestTemplate()
                .exchange("http://localhost:8080/cars/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Car>>() {
                        });
        log.info(exchange.getBody());
    }
}
