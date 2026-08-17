package com.devmasters.restaurant_erp.floor.handler;

import com.devmasters.restaurant_erp.floor.domain.Floor;
import com.devmasters.restaurant_erp.floor.model.FloorModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.floor.model.searchCriteria.FloorSearchCriteria;
import com.devmasters.restaurant_erp.floor.service.FloorService;
import com.devmasters.restaurant_erp.floor.transformer.FloorTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class FloorHandler {

    private final FloorService floorService;
    private final FloorTransformer floorTransformer;

    public FloorModel create(FloorModel model) {

        if (floorService.existsByFloorNameIgnoreCaseAndBranch_Id(
                model.getFloorName(),
                model.getBranchModel().getId())) {
            throw new RuntimeException(
                    "Floor already exists with name : " + model.getFloorName()
            );
        }

        Floor entity = floorTransformer.toEntity(model);
        Floor saved = floorService.create(entity);
        return floorTransformer.toModel(saved);
    }

    public PageResponse<FloorModel> getAll(FloorSearchCriteria criteria, Pageable pageable) {
        Page<Floor> page = floorService.search(criteria, pageable);

        return PageResponse.<FloorModel>builder()
                .content(floorTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public FloorModel update(UUID id, FloorModel model) {

        Floor entity = floorTransformer.toEntity(model);
        Floor updated = floorService.update(id, entity);
        return floorTransformer.toModel(updated);
    }

    public FloorModel delete(UUID id) {
        Floor deleted = floorService.delete(id);
        return floorTransformer.toModel(deleted);
    }

    public FloorModel restore(UUID id) {
        Floor restored = floorService.restore(id);
        return floorTransformer.toModel(restored);
    }
}