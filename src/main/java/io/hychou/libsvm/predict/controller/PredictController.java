package io.hychou.libsvm.predict.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.hychou.common.MessageResponseEntity;
import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.data.DataEntity;
import io.hychou.entity.model.ModelEntity;
import io.hychou.entity.parameter.LibsvmPredictParameterEntity;
import io.hychou.entity.prediction.PredictionEntity;
import io.hychou.libsvm.model.service.ModelService;
import io.hychou.libsvm.predict.service.PredictService;
import io.hychou.libsvm.prediction.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static io.hychou.common.Constant.SUCCESS_MESSAGE;

@RestController
public class PredictController {

    private final ModelService modelService;
    private final PredictService predictService;
    private final PredictionService predictionService;
    private final RestTemplate restTemplate;
    private final EurekaClient discoveryClient;

    @Autowired
    public PredictController(
            ModelService modelService,
            PredictService predictService,
            PredictionService predictionService,
            RestTemplate restTemplate,
            EurekaClient discoveryClient) {
        this.modelService = modelService;
        this.predictService = predictService;
        this.predictionService = predictionService;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping(RequestMappingPath.SvmPredict)
    public MessageResponseEntity svmPredict(
            @PathVariable String dataName,
            @PathVariable("modelId") long modelId,
            @RequestParam(value = "probabilityEstimates", required = false) Boolean probabilityEstimates
    ) {
        LibsvmPredictParameterEntity libsvmPredictParameterEntity;
        if (Objects.nonNull(probabilityEstimates))
            libsvmPredictParameterEntity = new LibsvmPredictParameterEntity(probabilityEstimates);
        else
            libsvmPredictParameterEntity = new LibsvmPredictParameterEntity();
        try {
            final InstanceInfo instance = discoveryClient.getNextServerFromEureka(
                    "dataservice", false);
            final byte[] dataBytes = restTemplate.getForObject(
                    instance.getHomePageUrl() + "/data/" + dataName, byte[].class);
            DataEntity dataEntity = new DataEntity();
            dataEntity.setName(dataName);
            dataEntity.setDataBytes(dataBytes);

            ModelEntity modelEntity = modelService.readModelById(modelId);
            PredictionEntity predictionEntity = predictService.svmPredict(dataEntity, modelEntity,
                    libsvmPredictParameterEntity);
            predictionEntity = predictionService.createPrediction(predictionEntity);
            return MessageResponseEntity.ok(predictionEntity.getId(), SUCCESS_MESSAGE);
        } catch (ServiceException e) {
            return e.getMessageResponseEntity();
        }
    }
}
