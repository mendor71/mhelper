//package ru.pack.csps.controller.rest;
//
//import org.hibernate.Session;
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import ru.pack.csps.entity.Regions;
//import ru.pack.csps.service.RespEntityService;
//import ru.pack.csps.util.JSONConvertorSerivce;
//
//import java.util.List;
//
//@RestController
//public class RegionsController {
//    @Autowired
//    private SessionService sessionService;
//    @Autowired
//    private CRUDService crudService;
//    @Autowired
//    private JSONConvertorSerivce convertorSerivce;
//    @Autowired
//    private RespEntityService respEntityService;
//
//    @RequestMapping(method = RequestMethod.GET, value = "Regions")
//    public ResponseEntity get(@RequestParam(required = false, value = "regionId") Integer regionId) {
//        Session dbSession = sessionService.getSession();
//        try {
//            JSONAware respObject;
//
//            if (regionId != null) {
//                Regions regions = (Regions) crudService.findSingleResult(dbSession, "Regions.findByRegionId", "regionId", regionId);
//                respObject = regions.toJSONObject();
//            } else {
//                List regions = crudService.findResultList(dbSession, "Regions.findAll");
//                respObject = convertorSerivce.toJSON(regions);
//            }
//            return respEntityService.createRespEntity(respObject, HttpStatus.OK);
//        } finally {
//            dbSession.close();
//        }
//    }
//}
