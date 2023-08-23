package org.auspicode.cml.realestatedbaccess.repositories;

import lombok.NonNull;
import org.auspicode.cml.realestatedbaccess.entities.ContactType;
import org.auspicode.cml.realestatedbaccess.entities.LandlordContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandlordContactRepository extends JpaRepository<LandlordContactEntity, Long> {

    @NonNull Optional<LandlordContactEntity> findById(@NonNull Long id);

    void deleteByLandlordEntityAndContactTypeAndValue(LandlordEntity landlord, ContactType type, String value);
}
