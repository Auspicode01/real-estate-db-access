package org.auspicode.cml.realestatedbaccess.services;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.repositories.UnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.UNIT_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.UNIT_NOT_IN_DB;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.UNIT_ID;
import static org.auspicode.cml.realestatedbaccess.testConstants.TestConstants.USER_NIF;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DBRider
@DBUnit(allowEmptyFields = true)
@SpringBootTest
class UnitServiceTest {

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    UnitService unitService;

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenRetrieveUnits_ReturnUnitsInDB() {
        List<UnitResponse> unitResponseList = unitService.retrieveUnits();

        assertThat(unitResponseList.size()).isEqualTo(2);
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenFindOneUnit_ReturnUnit() {
        UnitResponse unitResponse = unitService.findOne(UNIT_ID);

        assertThat(unitResponse.getId()).isEqualTo(UNIT_ID);
    }

    @Test
    void whenFindOneUnitNotInDB_ReturnUnitNotInDBException() {
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            unitService.findOne(UNIT_ID);
        });

        assertThat(noSuchElementException.getMessage()).isEqualTo(UNIT_NOT_IN_DB);
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenFindByLandlordNif_ReturnUnit() {
        List<UnitResponse> unitResponseList = unitService.findByLandlordNif(USER_NIF);

        assertThat(unitResponseList.get(0).getLandlordEntity().getNif()).isEqualTo(USER_NIF);
        assertThat(unitResponseList.size()).isEqualTo(1);
    }

    @Test
    void whenFindLandlordNifNotInDB_ReturnEmptyList() {
        List<UnitResponse> unitResponseList = unitService.findByLandlordNif(USER_NIF);

        assertThat(unitResponseList.size()).isEqualTo(0);
    }

    @Test
    @DataSet(value = "datasets/landlords/landlords.yml", cleanAfter = true)
    void whenCreateUnit_SaveUnitInDB() {
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

        UnitResponse savedUnit = unitService.createUnit(USER_NIF, unitToSave);

        Optional<UnitEntity> result = unitRepository.findById(UNIT_ID);

        assertThat(savedUnit.getId()).isEqualTo(result.get().getId());
        assertThat(savedUnit.getStreet()).isEqualTo(result.get().getStreet());
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
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
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
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
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenUpdateUnit_changeStreetValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .street("Rua dos Pedreiros")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getStreet()).isEqualTo(updateUnitRequest.getStreet());
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenUpdateUnit_changeFractionValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .fraction("2ºESQ")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getFraction()).isEqualTo(updateUnitRequest.getFraction());
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenUpdateUnit_changeTypologyValue() {
        UpdateUnitRequest updateUnitRequest = UpdateUnitRequest.builder()
                .typology("T3")
                .build();
        UnitResponse updatedUnit = unitService.updateUnit(UNIT_ID, updateUnitRequest);

        assertThat(updatedUnit.getTypology()).isEqualTo(updateUnitRequest.getTypology());
    }

    @Test
    @DataSet(value = "datasets/units/units.yml", cleanAfter = true)
    void whenDeleteUnit_DeleteUnitFromDB() {
        unitService.deleteUnit(UNIT_ID);

        List<UnitResponse> result = unitService.retrieveUnits();

        assertThat(result.size()).isEqualTo(1);
    }
}