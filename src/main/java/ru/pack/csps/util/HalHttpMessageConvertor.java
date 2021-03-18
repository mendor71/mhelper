package ru.pack.csps.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class HalHttpMessageConvertor extends AbstractJackson2HttpMessageConverter {

    protected HalHttpMessageConvertor() {
        super(new ObjectMapper(), new MediaType("application", "hal+json", DEFAULT_CHARSET));
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(new DefaultRelProvider(), null, null));
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return ResourceSupport.class.isAssignableFrom(clazz);
    }
}
