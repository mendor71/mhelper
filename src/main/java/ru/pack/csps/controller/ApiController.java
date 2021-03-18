package ru.pack.csps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pack.csps.service.RestMappingApiDescriptionService;
import ru.pack.csps.util.RestControllerApiDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/description")
public class ApiController {
    private RestMappingApiDescriptionService descriptionService;

    @Autowired
    public void setDescriptionService(RestMappingApiDescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    @RequestMapping
    public List<RestControllerApiDescription> getApiDescription() {
        List<RestControllerApiDescription> descriptionList = new ArrayList<>();
        Map<String, RestControllerApiDescription> dictionaryMap = descriptionService.getDictionaryMap();
        dictionaryMap.forEach((k,v) -> {
            descriptionList.add(v);
        });
        return descriptionList;
    }
}
