package org.auspicode.cml.realestatedbaccess.mappers;

import org.auspicode.cml.realestatedbaccess.entities.LandlordContactEntity;
import org.auspicode.cml.realestatedbaccess.entities.LandlordEntity;
import org.auspicode.cml.realestatedbaccess.models.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LandlordContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "landlordEntity", source = "landlordEntity")
    @Mapping(target = "contactType", source = "contact.contactType")
    @Mapping(target = "value", source = "contact.value")
    LandlordContactEntity createContactRequestToEntity(LandlordEntity landlordEntity, Contact contact);
}
