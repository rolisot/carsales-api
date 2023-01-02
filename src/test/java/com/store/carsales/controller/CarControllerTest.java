package com.store.carsales.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.store.carsales.domain.Car;
import com.store.carsales.fixtures.CarFixture;
import com.store.carsales.fixtures.CreateCarRequestFixture;
import com.store.carsales.request.CreateCarRequest;
import com.store.carsales.service.CarService;

@ExtendWith(SpringExtension.class)
class CarControllerTest {

    @Mock
    private CarService service;
    @InjectMocks
    private CarController controller;

    @Test
    @DisplayName("Find all returns a page of list cars")
    void findAll_ReturnsPageOfListCar_WhenSuccessful() {
        PageImpl<Car> carPage = new PageImpl<>(List.of(CarFixture.createCar()));
        when(service.findAll(any())).thenReturn(carPage);
        Page<Car> output = controller.findAll(null);
        Assertions.assertThat(output).isNotNull();
        Assertions.assertThat(output.toList()).isNotEmpty().hasSize(1);
        verify(service, times(1)).findAll(any());
    }

    @Test
    @DisplayName("List all returns a list of cars")
    void listAll_ReturnsListOfCar_WhenSuccessful() {
        when(service.findAll()).thenReturn(List.of(CarFixture.createCar()));
        List<Car> cars = controller.listAll();
        Assertions.assertThat(cars).isNotNull();
        Assertions.assertThat(cars).isNotEmpty().hasSize(1);
        verify(service, times(1)).findAll();
    }

    @Test
    @DisplayName("FindOne returns car when successful")
    void findOne_ReturnsCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(service.findOne(any(UUID.class))).thenReturn(CarFixture.createCar());
        Car car = controller.findOne(expected.getId());
        Assertions.assertThat(car).isNotNull();
        Assertions.assertThat(car).isEqualTo(expected);
        verify(service, times(1)).findOne(any(UUID.class));
    }

    @Test
    @DisplayName("FindByName returns car when successful")
    void findByName_ReturnsCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(service.findByName(anyString())).thenReturn(List.of(CarFixture.createCar()));
        List<Car> output = controller.findByName(expected.getName());
        Assertions.assertThat(output).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(output.get(0)).isEqualTo(expected);
        verify(service, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("FindByName returns empty list when is not found")
    void findByName_ReturnsEmptyList_WhenCarIsNotFound() {
        when(service.findByName(anyString())).thenReturn(Collections.emptyList());
        List<Car> output = controller.findByName("car");
        Assertions.assertThat(output).isNotNull().isEmpty();
        verify(service, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("Save returns new car when successful")
    void save_ReturnsNewCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(service.save(any(CreateCarRequest.class))).thenReturn(expected);
        CreateCarRequest createCarRequest = CreateCarRequestFixture.createCarRequest();
        Car output = controller.save(createCarRequest);
        Assertions.assertThat(output).isNotNull().isEqualTo(expected);
        verify(service, times(1)).save(any(CreateCarRequest.class));
    }

    @Test
    @DisplayName("Delete removes car when successful")
    void delete_RemovesCar_WhenSuccessful() {
        doNothing().when(service).delete(any(UUID.class));
        controller.delete(UUID.randomUUID());
        verify(service, times(1)).delete(any(UUID.class));
    }
}