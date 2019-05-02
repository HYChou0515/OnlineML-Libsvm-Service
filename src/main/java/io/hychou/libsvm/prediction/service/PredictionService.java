package io.hychou.libsvm.prediction.service;

import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.prediction.PredictionEntity;

public interface PredictionService {

    PredictionEntity readPredictionById(Long id) throws ServiceException;

    PredictionEntity createPrediction(PredictionEntity predictionEntity) throws ServiceException;

    PredictionEntity updatePrediction(PredictionEntity predictionEntity) throws ServiceException;

    void deletePredictionById(Long id) throws ServiceException;
}
