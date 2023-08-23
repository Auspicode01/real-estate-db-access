package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.exception.RoomIsOccupiedException;
import org.auspicode.cml.realestatedbaccess.mappers.UnitMapper;
import org.auspicode.cml.realestatedbaccess.models.UnitResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateUnitRequest;
import org.auspicode.cml.realestatedbaccess.repositories.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.*;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    public UnitService(UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Transactional
    public List<UnitResponse> retrieveUnits() {
        return unitMapper.toModel(unitRepository.findAll());
    }

    @Transactional
    public UnitResponse findOne(String id) {
        UnitEntity unitEntity = findOneEntity(id);
        return unitMapper.toModel(unitEntity);
    }

    public UnitEntity createUnit(UnitEntity unitEntity) {
        Optional<UnitEntity> unitToCreate = unitRepository.findById(unitEntity.getId());
        if (unitToCreate.isPresent()) {
            throw new EntryAlreadyInDbException(UNIT_ALREADY_IN_DB);
        }
        return unitRepository.save(unitEntity);
    }

    @Transactional
    public UnitResponse updateUnit(String id, UpdateUnitRequest updateUnitRequest) {
        UnitEntity unitEntity = findOneEntity(id);
        unitMapper.updateUnitToEntity(updateUnitRequest, unitEntity);
        unitRepository.save(unitEntity);
        return unitMapper.toModel(unitEntity);
    }

    @Transactional
    public UnitResponse deleteUnit(String id) {
        UnitEntity unitToDelete = findOneEntity(id);
        unitToDelete.getRooms().stream()
                .filter(room -> !room.getIsAvailable())
                .findFirst()
                .ifPresent(obj -> {
                    throw new RoomIsOccupiedException(UNIT_CAN_NOT_BE_DELETED);
                });
        unitRepository.delete(unitToDelete);
        return unitMapper.toModel(unitToDelete);
    }

    protected UnitEntity findOneEntity(String id) {
        Optional<UnitEntity> unitEntity = unitRepository.findById(id);
        if (unitEntity.isEmpty()) {
            throw new NoSuchElementException(UNIT_NOT_IN_DB);
        }
        return unitEntity.get();
    }
}
