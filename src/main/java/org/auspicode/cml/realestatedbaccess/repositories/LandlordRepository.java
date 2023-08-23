package org.auspicode.cml.realestatedbaccess.repositories;

import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntityId;
import org.springframework.stereotype.Repository;

@Repository
public interface LandlordRepository extends UserRepository<LandlordEntity, LandlordEntityId> {
}
