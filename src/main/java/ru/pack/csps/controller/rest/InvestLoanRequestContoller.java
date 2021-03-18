//package ru.pack.csps.controller.rest;
//
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import ru.pack.csps.entity.InvestLoanRequests;
//import ru.pack.csps.service.dao.InvestLoanRequestService;
//
//import java.util.List;
//
//import static ru.pack.csps.security.app.AccessResolver.isAdmin;
//
//@RestController
//@RequestMapping(value = "/investLoanRequests")
//public class InvestLoanRequestContoller {
//    private InvestLoanRequestService ilrService;
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/{ilrId}")
//    public InvestLoanRequests getInvestLoanRequestByIlrId(@PathVariable(value = "ilrId") Long ilrId, Authentication authentication) {
//        return ilrService.getInvestLoanRequestByIlrId(ilrId, authentication.getPrincipal().toString(), isAdmin(authentication));
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/loan/users/{userId}", method = RequestMethod.GET)
//    public List<InvestLoanRequests> getInvestLoanRequestsByLoanUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) {
//        return ilrService.getInvestLoanRequestsByLoanUserId(userId, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/loan/users/{userId}/states/{stateId}", method = RequestMethod.GET)
//    public List<InvestLoanRequests> getInvestLoanRequestsByLoanUserIdAndIlrStateId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "stateId") Integer stateId, Authentication authentication) {
//        return ilrService.getInvestLoanRequestsByLoanUserIdAndIlrStateId(userId, stateId, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/invest/users/{userId}", method = RequestMethod.GET)
//    public List<InvestLoanRequests> getInvestLoanRequestsByInvestUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) {
//        return ilrService.getInvestLoanRequestsByInvestUserId(userId, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/invest/users/{userId}/states/{stateId}", method = RequestMethod.GET)
//    public List<InvestLoanRequests> getInvestLoanRequestsByInvestUserIdAndIlrStateId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "stateId") Integer stateId , Authentication authentication) {
//        return ilrService.getInvestLoanRequestsByInvestUserIdAndIlrStateId(userId, stateId, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_INVESTOR')")
//    @RequestMapping(value = "/{ilrId}/solution", method = RequestMethod.POST)
//    public JSONAware setSolution(@PathVariable(value = "ilrId") Long ilrId, @RequestParam(value = "confirm") Boolean confirm, Authentication authentication) {
//        return ilrService.setSolution(ilrId, confirm, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/investRequests/{irId}/users/{userId}", method = RequestMethod.POST)
//    public JSONAware setInvestRequestRelation(@PathVariable(value = "irId") Long irId, @PathVariable(value = "userId") Long userId, Authentication authentication) {
//        return ilrService.setInvestRequestRelation(irId, userId, authentication.getPrincipal().toString());
//    }
//
//    @Autowired
//    public void setIlrService(InvestLoanRequestService ilrService) {
//        this.ilrService = ilrService;
//    }
//}
