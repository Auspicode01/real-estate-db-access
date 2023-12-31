package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.exception.RoomIsOccupiedException;
import org.auspicode.cml.realestatedbaccess.mappers.UnitMapper;
import org.auspicode.cml.realestatedbaccess.models.CreateUnitRequest;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.repositories.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.*;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    private final LandlordService landlordService;

    private final UnitMapper unitMapper;

    @Transactional
    public List<UnitResponse> retrieveUnits() {
        return unitMapper.toModel(unitRepository.findAll());
    }

    @Transactional
    public UnitResponse findOne(String unitId) {
        UnitEntity unitEntity = findOneEntity(unitId);
        return unitMapper.toModel(unitEntity);
    }

    @Transactional
    public List<UnitResponse> findByLandlordNif(String landlordNif) {
        return unitMapper.toModel(unitRepository.findByLandlordNif(landlordNif));
    }

    @Transactional
    public UnitResponse createUnit(String landlordNif, CreateUnitRequest createUnitRequest) {
        Optional<UnitEntity> unitToCreate = unitRepository.findById(createUnitRequest.getId());
        if (unitToCreate.isPresent()) {
            throw new EntryAlreadyInDbException(UNIT_ALREADY_IN_DB);
        }
        LandlordEntity landlordEntity = landlordService.findEntityByNif(landlordNif);
        UnitEntity unitEntity = unitMapper.toEntity(createUnitRequest, landlordEntity);
        return unitMapper.toModel(unitRepository.save(unitEntity));
    }

    @Transactional
    public UnitResponse updateUnit(String unitId, UpdateUnitRequest updateUnitRequest) {
        UnitEntity unitEntity = findOneEntity(unitId);
        unitMapper.updateUnitToEntity(updateUnitRequest, unitEntity);
        unitRepository.save(unitEntity);
        return unitMapper.toModel(unitEntity);
    }

    @Transactional
    public UnitResponse deleteUnit(String unitId) {
        UnitEntity unitToDelete = findOneEntity(unitId);
        unitToDelete.getRooms().stream()
                .filter(room -> !room.getIsAvailable())
                .findFirst()
                .ifPresent(obj -> {
                    throw new RoomIsOccupiedException(UNIT_CAN_NOT_BE_DELETED);
                });
        unitRepository.delete(unitToDelete);
        return unitMapper.toModel(unitToDelete);
    }

    protected UnitEntity findOneEntity(String unitId) {
        Optional<UnitEntity> unitEntity = unitRepository.findById(unitId);
        if (unitEntity.isEmpty()) {
            throw new NoSuchElementException(UNIT_NOT_IN_DB);
        }
        return unitEntity.get();
    }
}
