package com.colak.springjpatesttutorial.repository;

import com.colak.springjpatesttutorial.jpa.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryH2Test {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("JUnit test for findByLastName operation")
    void whenFindByLastName_thenReturnPerson() {
        // Given
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        personRepository.save(person);

        // When
        List<Person> foundPersons = personRepository.findByLastName("Doe");

        // Then
        assertThat(foundPersons).hasSize(1);
        assertThat(foundPersons.get(0).getFirstName()).isEqualTo("John");
    }
}

