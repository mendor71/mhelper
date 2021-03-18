package ru.pack.csps.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pack.csps.service.RestMappingApiDescriptionService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Component
public class IncludeApiInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private ConfigurableListableBeanFactory factory;
    private RestMappingApiDescriptionService dictionary;

    @Autowired
    public void setFactory(ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @Autowired
    public void setDictionary(RestMappingApiDescriptionService dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            BeanDefinition beanDefinition = factory.getBeanDefinition(name);
            String originalClassName = beanDefinition.getBeanClassName();
            try {
                if (originalClassName == null) continue;
                Class<?> aClass = Class.forName(originalClassName);

                if (aClass.isAnnotationPresent(RestController.class) && aClass.isAnnotationPresent(RequestMapping.class)) {
                    String basePath = aClass.getAnnotation(RequestMapping.class).value()[0];

                    Method[] methods = aClass.getMethods();

                    for (Method m : methods) {
                        if (m.isAnnotationPresent(IncludeAPI.class) && m.isAnnotationPresent(RequestMapping.class)) {

                            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

                            Parameter[] parameters = m.getParameters();
                            String[] parameterNames = discoverer.getParameterNames(m);
                            Class<?>[] parameterTypes = m.getParameterTypes();

                            List<ApiParameter> apiParameterList = new ArrayList<>();

                            for (int i = 0; i < parameterNames.length; i++) {

                                ApiParameter apiParameter = new ApiParameter();

                                if (parameterTypes[i].equals(Authentication.class)) {
                                    continue;
                                }

                                apiParameter.setName(parameterNames[i]);
                                apiParameter.setType(parameterTypes[i].getCanonicalName());
                                apiParameter.setPathVariable(parameters[i].isAnnotationPresent(PathVariable.class));

                                apiParameterList.add(apiParameter);
                            }

                            /*if (m.getAnnotation(IncludeAPI.class).arguments().length != 0) {
                                String[] parameters = m.getAnnotation(IncludeAPI.class).arguments();
                                for (String parameter : parameters) {
                                    if (first) {
                                        params.append(parameter);
                                        first = false;
                                    } else {
                                        params.append(",").append(parameter);
                                    }
                                }
                            }*/

                            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                            String value = requestMapping.value().length != 0 ? requestMapping.value()[0] : "";
                            String method = requestMapping.method().length != 0 ? requestMapping.method()[0].name() : "GET";

                            dictionary.addRequestMappingDescription(basePath + value, method, m.getName(), basePath.replace("/", ""), apiParameterList);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
