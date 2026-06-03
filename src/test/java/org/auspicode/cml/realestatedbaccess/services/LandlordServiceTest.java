package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import org.auspicode.cml.realestatedbaccess.entities.ContactType;
import org.auspicode.cml.realestatedbaccess.entities.LandlordContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.exception.customExceptions.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.repositories.LandlordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.LANDLORD_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.LANDLORD_NOT_IN_DB;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/landlords/landlords.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/datasets/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LandlordServiceTest extends DbTestContainer {

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    LandlordService landlordService;

    @Test
    void whenRetrieveLandlords_ReturnLandlordsInDB() {
        List<UserResponse> landlordsList = landlordService.retrieveLandlords();

        assertThat(landlordsList).hasSize(2);
    }

    @Test
    void whenFindOneLandlord_ReturnLandlord() {
        UserResponse result = landlordService.findOne(USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
    }

    @Test
    void whenFindOneLandlordNotInDB_ReturnLandlordNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            landlordService.findOne(NON_EXISTENT_USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(LANDLORD_NOT_IN_DB);
    }

    @Test
    void whenFindByNif_ReturnLandlord() {
        UserResponse result = landlordService.findByNif(USER_NIF);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
    }

    @Test
    void whenFindByNifNotInDB_ReturnLandlordNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            landlordService.findByNif(NON_EXISTENT_USER_NIF);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(LANDLORD_NOT_IN_DB);
    }

    @Test
    void whenCreateLandlord_SaveLandlordInDB() {
        CreateUserRequest landlordToSave = CreateUserRequest.builder()
                .nif("123.123.123")
                .idCardNumber("29904882")
                .fullName("Idalinda Gama")
                .nib("PT50002200003426584958699")
                .birthDate(LocalDate.of(1999, 7, 01))
                .build();

        UserResponse savedLandlord = landlordService.createLandlord(landlordToSave);

        Optional<LandlordEntity> result = landlordRepository.findByIdNif("123.123.123");

        assertThat(savedLandlord.getNif()).isEqualTo(result.get().getId().getNif());
        assertThat(savedLandlord.getNib()).isEqualTo(result.get().getNib());
    }

    @Test
    void whenCreateLandlordThatAlreadyExists_ReturnEntryAlreadyInDBException() {
        CreateUserRequest landlordToSave = CreateUserRequest.builder()
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        EntryAlreadyInDbException entryAlreadyInDbException = assertThrows(EntryAlreadyInDbException.class, () -> {
            landlordService.createLandlord(landlordToSave);
        });

        assertThat(entryAlreadyInDbException.getMessage()).isEqualTo(LANDLORD_ALREADY_IN_DB);
    }

    @Test
    @Transactional
    void whenCreateLandlordContact_SaveLandlordContactInDB() {
        Contact contact = Contact.builder()
                .contactType(ContactType.EMAIL)
                .contact("email@mail.com")
                .build();
        landlordService.createContact(USER_NIF, contact);

        LandlordContactEntity result = landlordRepository.findByIdNif(USER_NIF).get().getContacts().iterator().next();

        assertThat(result.getContactType()).isEqualTo(contact.getContactType());
        assertThat(result.getContact()).isEqualTo(contact.getContact());
        assertThat(result.getLandlordEntity().getId().getNif()).isEqualTo(USER_NIF);
    }

    @Test
    void whenUpdateLandlordNib_SaveNewLandlordNib() {
        String newNib = "PT50002200003426584958633";

        landlordService.updateLandlord(USER_NIF, newNib);

        UserResponse result = landlordService.findByNif(USER_NIF);

        assertThat(result.getNib()).isEqualTo(newNib);
    }

    @Test
    void whenDeleteLandlord_DeleteLandlordFromDB() {
        landlordService.deleteLandlord(USER_NIF);

        List<UserResponse> result = landlordService.retrieveLandlords();

        assertThat(result).hasSize(1);
    }

    @Test
    void whenDeleteLandlordContact_DeleteLandlordContactFromDB() {
        String landlordNif = "123.445.249";
        Contact contactToDelete = landlordService.findByNif(landlordNif).getContacts().iterator().next();

        landlordService.deleteContact(landlordNif, contactToDelete);

        Set<Contact> result = landlordService.findByNif(landlordNif).getContacts();

        assertThat(result).isEmpty();
    }
}