package com.store.carsales.integration;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.store.carsales.domain.Car;
import com.store.carsales.domain.DomainUser;
import com.store.carsales.fixtures.CarFixture;
import com.store.carsales.fixtures.CreateCarRequestFixture;
import com.store.carsales.fixtures.DomainUserFixture;
import com.store.carsales.repository.CarRepository;
import com.store.carsales.repository.DomainUserRepository;
import com.store.carsales.request.CreateCarRequest;
import com.store.carsales.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CarControllerIntegration {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DomainUserRepository domainUserRepository;

    @Autowired
    private CarRepository repository;

    @TestConfiguration
    @Lazy
    static class Config {

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            String rootUri = String.format("http://localhost:%s", port);
            RestTemplateBuilder basicAuthentication = new RestTemplateBuilder()
                    .rootUri(rootUri)
                    .basicAuthentication("usuario", "123456");
            return new TestRestTemplate(basicAuthentication);
        }
    }

    void saveDomainUser() {
        DomainUser user = DomainUserFixture.build();
        domainUserRepository.save(user);
    }

    @BeforeEach
    void init() {
        saveDomainUser();
    }

    @Test
    @DisplayName("Find all returns a page of list cars")
    void findAll_ReturnsPageOfListCar_WhenSuccessful() {
        Car saved = repository.save(CarFixture.createCar());

        PageableResponse<Car> exchange = testRestTemplate.exchange("/cars", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Car>>() {
                }).getBody();

        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(exchange.toList().get(0)).isEqualTo(saved);
    }

    @Test
    @DisplayName("List all returns a list of cars")
    void listAll_ReturnsListOfCar_WhenSuccessful() {
        Car saved = repository.save(CarFixture.createCar());

        List<Car> exchange = testRestTemplate.exchange("/cars/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Car>>() {
                }).getBody();

        Assertions.assertThat(exchange).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(exchange.get(0)).isEqualTo(saved);
    }

    @Test
    @DisplayName("FindOne returns car when successful")
    void findOne_ReturnsCar_WhenSuccessful() {
        Car saved = repository.save(CarFixture.createCar());

        UUID expectedId = saved.getId();

        Car output = testRestTemplate.getForObject("/cars/{id}", Car.class, expectedId);

        Assertions.assertThat(output).isNotNull().isEqualTo(saved);
    }

    @Test
    @DisplayName("FindByName returns car when successful")
    void findByName_ReturnsCar_WhenSuccessful() {
        Car saved = repository.save(CarFixture.createCar());
        String url = String.format("/cars/find?name=%s", saved.getName());
        List<Car> exchange = testRestTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Car>>() {
                }).getBody();

        Assertions.assertThat(exchange).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(exchange.get(0)).isEqualTo(saved);
    }

    @Test
    @DisplayName("FindByName returns empty list when is not found")
    void findByName_ReturnsEmptyList_WhenCarIsNotFound() {
        List<Car> exchange = testRestTemplate.exchange("/cars/find?name=test", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Car>>() {
                }).getBody();

        Assertions.assertThat(exchange).isEmpty();
    }

    @Test
    @DisplayName("Save returns new car when successful")
    void save_ReturnsNewCar_WhenSuccessful() {
        CreateCarRequest createCarRequest = CreateCarRequestFixture.createCarRequest();

        ResponseEntity<Car> postForEntity = testRestTemplate.postForEntity("/cars",
                createCarRequest, Car.class);

        Assertions.assertThat(postForEntity).isNotNull();
        Assertions.assertThat(postForEntity.getBody()).isNotNull();
        Assertions.assertThat(postForEntity.getBody().getId()).isNotNull();
        Assertions.assertThat(postForEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Delete removes car when successful")
    void delete_RemovesCar_WhenSuccessful() {
        Car saved = repository.save(CarFixture.createCar());
        ResponseEntity<Void> output = testRestTemplate.exchange("/cars/{id}",
                HttpMethod.DELETE, null, Void.class, saved.getId());

        Assertions.assertThat(output).isNotNull();
    }

}
