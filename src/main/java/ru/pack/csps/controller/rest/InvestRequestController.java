//package ru.pack.csps.controller.rest;
//
//import org.json.simple.JSONAware;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import ru.pack.csps.entity.InvestRequests;
//import ru.pack.csps.exceptions.PropertyFindException;
//import ru.pack.csps.service.dao.InvestRequestService;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.List;
//
//import static ru.pack.csps.security.app.AccessResolver.isAdmin;
//
///**
// * Created by Mendor on 24.10.2017.
// */
//
//@RestController
//@RequestMapping("/investRequests")
//public class InvestRequestController {
//    private InvestRequestService irService;
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.GET, value = "/{irId}")
//    public InvestRequests findById(@PathVariable(value = "irId") Long irId, Authentication authentication) {
//        return irService.findById(irId, authentication.getPrincipal().toString(), isAdmin(authentication));
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
//    public List<InvestRequests> findByUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) {
//        return irService.findByUserId(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/states/{stateId}")
//    public List<InvestRequests> findByUserIdAndStateId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "stateId") Integer stateId, Authentication authentication) {
//        return irService.findByUserIdAndStateId(userId, stateId, authentication.getPrincipal().toString(), isAdmin(authentication));
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/for")
//    public List<InvestRequests> selectInvestRequestForUser(@PathVariable(value = "userId") Long userId
//            , @RequestParam(value = "loanTerm") Integer loanTerm
//            , @RequestParam(value = "loanAmount") Double loanAmount
//            , @RequestParam(value = "refundAmount") Double refundAmount
//            , Authentication authentication) throws ParseException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
//        return irService.selectInvestRequestForUser(userId, loanTerm, loanAmount, refundAmount, authentication.getPrincipal().toString());
//    }
//
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(method = RequestMethod.POST, value = "/users/{userId}")
//    public JSONAware addInvestRequest(@PathVariable(value = "userId") Long userId, @RequestBody InvestRequests investRequests, Authentication authentication) {
//        return irService.addInvestRequest(userId, investRequests, authentication.getPrincipal().toString());
//    }
//
//    @Autowired
//    public void setIrService(InvestRequestService irService) {
//        this.irService = irService;
//    }
//}