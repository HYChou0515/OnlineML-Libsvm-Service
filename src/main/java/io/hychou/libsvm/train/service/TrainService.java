package io.hychou.libsvm.train.service;

import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.data.DataEntity;
import io.hychou.entity.model.ModelEntity;
import io.hychou.entity.parameter.LibsvmTrainParameterEntity;
import org.springframework.stereotype.Service;

@Service
public interface TrainService {
    ModelEntity svmTrain(DataEntity dataEntity, LibsvmTrainParameterEntity libsvmTrainParameterEntity) throws ServiceException;
}
