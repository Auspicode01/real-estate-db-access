package org.auspicode.cml.realestatedbaccess.repositories;

import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntityId;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends UserRepository<TenantEntity, TenantEntityId> {
}
