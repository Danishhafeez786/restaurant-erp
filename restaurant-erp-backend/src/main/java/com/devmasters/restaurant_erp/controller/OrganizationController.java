package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrganizationHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organization")
public class OrganizationController {
    private final OrganizationHandler organizationHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationModel>> create(@RequestBody OrganizationModel model) {

        OrganizationModel response = organizationHandler.create(model);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse
                                .<OrganizationModel>builder()
                                .success(true)
                                .message(
                                        "Organization Created Successfully"
                                )
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationModel>>> search(
            @RequestBody
            OrganizationSearchCriteria criteria,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "DESC")
            String direction) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(
                                        direction.toUpperCase()
                                ),
                                sortBy
                        )
                );

        PageResponse<OrganizationModel> response =
                organizationHandler.getAll(
                        criteria,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse
                        .<PageResponse<OrganizationModel>>
                                builder()
                        .success(true)
                        .message(
                                "Organizations fetched successfully"
                        )
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationModel>> update(
            @PathVariable UUID id,
            @RequestBody OrganizationModel model) {

        OrganizationModel response =
                organizationHandler.update(
                        id,
                        model
                );

        return ResponseEntity.ok(
                ApiResponse
                        .<OrganizationModel>builder()
                        .success(true)
                        .message(
                                "Organization Updated Successfully"
                        )
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        organizationHandler.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Organization Deleted Successfully"
                        )
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        organizationHandler.restore(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Organization Restored Successfully"
                        )
                        .build()
        );
    }

}
