package ru.pack.csps.service;


import org.springframework.stereotype.Component;
import ru.pack.csps.util.ApiParameter;
import ru.pack.csps.util.RestControllerApiDescription;
import ru.pack.csps.util.RestMethodApiDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestMappingApiDescriptionService {

    private Map<String, RestControllerApiDescription> dictionaryMap = new HashMap<>();

    public void addRequestMappingDescription(String url, String method, String actionName, String modelName, List<ApiParameter> params){
        RestMethodApiDescription description = new RestMethodApiDescription(url, method, actionName, modelName, params);
        if (dictionaryMap.containsKey(description.getModelName())) {
            dictionaryMap.get(description.getModelName()).getMethodList().add(description);
        } else {
            List<RestMethodApiDescription> descriptions = new ArrayList<>();
            descriptions.add(description);
            dictionaryMap.put(description.getModelName(), new RestControllerApiDescription(description.getModelName(), descriptions));
        }
    }

    public Map<String, RestControllerApiDescription> getDictionaryMap() {
        return dictionaryMap;
    }
}
