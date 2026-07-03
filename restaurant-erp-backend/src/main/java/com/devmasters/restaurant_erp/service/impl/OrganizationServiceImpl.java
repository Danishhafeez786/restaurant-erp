package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrganizationRepository;
import com.devmasters.restaurant_erp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return organizationRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByOrganizationNameIgnoreCase(String organizationName) {
        return organizationRepository.existsByOrganizationNameIgnoreCase(organizationName);
    }

    @Override
    public Organization create(Organization entity) {
        return organizationRepository.save(entity);
    }

    @Override
    public Page<Organization> search(OrganizationSearchCriteria criteria, Pageable pageable) {
        return organizationRepository.search(criteria, pageable);
    }

    @Override
    public Organization findById(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
    }

    @Override
    public Organization update(UUID id, Organization entity) {

        Organization existing = findById(id);

        existing.setOrganizationName(entity.getOrganizationName()
        );
        existing.setLogoUrl(entity.getLogoUrl());
        existing.setOwnerName(entity.getOwnerName());
        existing.setContactNumber(entity.getContactNumber());
        existing.setEmail(entity.getEmail());
        existing.setAddress(entity.getAddress());
        existing.setCity(entity.getCity());
        existing.setCountry(entity.getCountry());
        existing.setSubscriptionPlan(entity.getSubscriptionPlan());
        existing.setBillingCycle(entity.getBillingCycle());
        existing.setSubscriptionStartDate(entity.getSubscriptionStartDate());
        existing.setSubscriptionEndDate(entity.getSubscriptionEndDate());
        existing.setIsActive(entity.getIsActive());

        return organizationRepository.save(existing);
    }

    @Override
    public Organization delete(UUID id) {
        Organization organization = findById(id);

        if (!Boolean.TRUE.equals(organization.getIsActive())) {
            throw new RuntimeException("Organization already deleted.");}
        organization.setIsActive(false);
        return organizationRepository.save(organization);
    }

    @Override
    public Organization restore(UUID id) {

        Organization organization = findById(id);
        organization.setIsActive(true);
        return organizationRepository.save(organization);
    }
}
