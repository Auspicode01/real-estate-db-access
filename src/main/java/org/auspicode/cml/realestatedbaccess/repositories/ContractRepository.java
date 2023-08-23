package org.auspicode.cml.realestatedbaccess.repositories;

import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    Optional<ContractEntity> findById(Long id);

    List<ContractEntity> findByUnitId(UnitEntity unitEntity);

    //List<ContractEntity> findByTenantId(TenantEntity tenantEntity);

    Optional<ContractEntity> findByRoomId(RoomEntity roomEntity);
}
