package io.hychou.libsvm.predict.service;

import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.data.DataEntity;
import io.hychou.entity.model.ModelEntity;
import io.hychou.entity.parameter.LibsvmPredictParameterEntity;
import io.hychou.entity.prediction.PredictionEntity;

public interface PredictService {
    PredictionEntity svmPredict(DataEntity dataEntity, ModelEntity modelEntity,
                                LibsvmPredictParameterEntity libsvmPredictParameterEntity) throws ServiceException;
}
