package com.devmasters.restaurant_erp.common.transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic transformer for converting between domain entities and models
 *
 * @param <E> Entity (Domain) type
 * @param <M> Model type (single unified model for both request and response)
 */
public abstract class Transformer<E, M> {

    /**
     * Convert model to entity (domain)
     * Used when receiving data from frontend
     * @param model Model object from frontend
     * @return Entity object for persistence
     */
    public abstract E toEntity(M model);

    /**
     * Convert entity (domain) to model
     * Used when sending data to frontend
     * @param entity Entity object from database
     * @return Model object for frontend
     */
    public abstract M toModel(E entity);

    /**
     * Convert list of models to entities
     * @param models List of model objects
     * @return List of entity objects
     */
    public List<E> toEntities(List<M> models) {
        return models != null ? models.stream().map(this::toEntity).toList() : new ArrayList<>();
    }

    /**
     * Convert list of entities to models
     * @param entities List of entity objects
     * @return List of model objects
     */
    public List<M> toModels(List<E> entities) {
        return entities != null ? entities.stream().map(this::toModel).toList() : new ArrayList<>();
    }
}
