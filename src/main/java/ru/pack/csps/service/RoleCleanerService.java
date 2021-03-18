package ru.pack.csps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Deals;
import ru.pack.csps.repository.DealsRepository;
import ru.pack.csps.repository.StatesRepository;
import ru.pack.csps.service.dao.RolesService;

import java.util.List;

@Service
public class RoleCleanerService {
    private RolesService rolesService;
    private DealsRepository dealsRepository;
    private StatesRepository statesRepository;

    public void cleanRoles(Deals deals) {
        List<Deals> loanReq  = dealsRepository.findByDealInvestorUserUserId(deals.getDealInvestorUser().getUserId());
        List<Deals> investReq  = dealsRepository.findByDealBorrowerUserUserId(deals.getDealBorrowerUser().getUserId());

        if (loanReq.stream()
                .filter(v -> v.getDealState().equals(statesRepository.findStatesByStateName("request.closed")))
                .count() == loanReq.size())
            rolesService.removeUserRole(deals.getDealBorrowerUser(), "ROLE_BORROWER");

        if (investReq.stream()
                .filter(v -> v.getDealState().equals(statesRepository.findStatesByStateName("request.closed")))
                .count() == investReq.size())
            rolesService.removeUserRole(deals.getDealInvestorUser(), "ROLE_INVESTOR");
    }

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }
    @Autowired
    public void setDealsRepository(DealsRepository dealsRepository) {
        this.dealsRepository = dealsRepository;
    }
    @Autowired
    public void setStatesRepository(StatesRepository statesRepository) {
        this.statesRepository = statesRepository;
    }
}
