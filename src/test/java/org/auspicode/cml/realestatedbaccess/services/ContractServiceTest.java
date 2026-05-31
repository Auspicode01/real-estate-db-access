package org.auspicode.cml.realestatedbaccess.services;

import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.exception.customExceptions.NoSuchRoomException;
import org.auspicode.cml.realestatedbaccess.exception.customExceptions.RoomIsOccupiedException;
import org.auspicode.cml.realestatedbaccess.models.ContractResponse;
import org.auspicode.cml.realestatedbaccess.models.CreateContractRequest;
import org.auspicode.cml.realestatedbaccess.repositories.ContractRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.*;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/contracts/contracts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/datasets/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
final class ContractServiceTest extends DbTestContainer {

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    ContractService contractService;

    @Test
    void whenRetrieveContracts_ReturnContractsInDB() {
        List<ContractResponse> contractResponseList = contractService.retrieveContracts();

        assertThat(contractResponseList).hasSize(2);
    }

    @Test
    void whenFindOneContract_ReturnContract() {
        ContractResponse contractResponse = contractService.findOne(CONTRACT_ID);

        assertThat(contractResponse.getId()).isEqualTo(CONTRACT_ID);
    }

    @Test
    void whenFindOneContractNotInDB_ReturnContractNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            contractService.findOne(NON_EXISTENT_CONTRACT_ID);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(CONTRACT_NOT_IN_DB);
    }

    @Test
    void whenFindByUnitId_ReturnContract() {
        List<ContractResponse> contractResponseList = contractService.findByUnitId(UNIT_ID);

        assertThat(contractResponseList).hasSize(1);
        assertThat(contractResponseList.get(0).getUnit().getId()).isEqualTo(UNIT_ID);
    }

    @Test
    void whenFindByUnitIdNotInDB_ReturnContractNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            contractService.findByUnitId(UNIT_ID_WITHOUT_CONTRACT);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(CONTRACT_NOT_IN_DB);
    }

    @Test
    void whenFindByLandlordNif_ReturnContract() {
        List<ContractResponse> contractResponseList = contractService.findByLandlordNif(USER_NIF);

        assertThat(contractResponseList).hasSize(1);
    }

    @Test
    void whenFindByLandlordNifNotInDB_ReturnContractNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            contractService.findByLandlordNif(USER_NIF_WITHOUT_CONTRACT);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(CONTRACT_NOT_IN_DB);
    }

    @Test
    void whenFindByTenantNif_ReturnContract() {
        List<ContractResponse> contractResponseList = contractService.findByTenantNif(USER_NIF);

        assertThat(contractResponseList).hasSize(1);
    }

    @Test
    void whenFindByTenantNifNotInDB_ReturnContractNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            contractService.findByTenantNif(USER_NIF_WITHOUT_CONTRACT);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(CONTRACT_NOT_IN_DB);
    }

    @Test
    void whenFindByRoomId_ReturnContract() {
        Long roomId = 2L;
        ContractResponse contractResponseList = contractService.findByRoomId(roomId);

        assertThat(contractResponseList.getRoom().getId()).isEqualTo(roomId);
    }

    @Test
    void whenFindByRoomIdNotInDB_ReturnContractNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            contractService.findByRoomId(ROOM_ID);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(CONTRACT_NOT_IN_DB);
    }

    @Test
    void whenCreateContract_SaveContractInDB() {
        CreateContractRequest contractToSave = CreateContractRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 5, 31))
                .unitId(UNIT_ID)
                .roomId(ROOM_ID)
                .landlordsNif(List.of(USER_NIF))
                .tenantsNif(List.of(USER_NIF))
                .type("Fraction")
                .build();

        ContractResponse savedContract = contractService.createContract(contractToSave);

        Optional<ContractEntity> result = contractRepository.findById(3L);

        assertThat(savedContract.getRoom().getId()).isEqualTo(result.get().getRoomId().getId());
        assertThat(savedContract.getUnit().getId()).isEqualTo(result.get().getUnitId().getId());
    }

    @Test
    void whenCreateContractToUnavailableRoom_ReturnRoomIsOccupiedException() {
        CreateContractRequest contractToSave = CreateContractRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 5, 31))
                .unitId(UNIT_ID)
                .roomId(2L)
                .landlordsNif(List.of(USER_NIF))
                .tenantsNif(List.of(USER_NIF))
                .type("Fraction")
                .build();

        RoomIsOccupiedException roomIsOccupiedException = assertThrows(RoomIsOccupiedException.class, () -> {
            contractService.createContract(contractToSave);
        });

        assertThat(roomIsOccupiedException.getMessage()).isEqualTo(ROOM_NOT_AVAILABLE);
    }

    @Test
    void whenCreateContractWithUnmatchedUnitAndRoom_ReturnNoSuchRoomException() {
        CreateContractRequest contractToSave = CreateContractRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 5, 31))
                .unitId(UNIT_ID)
                .roomId(3L)
                .landlordsNif(List.of(USER_NIF))
                .tenantsNif(List.of(USER_NIF))
                .type("Fraction")
                .build();

        NoSuchRoomException noSuchRoomException = assertThrows(NoSuchRoomException.class, () -> {
            contractService.createContract(contractToSave);
        });

        assertThat(noSuchRoomException.getMessage()).isEqualTo(ROOM_DOES_NOT_MATCH_UNIT);
    }

    @Test
    void whenDeleteContract_DeleteContractFromDB() {
        contractService.deleteContract(CONTRACT_ID);

        List<ContractResponse> result = contractService.retrieveContracts();

        assertThat(result).hasSize(1);
    }
}