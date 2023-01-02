package com.store.carsales.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.store.carsales.domain.Car;
import com.store.carsales.fixtures.CarFixture;
import com.store.carsales.fixtures.CreateCarRequestFixture;
import com.store.carsales.repository.CarRepository;
import com.store.carsales.request.CreateCarRequest;

@ExtendWith(SpringExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository repository;

    @InjectMocks
    private CarService service;

    @Test
    @DisplayName("Find all returns a page of list cars")
    void findAll_ReturnsPageOfListCar_WhenSuccessful() {
        PageImpl<Car> carPage = new PageImpl<>(List.of(CarFixture.createCar()));
        when(repository.findAll(any(PageRequest.class))).thenReturn(carPage);
        Page<Car> output = service.findAll(PageRequest.of(1, 2));
        Assertions.assertThat(output).isNotNull();
        Assertions.assertThat(output.toList()).isNotEmpty().hasSize(1);
        verify(repository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("List all returns a list of cars")
    void listAll_ReturnsListOfCar_WhenSuccessful() {
        when(repository.findAllByOrderByNameAsc()).thenReturn(List.of(CarFixture.createCar()));
        List<Car> cars = service.findAll();
        Assertions.assertThat(cars).isNotNull();
        Assertions.assertThat(cars).isNotEmpty().hasSize(1);
        verify(repository, times(1)).findAllByOrderByNameAsc();
    }

    @Test
    @DisplayName("FindOne returns car when successful")
    void findOne_ReturnsCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(CarFixture.createCar()));
        Car car = service.findOne(expected.getId());
        Assertions.assertThat(car).isNotNull();
        Assertions.assertThat(car).isEqualTo(expected);
        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("FindByName returns car when successful")
    void findByName_ReturnsCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(repository.findByName(anyString())).thenReturn(List.of(CarFixture.createCar()));
        List<Car> output = service.findByName(expected.getName());
        Assertions.assertThat(output).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(output.get(0)).isEqualTo(expected);
        verify(repository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("FindByName returns empty list when is not found")
    void findByName_ReturnsEmptyList_WhenCarIsNotFound() {
        when(repository.findByName(anyString())).thenReturn(Collections.emptyList());
        List<Car> output = service.findByName("car");
        Assertions.assertThat(output).isNotNull().isEmpty();
        verify(repository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("Save returns new car when successful")
    void save_ReturnsNewCar_WhenSuccessful() {
        Car expected = CarFixture.createCar();
        when(repository.save(any(Car.class))).thenReturn(expected);
        CreateCarRequest createCarRequest = CreateCarRequestFixture.createCarRequest();
        Car output = service.save(createCarRequest);
        Assertions.assertThat(output).isNotNull().isEqualTo(expected);
        verify(repository, times(1)).save(any(Car.class));
    }

    @Test
    @DisplayName("Delete removes car when successful")
    void delete_RemovesCar_WhenSuccessful() {
        doNothing().when(repository).deleteById(any(UUID.class));
        service.delete(UUID.randomUUID());
        verify(repository, times(1)).deleteById(any(UUID.class));
    }
}
