package ru.pack.csps.service;

import org.json.simple.JSONAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RespEntityService {

    public ResponseEntity createRespEntity(JSONAware jsonAware, HttpStatus state) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain; charset=UTF-8");
        return new ResponseEntity(jsonAware.toJSONString(), headers, state);
    }
}
