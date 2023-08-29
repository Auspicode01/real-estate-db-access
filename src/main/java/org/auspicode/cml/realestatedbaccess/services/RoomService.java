package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.exception.AllRoomsCreatedForUnitException;
import org.auspicode.cml.realestatedbaccess.exception.NoSuchRoomException;
import org.auspicode.cml.realestatedbaccess.exception.RoomIsOccupiedException;
import org.auspicode.cml.realestatedbaccess.mappers.RoomMapper;
import org.auspicode.cml.realestatedbaccess.models.RoomRequest;
import org.auspicode.cml.realestatedbaccess.models.RoomResponse;
import org.auspicode.cml.realestatedbaccess.models.UpdateRoomRequest;
import org.auspicode.cml.realestatedbaccess.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.*;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    private final UnitService unitService;

    private final RoomMapper roomMapper;

    public List<RoomResponse> retrieveRooms() {
        return roomMapper.toModel(roomRepository.findAll());
    }

    public RoomResponse findOne(Long roomId) {
        RoomEntity roomEntity = findOneEntity(roomId);
        return roomMapper.toModel(roomEntity);
    }

    @Transactional
    public List<RoomResponse> findByUnitId(String unitId) {
        UnitEntity unit = unitService.findOneEntity(unitId);
        return roomMapper.toModel(unit.getRooms());
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {
        UnitEntity unit = unitService.findOneEntity(roomRequest.getUnitId());
        if (Boolean.TRUE.equals(checkNumberOfRoomsForUnit(unit))) {
            throw new AllRoomsCreatedForUnitException(ALL_ROOMS_ALREADY_IN_DB);
        }
        RoomEntity roomEntity = roomMapper.toEntity(roomRequest, unit);
        return roomMapper.toModel(roomRepository.save(roomEntity));
    }

    private Boolean checkNumberOfRoomsForUnit(UnitEntity unit) {
        return unit.getRooms().size() == unit.getTypology().getNumberOfRooms();
    }

    public RoomResponse updateRoom(Long roomId, UpdateRoomRequest updateRoomRequest) {
        RoomEntity roomEntity = findOneEntity(roomId);
        roomMapper.updateRoomToEntity(updateRoomRequest, roomEntity);
        roomRepository.save(roomEntity);
        return roomMapper.toModel(roomEntity);
    }

    public RoomResponse deleteRoom(Long roomId) {
        RoomEntity roomToDelete = findOneEntity(roomId);
        if (roomRepository.findByIdAndIsAvailable(roomId).isEmpty()) {
            throw new RoomIsOccupiedException(ROOM_CAN_NOT_BE_DELETED);
        }
        roomRepository.delete(roomToDelete);
        return roomMapper.toModel(roomToDelete);
    }

    protected RoomEntity findOneEntity(Long roomId) {
        Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
        if (roomEntity.isEmpty()) {
            throw new NoSuchElementException(ROOM_NOT_IN_DB);
        }
        return roomEntity.get();
    }

    protected RoomEntity findAvailableRoomToAddToContract(Long roomId, UnitEntity unitEntity) {
        Optional<RoomEntity> roomEntity = roomRepository.findByIdAndUnitId(roomId, unitEntity.getId());
        if (roomEntity.isEmpty()) {
            throw new NoSuchRoomException(ROOM_DOES_NOT_MATCH_UNIT);
        }
        if (roomRepository.findByIdAndIsAvailable(roomId).isEmpty()) {
            throw new RoomIsOccupiedException(ROOM_NOT_AVAILABLE);
        }
        roomEntity.get().setIsAvailable(!roomEntity.get().getIsAvailable());
        return roomEntity.get();
    }

    protected void findRoomToDeleteFromContract(RoomEntity roomEntity) {
        roomEntity.setIsAvailable(!roomEntity.getIsAvailable());
        roomRepository.save(roomEntity);
    }
}
