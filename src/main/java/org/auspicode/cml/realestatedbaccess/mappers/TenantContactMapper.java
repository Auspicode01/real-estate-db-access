package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.TenantContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.TenantEntity;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TenantContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantEntity", source = "tenantEntity")
    @Mapping(target = "contactType", source = "contact.contactType")
    @Mapping(target = "value", source = "contact.value")
    TenantContactEntity createContactRequestToEntity(TenantEntity tenantEntity, Contact contact);

    @Mapping(target = "contactType", source = "contactType")
    @Mapping(target = "value", source = "value")
    Contact toModel(TenantContactEntity tenantContactEntity);

    List<Contact> toModel(List<TenantContactEntity> tenantContactEntityList);
}
