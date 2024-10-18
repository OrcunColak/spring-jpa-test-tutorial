package com.colak.springtutorial.repository;

import com.colak.springtutorial.jpa.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// Disable the transactional and roll back in @DataJpaTest
// @Transactional(propagation = Propagation.NOT_SUPPORTED)
// Disable H2
// See https://erkanyasun.medium.com/a-deep-dive-into-the-latest-spring-boot-3-4-0-m3-52ca7e03db2d
// With 3.4.0-M3 the @AutoConfigureTestDatabase annotation has been upgraded to automatically detect if a database is sourced from a container.
// Previously, using @AutoConfigureTestDatabase with containerized databases required the replace=Replace.NONE attribute.
// With this release, this manual configuration is no longer necessary, simplifying your test setup.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PersonRepositoryPostgresTest {

    @SuppressWarnings("resource")
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres"))
                    .withPassword("inmemory")
                    .withUsername("inmemory");

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("JUnit test for findByLastName operation")
    void findByLastName() {
        // Given
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        personRepository.save(person);

        // When
        List<Person> foundPersons = personRepository.findByLastName("Doe");

        // Then
        assertThat(foundPersons).hasSizeGreaterThanOrEqualTo(1);

        Person firstPerson = foundPersons.getFirst();
        assertThat(firstPerson.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("JUnit test for findById operation")
    void findById() {
        // Given
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");
        Person savedPerson = personRepository.save(person);

        // When
        Optional<Person> foundPersonOptional = personRepository.findById(savedPerson.getId());

        // Then
        assertThat(foundPersonOptional).isPresent()
                .hasValueSatisfying(foundPerson -> {
                            assertThat(foundPerson.getFirstName()).isEqualTo("Jane");
                            assertThat(foundPerson.getLastName()).isEqualTo("Doe");
                        }
                );
    }
}

