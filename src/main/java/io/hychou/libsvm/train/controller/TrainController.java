package io.hychou.libsvm.train.controller;

import io.hychou.common.MessageResponseEntity;
import io.hychou.common.exception.service.ServiceException;
import io.hychou.entity.data.DataEntity;
import io.hychou.entity.model.ModelEntity;
import io.hychou.libsvm.model.service.ModelService;
import io.hychou.entity.parameter.KernelTypeEnum;
import io.hychou.entity.parameter.LibsvmTrainParameterEntity;
import io.hychou.entity.parameter.SvmTypeEnum;
import io.hychou.libsvm.train.service.TrainService;
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

import static io.hychou.common.Constant.SUCCESS_MESSAGE;

@RestController
public class TrainController {
    private final TrainService trainService;
    private final ModelService modelService;

    @Autowired
    public TrainController(TrainService trainService, ModelService modelService) {
        this.trainService = trainService;
        this.modelService = modelService;
    }

    @GetMapping(RequestMappingPath.SvmTrain)
    public MessageResponseEntity svmTrain(
            @PathVariable String dataName,
            @RequestParam(value = "svmType", required=false) SvmTypeEnum svmType,
            @RequestParam(value = "kernelType", required=false) KernelTypeEnum kernelType,
            @RequestParam(value = "degree", required=false) Integer degree,
            @RequestParam(value = "gamma", required=false) Double gamma,
            @RequestParam(value = "coef0", required=false) Double coef0,
            @RequestParam(value = "cacheSize", required=false) Double cacheSize,
            @RequestParam(value = "eps", required=false) Double eps,
            @RequestParam(value = "c", required=false) Double c,
            @RequestParam(value = "nu", required=false) Double nu,
            @RequestParam(value = "p", required=false) Double p,
            @RequestParam(value = "shrinking", required=false) Boolean shrinking,
            @RequestParam(value = "probability", required=false) Boolean probability
    ) {
        LibsvmTrainParameterEntity libsvmTrainParameterEntity = LibsvmTrainParameterEntity.build()
                .svmType(svmType).kernelType(kernelType).degree(degree)
                .gamma(gamma).coef0(coef0).cacheSize(cacheSize)
                .eps(eps).c(c).nu(nu).p(p).shrinking(shrinking).probability(probability).done();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:9010/data/"+dataName);
            HttpResponse response = client.execute( httpGet );
            InputStream body = response.getEntity().getContent();
            DataEntity dataEntity = new DataEntity();
            dataEntity.setName(dataName);
            dataEntity.setDataBytes(IOUtils.toByteArray(body));
            //DataEntity dataEntity = dataService.readDataByName(dataName);
            ModelEntity modelEntity = trainService.svmTrain(dataEntity, libsvmTrainParameterEntity);
            modelEntity = modelService.createModel(modelEntity);
            return MessageResponseEntity.ok(modelEntity.getId(), SUCCESS_MESSAGE);
        } catch (ServiceException e) {
            return e.getMessageResponseEntity();
        } catch (IOException e) {
            return null;
        }
    }
}
