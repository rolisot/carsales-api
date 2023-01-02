package com.store.carsales.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;

import com.store.carsales.domain.Car;
import com.store.carsales.fixtures.CarFixture;

@DataJpaTest
@DisplayName("Car Repository tests")
class CarRepositoryTest {

    @Autowired
    private CarRepository repository;

    @Test
    @DisplayName("Save inserts car when successful")
    void save_InsertsCar_WhenSuccessful() {
        Car car = CarFixture.createCar();
        Car output = repository.save(car);
        Assertions.assertThat(output)
                .isNotNull()
                .isEqualTo(car);
    }

    @Test
    @DisplayName("Save updates car when successful")
    void save_UpdatesCar_WhenSuccessful() {
        Car car = CarFixture.createCar();
        Car inserted = repository.save(car);

        inserted.setName("Car name was updated!");

        Car updated = repository.save(inserted);

        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getId()).isEqualTo(inserted.getId());
        Assertions.assertThat(updated.getName()).isEqualTo("Car name was updated!");

    }

    @Test
    @DisplayName("Save throws JpaSystemException when name is empty")
    void save_ThrowsJpaSystemException_WhenNameIsEmpty() {
        Car car = new Car();

        Assertions.assertThatThrownBy(() -> repository.save(car))
                .isInstanceOf(JpaSystemException.class);
    }

    @Test
    @DisplayName("Delete removes car when successful")
    void delete_RemovesCar_WhenSuccessful() {
        Car car = CarFixture.createCar();
        Car inserted = repository.save(car);

        repository.delete(inserted);
        Optional<Car> found = repository.findById(inserted.getId());

        Assertions.assertThat(found.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Find by name returns list of car when successful")
    void findByName_ReturnsListOfCar_WhenSuccessful() {
        Car car = CarFixture.createCar();
        Car inserted = repository.save(car);

        List<Car> cars = repository.findByName(inserted.getName());

        Assertions.assertThat(cars)
                .isNotEmpty()
                .contains(inserted);
    }

    @Test
    @DisplayName("Find by name returns empty list of car when no car is found")
    void findByName_ReturnsEmptyListOfCar_WhenCarIsNotFound() {
        List<Car> cars = repository.findByName("no found");

        Assertions.assertThat(cars).isEmpty();
    }
}