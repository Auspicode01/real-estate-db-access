package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.exception.EntryAlreadyInDbException;
import org.auspicode.cml.realestatedbaccess.mappers.TenantContactMapper;
import org.auspicode.cml.realestatedbaccess.mappers.TenantMapper;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.auspicode.cml.realestatedbaccess.models.CreateTenantRequest;
import org.auspicode.cml.realestatedbaccess.models.TenantResponse;
import org.auspicode.cml.realestatedbaccess.repositories.TenantContactRepository;
import org.auspicode.cml.realestatedbaccess.repositories.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public List<TenantResponse> retrieveTenants() {
        return tenantMapper.toModel(tenantRepository.findAll());
    }

    @Transactional
    public TenantResponse findOne(String nif, String idCardNumber, String fullName) {
        Optional<TenantEntity> tenantEntity = tenantRepository.findByIdNifAndIdIdCardNumberAndIdFullName(nif, idCardNumber, fullName);
        if (tenantEntity.isEmpty()) {
            throw new NoSuchElementException(TENANT_NOT_IN_DB);
        }
        return tenantMapper.toModel(tenantEntity.get());
    }

    @Transactional
    public TenantResponse findByNif(String nif) {
        TenantEntity tenantEntity = findEntityByNif(nif);
        return tenantMapper.toModel(tenantEntity);
    }

    public TenantEntity createTenant(CreateTenantRequest tenant) {
        Optional<TenantEntity> tenantInDb = tenantRepository.findByIdNifAndIdIdCardNumberAndIdFullName(tenant.getNif(), tenant.getIdCardNumber(), tenant.getFullName());
        if (tenantInDb.isPresent()) {
            throw new EntryAlreadyInDbException(TENANT_ALREADY_IN_DB);
        }
        return tenantRepository.save(tenantMapper.createTenantRequestToEntity(tenant));
    }

    @Transactional
    public TenantResponse createContact(String tenantNif, Contact contact) {
        TenantEntity tenantEntity = findEntityByNif(tenantNif);
        tenantContactRepository.save(tenantContactMapper.createContactRequestToEntity(tenantEntity, contact));
        return tenantMapper.toModel(tenantEntity);
    }

    @Transactional
    public TenantEntity deleteTenant(String nif) {
        TenantEntity tenantEntity = findEntityByNif(nif);
        tenantRepository.delete(tenantEntity);
        return tenantEntity;
    }

    protected TenantEntity findEntityByNif(String nif) {
        Optional<TenantEntity> tenantEntity = tenantRepository.findByIdNif(nif);
        if (tenantEntity.isEmpty()) {
            throw new NoSuchElementException(TENANT_NOT_IN_DB);
        }
        return tenantEntity.get();
    }
}
