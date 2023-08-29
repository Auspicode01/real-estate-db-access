package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.mappers.TenantContactMapper;
import org.auspicode.cml.realestatedbaccess.mappers.TenantMapper;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateUserRequest;
import org.auspicode.cml.realestatedbaccess.models.UserResponse;
import org.auspicode.cml.realestatedbaccess.repositories.TenantContactRepository;
import org.auspicode.cml.realestatedbaccess.repositories.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_ALREADY_IN_DB;
import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.TENANT_NOT_IN_DB;

@Service
@AllArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    private final TenantContactRepository tenantContactRepository;

    private final TenantMapper tenantMapper;

    private final TenantContactMapper tenantContactMapper;

    @Transactional
    public List<UserResponse> retrieveTenants() {
        return tenantMapper.toModel(tenantRepository.findAll());
    }

    @Transactional
    public UserResponse findOne(String tenantNif, String tenantIdCardNumber, String tenantFullName) {
        Optional<TenantEntity> tenantEntity = tenantRepository.findByIdNifAndIdIdCardNumberAndIdFullName(tenantNif, tenantIdCardNumber, tenantFullName);
        if (tenantEntity.isEmpty()) {
            throw new NoSuchElementException(TENANT_NOT_IN_DB);
        }
        return tenantMapper.toModel(tenantEntity.get());
    }

    @Transactional
    public UserResponse findByNif(String tenantNif) {
        TenantEntity tenantEntity = findEntityByNif(tenantNif);
        return tenantMapper.toModel(tenantEntity);
    }

    public UserResponse createTenant(CreateUserRequest tenant) {
        Optional<TenantEntity> tenantInDb = tenantRepository.findByIdNifAndIdIdCardNumberAndIdFullName(tenant.getNif(), tenant.getIdCardNumber(), tenant.getFullName());
        if (tenantInDb.isPresent()) {
            throw new EntryAlreadyInDbException(TENANT_ALREADY_IN_DB);
        }
        TenantEntity tenantEntity = tenantMapper.createTenantRequestToEntity(tenant);
        return tenantMapper.toModel(tenantRepository.save(tenantEntity));
    }

    @Transactional
    public UserResponse createContact(String tenantNif, Contact contact) {
        TenantEntity tenantEntity = findEntityByNif(tenantNif);
        tenantContactRepository.save(tenantContactMapper.createContactRequestToEntity(tenantEntity, contact));
        return tenantMapper.toModel(tenantEntity);
    }

    @Transactional
    public UserResponse updateTenant(String tenantNif, String tenantNib) {
        TenantEntity tenantEntity = findEntityByNif(tenantNif);
        tenantEntity.setNib(tenantNib);
        return tenantMapper.toModel(tenantEntity);
    }

    @Transactional
    public UserResponse deleteTenant(String tenantNif) {
        TenantEntity tenantEntity = findEntityByNif(tenantNif);
        tenantRepository.delete(tenantEntity);
        return tenantMapper.toModel(tenantEntity);
    }

    @Transactional
    public Boolean deleteContact(String tenantNif, Contact contact) {
        tenantContactRepository.deleteByTenantEntityAndContactTypeAndValue(findEntityByNif(tenantNif), contact.getContactType(), contact.getValue());
        return true;
    }

    private TenantEntity findEntityByNif(String tenantNif) {
        Optional<TenantEntity> tenantEntity = tenantRepository.findByIdNif(tenantNif);
        if (tenantEntity.isEmpty()) {
            throw new NoSuchElementException(TENANT_NOT_IN_DB);
        }
        return tenantEntity.get();
    }

    protected Set<TenantEntity> findTenantsByNif(List<String> nifList) {
        return nifList.stream()
                .map(nif -> {
                    Optional<TenantEntity> tenantEntity = tenantRepository.findByIdNif(nif);
                    if (tenantEntity.isEmpty()) {
                        throw new NoSuchElementException(TENANT_NOT_IN_DB.concat(" with nif ").concat(nif));
                    }
                    return tenantEntity.get();
                })
                .collect(Collectors.toSet());
    }
}