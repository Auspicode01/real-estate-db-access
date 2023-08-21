package org.auspicode.cml.realestatedbaccess.repositories;

import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, TenantEntityId> {

    Optional<TenantEntity> findByIdNifAndIdIdCardNumberAndIdFullName(String nif, String idCardNumber, String fullName);

    Optional<TenantEntity> findByIdNif(String nif);
}
