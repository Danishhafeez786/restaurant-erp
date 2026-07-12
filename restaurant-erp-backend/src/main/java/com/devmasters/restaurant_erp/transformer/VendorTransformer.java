package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Vendor;
import com.devmasters.restaurant_erp.model.VendorModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VendorTransformer extends Transformer<Vendor, VendorModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public Vendor toEntity(VendorModel model) {

        if (model == null)
            return null;

        return Vendor.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .vendorName(model.getVendorName())
                .vendorCode(model.getVendorCode())
                .contactPerson(model.getContactPerson())
                .phone(model.getPhone())
                .alternatePhone(model.getAlternatePhone())
                .email(model.getEmail())
                .website(model.getWebsite())
                .taxNumber(model.getTaxNumber())
                .registrationNumber(model.getRegistrationNumber())
                .address(model.getAddress())
                .city(model.getCity())
                .state(model.getState())
                .country(model.getCountry())
                .zipCode(model.getZipCode())
                .notes(model.getNotes())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public VendorModel toModel(Vendor entity) {

        if (entity == null)
            return null;

        return VendorModel.builder()
                .id(entity.getId())
                .vendorName(entity.getVendorName())
                .vendorCode(entity.getVendorCode())
                .contactPerson(entity.getContactPerson())
                .phone(entity.getPhone())
                .alternatePhone(entity.getAlternatePhone())
                .email(entity.getEmail())
                .website(entity.getWebsite())
                .taxNumber(entity.getTaxNumber())
                .registrationNumber(entity.getRegistrationNumber())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .zipCode(entity.getZipCode())
                .notes(entity.getNotes())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}