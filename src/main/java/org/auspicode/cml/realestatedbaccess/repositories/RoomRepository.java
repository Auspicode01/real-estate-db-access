package org.auspicode.cml.realestatedbaccess.repositories;

import lombok.NonNull;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    @NonNull Optional<RoomEntity> findById(@NonNull Long id);

    @Query(value = "SELECT * FROM Rooms WHERE ID = ?1 AND UNIT_ID = ?2",
            nativeQuery = true)
    Optional<RoomEntity> findByIdAndUnitId(Long id, String unitEntity);

    @Query(value = "SELECT * FROM Rooms WHERE ID = ?1 AND Available = 1",
            nativeQuery = true)
    Optional<RoomEntity> findByIdAndIsAvailable(Long id);
}
