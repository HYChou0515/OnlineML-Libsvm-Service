package io.hychou.libsvm.prediction.dao;

import io.hychou.entity.prediction.PredictionEntity;
import org.springframework.data.repository.CrudRepository;

public interface PredictionEntityRepository extends CrudRepository<PredictionEntity, Long> {
}
