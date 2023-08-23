package org.auspicode.cml.realestatedbaccess.repositories;

import lombok.NonNull;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, String> {

    @NonNull Optional<UnitEntity> findById(@NonNull String id);
}
