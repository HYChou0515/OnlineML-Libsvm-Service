package io.hychou.libsvm.predict.controller;

import io.hychou.common.MessageResponseEntity;
import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.data.DataEntity;
import io.hychou.entity.model.ModelEntity;
import io.hychou.libsvm.model.service.ModelService;
import io.hychou.entity.parameter.LibsvmPredictParameterEntity;
import io.hychou.libsvm.predict.service.PredictService;
import io.hychou.entity.prediction.PredictionEntity;
import io.hychou.libsvm.prediction.service.PredictionService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static io.hychou.common.Constant.SUCCESS_MESSAGE;

@RestController
public class PredictController {

    private final ModelService modelService;
    private final PredictService predictService;
    private final PredictionService predictionService;

    @Autowired
    public PredictController(
            ModelService modelService,
            PredictService predictService,
            PredictionService predictionService) {
        this.modelService = modelService;
        this.predictService = predictService;
        this.predictionService = predictionService;
    }

    @GetMapping(RequestMappingPath.SvmPredict)
    public MessageResponseEntity svmPredict(
            @PathVariable String dataName,
            @PathVariable("modelId") long modelId,
            @RequestParam(value="probabilityEstimates", required = false) Boolean probabilityEstimates
    ) {
        LibsvmPredictParameterEntity libsvmPredictParameterEntity;
        if(Objects.nonNull(probabilityEstimates))
            libsvmPredictParameterEntity = new LibsvmPredictParameterEntity(probabilityEstimates);
        else
            libsvmPredictParameterEntity = new LibsvmPredictParameterEntity();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:9010/data/"+dataName);
            HttpResponse response = client.execute( httpGet );
            InputStream body = response.getEntity().getContent();
            DataEntity dataEntity = new DataEntity();
            dataEntity.setName(dataName);
            dataEntity.setDataBytes(IOUtils.toByteArray(body));
            ModelEntity modelEntity = modelService.readModelById(modelId);
            PredictionEntity predictionEntity = predictService.svmPredict(dataEntity, modelEntity,
                    libsvmPredictParameterEntity);
            predictionEntity = predictionService.createPrediction(predictionEntity);
            return MessageResponseEntity.ok(predictionEntity.getId(), SUCCESS_MESSAGE);
        } catch (ServiceException e) {
            return e.getMessageResponseEntity();
        } catch (IOException e) {
            return null;
        }
    }
}
