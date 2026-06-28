package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
import com.devmasters.restaurant_erp.service.OrganizationService;
import com.devmasters.restaurant_erp.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrganizationHandler {

    private final OrganizationService organizationService;
    private final OrganizationTransformer organizationTransformer;

    public OrganizationModel create(OrganizationModel model) {

        if (organizationService.existsByEmailIgnoreCase(model.getEmail())) {
            throw new RuntimeException(
                    "Organization already exists with email : " + model.getEmail()
            );
        }

        Organization entity = organizationTransformer.toEntity(model);

        Organization saved = organizationService.create(entity);

        return organizationTransformer.toModel(saved);
    }

    public PageResponse<OrganizationModel> getAll(
            OrganizationSearchCriteria criteria,
            Pageable pageable) {

        Page<Organization> page =
                organizationService.search(criteria, pageable);

        return PageResponse.<OrganizationModel>builder()
                .content(
                        organizationTransformer.toModels(page.getContent())
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrganizationModel update(UUID id, OrganizationModel model) {

        Organization entity = organizationTransformer.toEntity(model);

        Organization updated =
                organizationService.update(id, entity);

        return organizationTransformer.toModel(updated);
    }

    public OrganizationModel delete(UUID id) {

        Organization deleted =
                organizationService.delete(id);

        return organizationTransformer.toModel(deleted);
    }

    public OrganizationModel restore(UUID id) {

        Organization restored =
                organizationService.restore(id);

        return organizationTransformer.toModel(restored);
    }
}