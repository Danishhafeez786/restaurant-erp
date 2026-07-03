package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.UserSearchRequest;
import com.devmasters.restaurant_erp.model.pagination.PageInfo;
import com.devmasters.restaurant_erp.model.pagination.PageResult;
import com.devmasters.restaurant_erp.repository.UserRepository;
import com.devmasters.restaurant_erp.service.UserService;
import com.devmasters.restaurant_erp.transformer.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTransformer userTransformer;
    private final MongoTemplate mongoTemplate;

    @Override
    public User create(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public PageResult<UserModel> getAllUsers(UserSearchRequest request) {

        Sort sort = request.getSortDirection().equalsIgnoreCase("DESC")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                sort
        );

        Query query = new Query().with(pageable);

        List<Criteria> criteriaList = new ArrayList<>();

        // keyword search
        if (StringUtils.hasText(request.getKeyword())) {
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("firstName").regex(request.getKeyword(), "i"),
                    Criteria.where("lastName").regex(request.getKeyword(), "i"),
                    Criteria.where("email").regex(request.getKeyword(), "i")
            ));
        }

        // role filter
        if (request.getRole() != null) {
            criteriaList.add(Criteria.where("role").is(request.getRole()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        List<User> users = mongoTemplate.find(query, User.class);

        long total = mongoTemplate.count(query.skip(-1).limit(-1), User.class);

        List<UserModel> models = userTransformer.toModels(users);

        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / request.getPageSize()))
                .hasNext((request.getPageNumber() + 1) * request.getPageSize() < total)
                .hasPrevious(request.getPageNumber() > 0)
                .build();

        return PageResult.<UserModel>builder()
                .items(models)
                .page(pageInfo)
                .build();
    }


}
