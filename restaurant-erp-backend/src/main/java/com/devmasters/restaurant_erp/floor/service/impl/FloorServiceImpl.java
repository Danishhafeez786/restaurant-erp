package com.devmasters.restaurant_erp.floor.service.impl;

import com.devmasters.restaurant_erp.floor.domain.Floor;
import com.devmasters.restaurant_erp.floor.model.searchCriteria.FloorSearchCriteria;
import com.devmasters.restaurant_erp.floor.respository.FloorRepository;
import com.devmasters.restaurant_erp.floor.service.FloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;

    @Override
    public boolean existsByFloorNameIgnoreCaseAndBranch_Id(String floorName, UUID branchId) {
        return floorRepository.existsByFloorNameIgnoreCaseAndBranch_Id(
                        floorName,
                        branchId
                );
    }

    @Override
    public Floor create(Floor entity) {
        return floorRepository.save(entity);
    }

    @Override
    public Page<Floor> search(FloorSearchCriteria criteria, Pageable pageable) {
        return floorRepository.search(criteria, pageable);
    }

    @Override
    public Floor findById(UUID id) {
        return floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found."));
    }

    @Override
    public Floor update(UUID id, Floor entity) {

        Floor existing = findById(id);

        existing.setFloorName(entity.getFloorName());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setDescription(entity.getDescription());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());
        return floorRepository.save(existing);
    }

    @Override
    public Floor delete(UUID id) {
        Floor floor = findById(id);

        if (!Boolean.TRUE.equals(floor.getIsActive())) {
            throw new RuntimeException("Floor already deleted.");
        }
        floor.setIsActive(false);
        return floorRepository.save(floor);
    }

    @Override
    public Floor restore(UUID id) {
        Floor floor = findById(id);
        floor.setIsActive(true);
        return floorRepository.save(floor);
    }
}
