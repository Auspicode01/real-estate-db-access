package org.auspicode.cml.realestatedbaccess.repositories;

import lombok.NonNull;
import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @NonNull Optional<ContractEntity> findById(@NonNull Long id);

    List<ContractEntity> findByUnitId(UnitEntity unitEntity);

    @Query(value = "SELECT CONTRACTS.* FROM CONTRACTS, LANDLORD_CONTRACT WHERE LANDLORD_CONTRACT.LANDLORD_NIF = ?1 AND CONTRACTS.ID = LANDLORD_CONTRACT.CONTRACT_ID",
            nativeQuery = true)
    List<ContractEntity> findByLandlordNif(String landlordNif);

    @Query(value = "SELECT CONTRACTS.* FROM CONTRACTS, TENANT_CONTRACT WHERE TENANT_CONTRACT.TENANT_NIF = ?1 AND CONTRACTS.ID = TENANT_CONTRACT.CONTRACT_ID",
            nativeQuery = true)
    List<ContractEntity> findByTenantNif(String tenantNif);

    Optional<ContractEntity> findByRoomId(RoomEntity roomEntity);
}
