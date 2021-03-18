//package ru.pack.csps.util;
//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//import ru.pack.csps.service.RoleCleanerService;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class EnableRoleCleanerHandlerBeanPostProcessor implements BeanPostProcessor {
//    private Map<String, Class> beanClassMap = new HashMap<>();
//    private RoleCleanerService roleCleanerService;
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        Class beanClass = bean.getClass();
//
//        for (Method method : beanClass.getMethods()) {
//            if (method.isAnnotationPresent(EnableRoleCleaner.class)) {
//                beanClassMap.put(beanName, beanClass);
//            }
//        }
//
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
//        Class beanClass = beanClassMap.get(beanName);
//
//        if (beanClass != null) {
//             return Enhancer.create(beanClass, new MethodInterceptor() {
//                @Override
//                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                    if (method.isAnnotationPresent(EnableRoleCleaner.class)) {
//                        roleCleanerService.clean();
//                    }
//                    return methodProxy.invoke(bean, objects);
//                }
//            });
//        }
//        return bean;
//    }
//
//    @Autowired
//    public void setRoleCleanerService(RoleCleanerService roleCleanerService) {
//        this.roleCleanerService = roleCleanerService;
//    }
//}
