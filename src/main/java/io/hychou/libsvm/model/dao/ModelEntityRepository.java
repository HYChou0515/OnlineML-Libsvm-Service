package io.hychou.libsvm.model.dao;

import io.hychou.entity.model.ModelEntity;
import org.springframework.data.repository.CrudRepository;

public interface ModelEntityRepository extends CrudRepository<ModelEntity, Long> {
}
