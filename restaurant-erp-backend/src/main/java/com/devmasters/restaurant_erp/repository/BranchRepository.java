package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchRepository  extends MongoRepository<Branch, UUID> {
}
