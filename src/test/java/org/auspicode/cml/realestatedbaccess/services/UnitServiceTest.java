package org.auspicode.cml.realestatedbaccess.services;

import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.customExceptions.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.repositories.UnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.UNIT_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.UNIT_NOT_IN_DB;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/units/units.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/datasets/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UnitServiceTest extends DbTestContainer {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    UnitService unitService;

    @Test
    void whenRetrieveUnits_ReturnUnitsInDB() {
        List<UnitResponse> unitResponseList = unitService.retrieveUnits();

        assertThat(unitResponseList).hasSize(2);
    }

    @Test
    void whenFindOneUnit_ReturnUnit() {
        UnitResponse unitResponse = unitService.findOne(UNIT_ID);

        assertThat(unitResponse.getId()).isEqualTo(UNIT_ID);
    }

    @Test
    void whenFindOneUnitNotInDB_ReturnUnitNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            unitService.findOne(NON_EXISTENT_UNIT_ID);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(UNIT_NOT_IN_DB);
    }

    @Test
    void whenFindByLandlordNif_ReturnUnit() {
        List<UnitResponse> unitResponseList = unitService.findByLandlordNif(USER_NIF);

        assertThat(unitResponseList.get(0).getLandlordEntity().getNif()).isEqualTo(USER_NIF);
        assertThat(unitResponseList).hasSize(1);
    }

    @Test
    void whenFindLandlordNifNotInDB_ReturnEmptyList() {
        List<UnitResponse> unitResponseList = unitService.findByLandlordNif(NON_EXISTENT_USER_NIF);

        assertThat(unitResponseList).isEmpty();
    }

    @Test
    void whenCreateUnit_SaveUnitInDB() {
        CreateUnitRequest unitToSave = CreateUnitRequest.builder()
                .id(NON_EXISTENT_UNIT_ID)
                .street("Travessa das Leirinhas")
                .postalCode("3810-009")
                .article("9900")
                .registerNumber("9900")
                .town("Aveiro")
                .fraction("")
                .typology("T2")
                .build();

        UnitResponse savedUnit = unitService.createUnit(USER_NIF, unitToSave);

        Optional<UnitEntity> result = unitRepository.findById(NON_EXISTENT_UNIT_ID);

        assertThat(savedUnit.getId()).isEqualTo(result.get().getId());
        assertThat(savedUnit.getStreet()).isEqualTo(result.get().getStreet());
    }

    @Test
    void whenCreateUnitThatAlreadyExists_ReturnEntryAlreadyInDBException() {
        CreateUnitRequest unitToSave = CreateUnitRequest.builder()
                .id(UNIT_ID)
                .street("Travessa das Leirinhas")
                .postalCode("3810-001")
                .article("9999")
                .registerNumber("9999")
                .town("Aveiro")
                .fraction("")
                .typology("T2")
                .build();

        EntryAlreadyInDbException entryAlreadyInDbException = assertThrows(EntryAlreadyInDbException.class, () -> {
            unitService.createUnit(USER_NIF, unitToSave);
        });

        assertThat(entryAlreadyInDbException.getMessage()).isEqualTo(UNIT_ALREADY_IN_DB);
    }

    @Test
    void whenUpdateUnit_changeAllUpdatableValues() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .street("Rua dos Pedreiros")
                .fraction("2ºESQ")
                .typology("T3")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getStreet()).isEqualTo(updateUnitRequest.getStreet());
        assertThat(updatedUnit.getFraction()).isEqualTo(updateUnitRequest.getFraction());
        assertThat(updatedUnit.getTypology()).isEqualTo(updateUnitRequest.getTypology());
    }

    @Test
    void whenUpdateUnit_changeStreetValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .street("Rua dos Pedreiros")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getStreet()).isEqualTo(updateUnitRequest.getStreet());
    }

    @Test
    void whenUpdateUnit_changeFractionValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .fraction("2ºESQ")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getFraction()).isEqualTo(updateUnitRequest.getFraction());
    }

    @Test
    void whenUpdateUnit_changeTypologyValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .typology("T3")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getTypology()).isEqualTo(updateUnitRequest.getTypology());
    }

    @Test
    void whenDeleteUnit_DeleteUnitFromDB() {
        unitService.deleteUnit(UNIT_ID);

        List<UnitResponse> result = unitService.retrieveUnits();

        assertThat(result).hasSize(1);
    }
}