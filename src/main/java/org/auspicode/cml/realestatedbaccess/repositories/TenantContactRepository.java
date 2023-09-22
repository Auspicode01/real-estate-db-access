package org.auspicode.cml.realestatedbaccess.repositories;

import lombok.NonNull;
import org.auspicode.cml.realestatedbaccess.entities.ContactType;
import org.auspicode.cml.realestatedbaccess.entities.TenantContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantContactRepository extends JpaRepository<TenantContactEntity, Long> {

    @NonNull Optional<TenantContactEntity> findById(@NonNull Long id);

    void deleteByTenantEntityAndContactTypeAndContact(TenantEntity tenant, ContactType type, String contact);
}
