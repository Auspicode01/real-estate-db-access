package org.auspicode.cml.realestatedbaccess.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.database.rider.core.api.dataset.DataSet;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntityId;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_NOT_IN_DB;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TenantServiceTest {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    TenantService tenantService;

    @Test
    @DataSet
    void whenRetrieveTenants_ReturnTenantsInDB() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TenantEntityId tenant1Id = TenantEntityId.builder()
                .nif("696.609.103")
                .idCardNumber("29384750")
                .fullName("Cristela Santos")
                .build();
        TenantEntity tenant1 = TenantEntity.builder()
                .id(tenant1Id)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        TenantEntityId tenant2Id = TenantEntityId.builder()
                .nif("123.123.123")
                .idCardNumber("29384759")
                .fullName("Horácio Lima")
                .build();
        TenantEntity tenant2 = TenantEntity.builder()
                .id(tenant2Id)
                .nib("PT50002200003426584958600")
                .birthDate(LocalDate.of(1983, 7, 1))
                .build();
        tenantRepository.save(tenant1);
        tenantRepository.save(tenant2);
        String json = objectMapper.writeValueAsString(tenant1);
        String json2 = objectMapper.writeValueAsString(tenant2);
        List<UserResponse> tenantsList = tenantService.retrieveTenants();

        assertThat(tenantsList.size()).isEqualTo(2);
    }

    @Test
    void whenFindOneTenant_ReturnTenant() {
        TenantEntityId tenant1Id = TenantEntityId.builder()
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .build();
        TenantEntity tenant1 = TenantEntity.builder()
                .id(tenant1Id)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        tenantRepository.save(tenant1);

        UserResponse result = tenantService.findOne(USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
        assertThat(result.getNib()).isEqualTo(tenant1.getNib());
    }

    @Test
    void whenFindOneTenantNotInDB_ReturnTenantNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            tenantService.findOne(USER_NIF, USER_ID_CARD_NUMBER, USER_FULL_NAME);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(TENANT_NOT_IN_DB);
    }

    @Test
    void whenFindByNif_ReturnTenant() {
        TenantEntityId tenant1Id = TenantEntityId.builder()
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .build();
        TenantEntity tenant1 = TenantEntity.builder()
                .id(tenant1Id)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        tenantRepository.save(tenant1);

        UserResponse result = tenantService.findByNif(USER_NIF);

        assertThat(result.getNif()).isEqualTo(USER_NIF);
        assertThat(result.getNib()).isEqualTo(tenant1.getNib());
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
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();

        UserResponse SavedTenant = tenantService.createTenant(tenantToSave);

        Optional<TenantEntity> result = tenantRepository.findByIdNif(USER_NIF);

        assertThat(SavedTenant.getNif()).isEqualTo(result.get().getId().getNif());
        assertThat(SavedTenant.getNib()).isEqualTo(result.get().getNib());
    }

    @Test
    void whenCreateTenantThatAlreadyExists_ReturnEntryAlreadyInDBException() {
        TenantEntityId tenant1Id = TenantEntityId.builder()
                .nif(USER_NIF)
                .idCardNumber(USER_ID_CARD_NUMBER)
                .fullName(USER_FULL_NAME)
                .build();
        TenantEntity tenant1 = TenantEntity.builder()
                .id(tenant1Id)
                .nib("PT50002200003426584958622")
                .birthDate(LocalDate.of(1968, 3, 22))
                .build();
        tenantRepository.save(tenant1);

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
}