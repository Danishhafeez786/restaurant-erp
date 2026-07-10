package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.RestaurantTable;
import com.devmasters.restaurant_erp.model.RestaurantTableModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.RestaurantTableSearchCriteria;
import com.devmasters.restaurant_erp.service.RestaurantTableService;
import com.devmasters.restaurant_erp.transformer.RestaurantTableTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RestaurantTableHandler {

    private final RestaurantTableService restaurantTableService;
    private final RestaurantTableTransformer restaurantTableTransformer;

    public RestaurantTableModel create(RestaurantTableModel model) {

        if (restaurantTableService.existsByTableNumberIgnoreCaseAndBranch_Id(model.getTableNumber(),
                model.getBranchModel().getId())) {
            throw new RuntimeException("Table Number already exists : " + model.getTableNumber());
        }

        if (restaurantTableService.existsByTableNameIgnoreCaseAndBranch_Id(
                model.getTableName(),
                model.getBranchModel().getId())) {
            throw new RuntimeException("Table Name already exists : " + model.getTableName()
            );
        }

        RestaurantTable entity = restaurantTableTransformer.toEntity(model);
        RestaurantTable saved = restaurantTableService.create(entity);
        return restaurantTableTransformer.toModel(saved);
    }

    public PageResponse<RestaurantTableModel> getAll(RestaurantTableSearchCriteria criteria, Pageable pageable) {
        Page<RestaurantTable> page = restaurantTableService.search(criteria, pageable);
        return PageResponse.<RestaurantTableModel>builder()
                .content(restaurantTableTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public RestaurantTableModel update(UUID id, RestaurantTableModel model) {

        if (restaurantTableService
                .existsByTableNumberIgnoreCaseAndBranch_IdAndIdNot(
                        model.getTableNumber(),
                        model.getBranchModel().getId(),
                        id)) {
            throw new RuntimeException(
                    "Table Number already exists : "
                            + model.getTableNumber());
        }

        if (restaurantTableService
                .existsByTableNameIgnoreCaseAndBranch_IdAndIdNot(
                        model.getTableName(),
                        model.getBranchModel().getId(),
                        id)) {
            throw new RuntimeException(
                    "Table Name already exists : "
                            + model.getTableName());
        }

        RestaurantTable entity = restaurantTableTransformer.toEntity(model);
        RestaurantTable updated = restaurantTableService.update(id, entity);
        return restaurantTableTransformer.toModel(updated);
    }

    public RestaurantTableModel delete(UUID id) {

        RestaurantTable deleted = restaurantTableService.delete(id);
        return restaurantTableTransformer.toModel(deleted);
    }

    public RestaurantTableModel restore(UUID id) {

        RestaurantTable restored = restaurantTableService.restore(id);
        return restaurantTableTransformer.toModel(restored);
    }
}