package org.auspicode.cml.realestatedbaccess.services;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import jakarta.transaction.Transactional;
import org.auspicode.cml.realestatedbaccess.entities.ContactType;
import org.auspicode.cml.realestatedbaccess.entities.TenantContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.repositories.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_NOT_IN_DB;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DBRider
@SpringBootTest
class TenantServiceTest {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    TenantService tenantService;

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenRetrieveTenants_ReturnTenantsInDB() {
        List<UserResponse> tenantsList = tenantService.retrieveTenants();

        assertThat(tenantsList.size()).isEqualTo(2);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenFindOneTenant_ReturnTenant() {
        UserResponse result = tenantService.findOne(USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
    }

    @Test
    void whenFindOneTenantNotInDB_ReturnTenantNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            tenantService.findOne(USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(TENANT_NOT_IN_DB);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenFindByNif_ReturnTenant() {
        UserResponse result = tenantService.findByNif(USER_NIF);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
    }

    @Test
    void whenFindByNifNotInDB_ReturnTenantNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            tenantService.findByNif(USER_NIF);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(TENANT_NOT_IN_DB);
    }

    @Test
    void whenCreateTenant_SaveTenantInDB() {
        CreateUserRequest tenantToSave = CreateUserRequest.builder()
                .nif("123.123.123")
                .idCardNumber("29904882")
                .fullName("Idalinda Gama")
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1999, 7, 01))
                .build();

        UserResponse savedTenant = tenantService.createTenant(tenantToSave);

        Optional<TenantEntity> result = tenantRepository.findByIdNif("123.123.123");

        assertThat(savedTenant.getNif()).isEqualTo(result.get().getId().getNif());
        assertThat(savedTenant.getNib()).isEqualTo(result.get().getNib());
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenCreateTenantThatAlreadyExists_ReturnEntryAlreadyInDBException() {
        CreateUserRequest tenantToSave = CreateUserRequest.builder()
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        EntryAlreadyInDbException entryAlreadyInDbException = assertThrows(EntryAlreadyInDbException.class, () -> {
            tenantService.createTenant(tenantToSave);
        });

        assertThat(entryAlreadyInDbException.getMessage()).isEqualTo(TENANT_ALREADY_IN_DB);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    @Transactional
    void whenCreateTenantContact_SaveTenantContactInDB() {
        Contact contact = Contact.builder()
                .contactType(ContactType.EMAIL)
                .contact("email@mail.com")
                .build();
        tenantService.createContact(USER_NIF, contact);

        TenantContactEntity result = tenantRepository.findByIdNif(USER_NIF).get().getContacts().iterator().next();

        assertThat(result.getContactType()).isEqualTo(contact.getContactType());
        assertThat(result.getContact()).isEqualTo(contact.getContact());
        assertThat(result.getTenantEntity().getId().getNif()).isEqualTo(USER_NIF);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenUpdateTenantNib_SaveNewTenantNib() {
        String newNib = "PT50002200003426584958633";

        tenantService.updateTenant(USER_NIF, newNib);

        UserResponse result = tenantService.findByNif(USER_NIF);

        assertThat(result.getNib()).isEqualTo(newNib);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants.yml", cleanAfter = true)
    void whenDeleteTenant_DeleteTenantFromDB() {
        tenantService.deleteTenant(USER_NIF);

        List<UserResponse> result = tenantService.retrieveTenants();

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DataSet(value = "datasets/tenants/tenants_with_contacts.yml", cleanAfter = true)
    void whenDeleteTenantContact_DeleteTenantContactFromDB() {
        String tenantNif = "123.445.249";
        Contact contactToDelete = tenantService.findByNif(tenantNif).getContacts().iterator().next();

        tenantService.deleteContact(tenantNif, contactToDelete);

        Set<Contact> result = tenantService.findByNif(tenantNif).getContacts();

        assertThat(result.size()).isEqualTo(0);
    }
}