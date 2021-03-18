package ru.pack.csps.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pack.csps.entity.Deals;
import ru.pack.csps.exceptions.IllegalDealAccessException;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.dao.DealsService;
import ru.pack.csps.util.IncludeAPI;

import java.util.ArrayList;
import java.util.List;

import static ru.pack.csps.security.app.AccessResolver.isAdmin;
import static ru.pack.csps.util.JSONResponse.createERRResponse;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@RestController
@RequestMapping(value = "/deals")
public class DealsContoller {
    private DealsService dealsService;

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{dealId}")
    public Deals getInvestLoanRequestByIlrId(@PathVariable(value = "dealId") Long dealId, Authentication authentication) {
        try {
            return dealsService.getDealByDealId(dealId);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return null;
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/for_user/{userId}")
    public List<Deals> findDealsForBorrower(@PathVariable Long userId
            , @RequestParam(value = "dealGivenSum") Double dealGivenSum
            , @RequestParam(value = "dealRefundSum", required = false) Double dealRefundSum
            , @RequestParam(value = "dealTermDays", required = false) Integer dealTermDays
            , Authentication authentication) throws PropertyFindException {
        try {
            return dealsService.findDealsForBorrower(userId, authentication.getPrincipal().toString(), dealGivenSum, dealRefundSum, dealTermDays);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/users/{userId}")
    public List<Deals> finDealsByCurrentUserRoleAndUserId(@PathVariable Long userId, Authentication authentication
            , @RequestParam(value = "pageId", required = false) Integer pageId
            , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            if (pageId == null || pageSize == null)
                return dealsService.getDealsByUserIdAndCurrentUserRole(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
            else
                return dealsService.getDealsByUserIdAndCurrentUserRole(userId, authentication.getPrincipal().toString(), isAdmin(authentication), pageId, pageSize);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/borrower/{userId}", method = RequestMethod.GET)
    public List<Deals> getDealsByBorrower(@PathVariable Long userId, Authentication authentication
            , @RequestParam(value = "pageId", required = false) Integer pageId
            , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {

            if (pageId == null || pageSize == null)
                return dealsService.getDealsByBorrower(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
            else
                return dealsService.getDealsByBorrower(userId, authentication.getPrincipal().toString(), isAdmin(authentication), pageId, pageSize);
        } catch (InvalidValueException e) {
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/borrower/{userId}/states/{stateId}", method = RequestMethod.GET)
    public List<Deals> getDealsByBorrowerAndState(@PathVariable Long userId, @PathVariable Integer stateId, Authentication authentication
            , @RequestParam(value = "pageId", required = false) Integer pageId
            , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            if (pageId == null || pageSize == null)
                return dealsService.getDealsByBorrowerAndState(userId, authentication.getPrincipal().toString(), stateId, isAdmin(authentication));
            else
                return dealsService.getDealsByBorrowerAndState(userId, authentication.getPrincipal().toString(), stateId, isAdmin(authentication), pageId, pageSize);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/investor/{userId}", method = RequestMethod.GET)
    public List<Deals> getDealsByInvestor(@PathVariable Long userId, Authentication authentication
            , @RequestParam(value = "pageId", required = false) Integer pageId
            , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            if (pageId == null || pageSize == null)
                return dealsService.getDealsByInvestor(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
            else
                return dealsService.getDealsByInvestor(userId, authentication.getPrincipal().toString(), isAdmin(authentication), pageId, pageSize);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/investor/{userId}/states/{stateId}", method = RequestMethod.GET)
    public List<Deals> getDealsByInvestorAndState(@PathVariable Long userId, @PathVariable Integer stateId, Authentication authentication, boolean isAdmin
            , @RequestParam(value = "pageId", required = false) Integer pageId
            , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            if (pageId == null || pageSize == null)
                return dealsService.getDealsByCurrentInvestorAndState(userId, authentication.getPrincipal().toString(), stateId, isAdmin);
            else
                return dealsService.getDealsByCurrentInvestorAndState(userId, authentication.getPrincipal().toString(), stateId, isAdmin, pageId, pageSize);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public JSONAware createDeal(@RequestBody Deals deals, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deal = dealsService.createNewDeal(deals, authentication.getPrincipal().toString());
            return createOKResponse("Сделка успешно создана. ID: " + deal.getDealId(), deal);
        } catch (InvalidValueException | IllegalArgumentException | IllegalDealAccessException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{dealId}/users/{userId}/borrower", method = RequestMethod.POST)
    public JSONAware setDealRequestFromBorrower(@PathVariable Long dealId, @PathVariable Long userId, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deals = dealsService.setDealRequestFromBorrower(userId, dealId, authentication.getPrincipal().toString());
            return createOKResponse("Сделка " + dealId + " отправлена на согласование", deals);
        } catch (InvalidValueException | IllegalArgumentException | IllegalDealAccessException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    @RequestMapping(value = "/{dealId}/confirm", method = RequestMethod.POST)
    public JSONAware confirmDeal(@PathVariable(value = "dealId") Long dealId, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deals = dealsService.confirmDealByInvestor(dealId, authentication.getPrincipal().toString());
            return createOKResponse("Сделка " + dealId + " успешно согласована", deals);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    @RequestMapping(value = "/{dealId}/reject", method = RequestMethod.POST)
    public JSONAware rejectDeal(@PathVariable(value = "dealId") Long dealId, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deals = dealsService.rejectDealByInvestor(dealId, authentication.getPrincipal().toString());
            return createOKResponse("Сделка " + dealId + " отклонена", deals);
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    @RequestMapping(value = "/{dealId}/cancel", method = RequestMethod.POST)
    public JSONAware closeDeal(@PathVariable(value = "dealId") Long dealId, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deals = dealsService.closeDealByInvestor(dealId, authentication.getPrincipal().toString());
            return createOKResponse("Сделка " + dealId + " закрыта", deals);
        } catch (InvalidValueException | IllegalDealAccessException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_INVESTOR')")
    @RequestMapping(value = "/{dealId}/lawsuit", method = RequestMethod.POST)
    public JSONAware startDealLawSuit(@PathVariable(value = "dealId") Long dealId, Authentication authentication) throws PropertyFindException, JsonProcessingException {
        try {
            Deals deals = dealsService.startLawSuit(dealId, authentication.getPrincipal().toString());
            return createOKResponse("Судебный процесс инициирован по сделке: " + dealId, deals); /*TODO doc pack send to investor*/
        } catch (InvalidValueException | IllegalDealAccessException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_BORROWER')")
    @RequestMapping(value = "/{dealId}/refund", method = RequestMethod.POST)
    public JSONAware refundDeal(@PathVariable(value = "dealId") Long dealId, Authentication authentication) throws PropertyFindException {
        try {
            Deals deals = dealsService.refundDeal(dealId, authentication.getPrincipal().toString());
            return createOKResponse("Инициирован возврат средств по сделке: " + dealId, deals);
        } catch (InvalidValueException | AccessDeniedException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @Autowired
    public void setDealsService(DealsService dealsService) {
        this.dealsService = dealsService;
    }
}
