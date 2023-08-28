package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.mappers.LandlordContactMapper;
import org.auspicode.cml.realestatedbaccess.mappers.LandlordMapper;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.repositories.LandlordContactRepository;
import org.auspicode.cml.realestatedbaccess.repositories.LandlordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.LANDLORD_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.LANDLORD_NOT_IN_DB;

@Service
@AllArgsConstructor
public class LandlordService {

    private final LandlordRepository landlordRepository;

    private final LandlordContactRepository landlordContactRepository;

    private final LandlordMapper landlordMapper;

    private final LandlordContactMapper landlordContactMapper;

    @Transactional
    public List<UserResponse> retrieveLandlords() {
        return landlordMapper.toModel(landlordRepository.findAll());
    }

    @Transactional
    public UserResponse findOne(String landlordNif, String landlordIdCardNumber, String landlordFullName) {
        Optional<LandlordEntity> landlordEntity = landlordRepository.findByIdNifAndIdIdCardNumberAndIdFullName(landlordNif, landlordIdCardNumber, landlordFullName);
        if (landlordEntity.isEmpty()) {
            throw new NoSuchElementException(LANDLORD_NOT_IN_DB);
        }
        return landlordMapper.toModel(landlordEntity.get());
    }

    @Transactional
    public UserResponse findByNif(String landlordNif) {
        LandlordEntity landlordEntity = findEntityByNif(landlordNif);
        return landlordMapper.toModel(landlordEntity);
    }

    public UserResponse createLandlord(CreateUserRequest landlord) {
        Optional<LandlordEntity> landlordInDb = landlordRepository.findByIdNifAndIdIdCardNumberAndIdFullName(landlord.getNif(), landlord.getIdCardNumber(), landlord.getFullName());
        if (landlordInDb.isPresent()) {
            throw new EntryAlreadyInDbException(LANDLORD_ALREADY_IN_DB);
        }
        LandlordEntity landlordEntity = landlordMapper.createLandlordRequestToEntity(landlord);
        return landlordMapper.toModel(landlordRepository.save(landlordEntity));
    }

    @Transactional
    public UserResponse createContact(String landlordNif, Contact contact) {
        LandlordEntity landlordEntity = findEntityByNif(landlordNif);
        landlordContactRepository.save(landlordContactMapper.createContactRequestToEntity(landlordEntity, contact));
        return landlordMapper.toModel(landlordEntity);
    }

    @Transactional
    public UserResponse updateLandlord(String landlordNif, String landlordNib) {
        LandlordEntity landlordEntity = findEntityByNif(landlordNif);
        landlordEntity.setNib(landlordNib);
        return landlordMapper.toModel(landlordEntity);
    }

    @Transactional
    public UserResponse deleteLandlord(String landlordNif) {
        LandlordEntity landlordEntity = findEntityByNif(landlordNif);
        landlordRepository.delete(landlordEntity);
        return landlordMapper.toModel(landlordEntity);
    }

    @Transactional
    public Boolean deleteContact(String landlordNif, Contact contact) {
        landlordContactRepository.deleteByLandlordEntityAndContactTypeAndValue(findEntityByNif(landlordNif), contact.getContactType(), contact.getValue());
        return true;
    }

    private LandlordEntity findEntityByNif(String landlordNif) {
        Optional<LandlordEntity> landlordEntity = landlordRepository.findByIdNif(landlordNif);
        if (landlordEntity.isEmpty()) {
            throw new NoSuchElementException(LANDLORD_NOT_IN_DB);
        }
        return landlordEntity.get();
    }

    protected Set<LandlordEntity> findLandlordsByNif(List<String> nifList) {
        return nifList.stream()
                .map(nif -> {
                    Optional<LandlordEntity> landlordEntity = landlordRepository.findByIdNif(nif);
                    if (landlordEntity.isEmpty()) {
                        throw new NoSuchElementException(LANDLORD_NOT_IN_DB.concat(" with nif ").concat(nif));
                    }
                    return landlordEntity.get();
                })
                .collect(Collectors.toSet());
    }
}
