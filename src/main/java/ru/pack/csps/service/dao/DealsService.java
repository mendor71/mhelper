package ru.pack.csps.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.*;
import ru.pack.csps.exceptions.IllegalDealAccessException;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.*;
import ru.pack.csps.service.CommissionService;
import ru.pack.csps.service.RoleCleanerService;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.util.DATEUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DealsService {
    private DealsRepository dealsRepository;
    private UsersRepository usersRepository;
    private StatesRepository statesRepository;
    private RolesRepository rolesRepository;
    private ClosureCodesRepository closureCodesRepository;
    private SettingsService settingsService;
    private NotificationsService notificationsService;
    private UsersService usersService;
    private CommissionService commissionService;
    private DealsBorrowerRelationsRepository dealsBorrowerRelationsRepository;
    private RoleCleanerService roleCleanerService;

    public Deals getDealByDealId(Long dealId) throws InvalidValueException {
        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        return deals;
    }

    public List<Deals> getDealsByUserIdAndCurrentUserRole(Long userId, String currentUserName, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (users == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!users.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        Roles roles = usersService.getCurrentUserDealsRole(users);

        switch (roles.getRoleName()) {
            case "ROLE_BORROWER":
                return dealsRepository.findByDealBorrowerUserUserId(users.getUserId());
            case "ROLE_INVESTOR":
                return dealsRepository.findByDealInvestorUserUserId(users.getUserId());
            default:
                return new ArrayList<>();
        }
    }

    public List<Deals> getDealsByUserIdAndCurrentUserRole(Long userId, String currentUserName, boolean isAdmin, int pageId, int pageSize) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (users == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!users.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        Roles roles = usersService.getCurrentUserDealsRole(users);

        PageRequest pageRequest = new PageRequest(pageId, pageSize, Sort.Direction.DESC, "dealId");

        switch (roles.getRoleName()) {
            case "ROLE_BORROWER":
                return dealsRepository.findByDealBorrowerUserUserId(users.getUserId(), pageRequest).getContent();
            case "ROLE_INVESTOR":
                return dealsRepository.findByDealInvestorUserUserId(users.getUserId(), pageRequest).getContent();
            default:
                return new ArrayList<>();
        }
    }

    public List<Deals> getDealsByInvestor(Long userId, String currentUserName, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users investorUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (investorUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!investorUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        return dealsRepository.findByDealInvestorUserUserId(investorUser.getUserId());
    }

    public List<Deals> getDealsByInvestor(Long userId, String currentUserName, boolean isAdmin, int pageId, int pageSize) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users investorUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (investorUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!investorUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        PageRequest pageRequest = new PageRequest(pageId, pageSize, Sort.Direction.DESC, "dealId");
        return dealsRepository.findByDealInvestorUserUserId(investorUser.getUserId(), pageRequest).getContent();
    }

    public List<Deals> getDealsByCurrentInvestorAndState(Long userId, String currentUserName, Integer stateId, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users investorUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (investorUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!investorUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        return dealsRepository.findByDealInvestorUserUserIdAndDealStateStateId(investorUser.getUserId(), stateId);
    }

    public List<Deals> getDealsByCurrentInvestorAndState(Long userId, String currentUserName, Integer stateId, boolean isAdmin, int pageId, int pageSize) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users investorUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (investorUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!investorUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        PageRequest pageRequest = new PageRequest(pageId, pageSize, Sort.Direction.DESC, "dealId");
        return dealsRepository.findByDealInvestorUserUserIdAndDealStateStateId(investorUser.getUserId(), stateId, pageRequest).getContent();
    }

    public List<Deals> getDealsByBorrower(Long userId, String currentUserName, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (borrowerUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!borrowerUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        return dealsRepository.findByDealBorrowerUserUserId(borrowerUser.getUserId());
    }

    public List<Deals> getDealsByBorrower(Long userId, String currentUserName, boolean isAdmin, int pageId, int pageSize) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (borrowerUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!borrowerUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        PageRequest pageRequest = new PageRequest(pageId, pageSize, Sort.Direction.DESC, "dealId");
        return dealsRepository.findByDealBorrowerUserUserId(borrowerUser.getUserId(), pageRequest).getContent();
    }

    public List<Deals> getDealsByBorrowerAndState(Long userId, String currentUserName, Integer stateId, boolean isAdmin) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (borrowerUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!borrowerUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        return dealsRepository.findByDealBorrowerUserUserIdAndDealStateStateId(borrowerUser.getUserId(), stateId);
    }

    public List<Deals> getDealsByBorrowerAndState(Long userId, String currentUserName, Integer stateId, boolean isAdmin, int pageId, int pageSize) throws InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (borrowerUser == null) { throw new InvalidValueException("Пользователь не найден по ID: " + userId); }
        if (!borrowerUser.equals(currentUser) && !isAdmin) { throw new AccessDeniedException("Недостаточно привилегий"); }

        PageRequest pageRequest = new PageRequest(pageId, pageSize, Sort.Direction.DESC, "dealId");
        return dealsRepository.findByDealBorrowerUserUserIdAndDealStateStateId(borrowerUser.getUserId(), stateId, pageRequest).getContent();
    }

    private Deals createNewDeal(Deals deals, Users investorUser) {
        deals.setDealInvestorUser(investorUser);
        deals.setDealState(statesRepository.findStatesByStateName("request.registrated")); //TODO
        deals.setDealRegDate(new Date());

        deals = dealsRepository.save(deals);

        if (investorUser.getRoleList().stream().noneMatch(v -> v.getRoleName().equals("ROLE_INVESTOR")))
            investorUser.getRoleList().add(rolesRepository.findRolesByRoleName("ROLE_INVESTOR"));

        usersRepository.save(investorUser);
        return deals;
    }

    public Deals createNewDeal(Deals deals, String currentUserName) throws InvalidValueException, IllegalDealAccessException {
        Users investorUser = usersRepository.findUsersByUserName(currentUserName);
        if (investorUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        if (investorUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER"))) {
            throw new IllegalDealAccessException("Перед созданием заявки на инвестирования нужно погасить все активные заемы");
        }
        return createNewDeal(deals, investorUser);
    }

    public Deals createNewDeal(Long investorUserId, Deals deals, String currentUserName) throws InvalidValueException, IllegalDealAccessException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }
        Users investorUser = investorUserId == -1 ? currentUser : usersRepository.findOne(investorUserId);

        if (investorUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER"))) {
            throw new IllegalDealAccessException("Перед созданием заявки на инвестирования нужно погасить все активные заемы");
        }

        if (!currentUser.equals(investorUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }
        return createNewDeal(deals, investorUser);
    }

    public List<Deals> findDealsForBorrower(Long borrowerUserId, String currentUserName, Double dealGivenSum, Double dealRefundSum, Integer dealTerm) throws InvalidValueException, PropertyFindException {
        if (dealGivenSum == null) {dealGivenSum = 0d;}
        if (dealRefundSum == null) {dealRefundSum = 0d;}
        if (dealTerm == null) { dealTerm = 0;}

        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = borrowerUserId == -1 ? currentUser : usersRepository.findOne(borrowerUserId);

        //TODO add user rate and age criteria //

        Integer termRangeDays = (Integer) settingsService.getProperty("app_term_range_days"); //TODO
        States defaultState = statesRepository.findStatesByStateName("request.registrated"); //TODO

        /*PageRequest pageRequest = new PageRequest( 0, 5, Sort.Direction.DESC, "dealId");

        List<Deals> h_deals = dealsRepository.findByDealGivenSumGreaterThanEqualAndDealTermDaysBetweenAndDealStateStateId(dealGivenSum, dealTerm - appTermRange, dealTerm + appTermRange, defaultState.getStateId(), pageRequest).getContent();
        List<Deals> l_deals = dealsRepository.findByDealGivenSumLessThanAndDealTermDaysBetweenAndDealStateStateId(dealGivenSum, dealTerm - appTermRange, dealTerm + appTermRange, defaultState.getStateId(), pageRequest).getContent();

        List<Deals> retDeals = new ArrayList<>();
        retDeals.addAll(h_deals);
        retDeals.addAll(l_deals);*/

        List<Deals> h_deals = dealsRepository.findDealsForUser_H(dealTerm, termRangeDays, dealGivenSum, defaultState.getStateId(), statesRepository.findStatesByStateName("request.on_confirm").getStateId());
        List<Deals> l_deals = dealsRepository.findDealsForUser_L(dealTerm, termRangeDays, dealGivenSum, defaultState.getStateId(), statesRepository.findStatesByStateName("request.on_confirm").getStateId());

        h_deals.addAll(l_deals);
        return h_deals;
    }

    private Deals setDealRequestFromBorrower(Deals deals, Users borrowerUser) throws PropertyFindException {
        States onConfirmState = statesRepository.findStatesByStateName("request.on_confirm"); //TODO

        DealsBorrowersRelations dbr = new DealsBorrowersRelations();
        dbr.setDbrBorrowerUser(borrowerUser);
        dbr.setDbrDeal(deals);
        dbr.setDbrState(onConfirmState);
        dbr.setDbrBorrowerCommission(commissionService.getCommissionSum(deals, borrowerUser));
        dealsBorrowerRelationsRepository.save(dbr);

        deals.setDealState(onConfirmState);
        deals.setDealBorrowerUser(borrowerUser);

        deals = dealsRepository.save(deals);

        if (borrowerUser.getRoleList().stream().noneMatch(v -> v.getRoleName().equals("ROLE_BORROWER")))
            borrowerUser.getRoleList().add(rolesRepository.findRolesByRoleName("ROLE_BORROWER"));

        usersRepository.save(borrowerUser);

        notificationsService.addUserNotification(deals.getDealInvestorUser(), "Запрошено согласование по Вашей заявке на инвестирование " + deals.getDealId());
        return deals;
    }

    public Deals setDealRequestFromBorrower(Long dealId, String currentUserName) throws InvalidValueException, IllegalDealAccessException, PropertyFindException {
        Users borrowerUser = usersRepository.findUsersByUserName(currentUserName);
        if (borrowerUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        if (borrowerUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR"))) {
            throw new IllegalDealAccessException("Перед созданием заявки на займ нужно закрыть все активные заявки на инвестирование");
        }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }

        return setDealRequestFromBorrower(deals, borrowerUser);
    }

    public Deals setDealRequestFromBorrower(Long borrowerUserId, Long dealId, String currentUserName) throws InvalidValueException, IllegalDealAccessException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Users borrowerUser = borrowerUserId == -1 ? currentUser : usersRepository.findOne(borrowerUserId);
        if (!currentUser.equals(borrowerUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (borrowerUser.getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR"))) {
            throw new IllegalDealAccessException("Перед созданием заявки на займ нужно закрыть все активные заявки на инвестирование");
        }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }

        return setDealRequestFromBorrower(deals, borrowerUser);
    }

    public Deals confirmDealByInvestor(Long dealId, String currentUserName) throws InvalidValueException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        if (!deals.getDealInvestorUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        States inWorkState = statesRepository.findStatesByStateName("request.in_work");

        DealsBorrowersRelations dbr = dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(deals.getDealBorrowerUser(), deals, statesRepository.findStatesByStateName("request.on_confirm"));
        dbr.setDbrState(inWorkState);
        dealsBorrowerRelationsRepository.save(dbr);

        deals.setDealState(inWorkState);
        deals.setDealInvestorConfirmed(true);
        deals.setDealConclusionDate(new Date());
        deals.setDealDeadLine(DATEUtil.addDays(new Date(), deals.getDealTermDays()));
        deals.setDealCommission(dbr.getDbrBorrowerCommission());

        deals = dealsRepository.save(deals);

        notificationsService.addUserNotification(deals.getDealBorrowerUser(), "Сделка " + dealId + " успешно заключена"); //TODO message text
        return deals;
    }

    public Deals rejectDealByInvestor(Long dealId, String currentUserName) throws InvalidValueException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        if (!deals.getDealInvestorUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        Users borrowerUsers = deals.getDealBorrowerUser();

        DealsBorrowersRelations dbr = dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(deals.getDealBorrowerUser(), deals, statesRepository.findStatesByStateName("request.on_confirm"));
        dbr.setDbrState(statesRepository.findStatesByStateName("request.closed"));
        dealsBorrowerRelationsRepository.save(dbr);

        deals.setDealState(statesRepository.findStatesByStateName("request.registrated"));
        deals.setDealBorrowerUser(null);

        deals = dealsRepository.save(deals);
        notificationsService.addUserNotification(borrowerUsers, "Сделка " + dealId + " отклонена инвестором");

        roleCleanerService.cleanRoles(deals);
        return deals;
    }

    public Deals closeDealByInvestor(Long dealId, String currentUserName) throws InvalidValueException, IllegalDealAccessException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        if (!deals.getDealInvestorUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (!deals.getDealState().getStateName().equals("request.registrated") && !deals.getDealState().getStateName().equals("request.on_confirm")) {
            throw new IllegalDealAccessException("Закрытие сделки из статуса "
                    + deals.getDealState().getStateNameLocale()
                    + " невозможно. Дождитесь возврата средств либо начните судебный процесс, если срок истек");
        }

        Users borrowerUsers = deals.getDealBorrowerUser();

        DealsBorrowersRelations dbr = dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(deals.getDealBorrowerUser(), deals, statesRepository.findStatesByStateName("request.on_confirm"));
        if (dbr != null) {
            dbr.setDbrState(statesRepository.findStatesByStateName("request.closed"));
            dealsBorrowerRelationsRepository.save(dbr);
        }

        deals.setDealClosureCode(closureCodesRepository.findClosureCodesByCloCodeName("closed_by_investor"));
        deals.setDealState(statesRepository.findStatesByStateName("request.closed"));
        deals.setDealBorrowerUser(null);

        deals = dealsRepository.save(deals);
        notificationsService.addUserNotification(borrowerUsers, "Сделка " + dealId + " закрыта инвестором");
        roleCleanerService.cleanRoles(deals);
        return deals;
    }

    public Deals startLawSuit(Long dealId, String currentUserName) throws InvalidValueException, IllegalDealAccessException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        if (!deals.getDealInvestorUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        if (deals.getDealDeadLine() == null || new Date().before(deals.getDealDeadLine())) {
            throw new IllegalDealAccessException("Начало судебного процесса возможно только после истечения срока возврата средств. " + (deals.getDealDeadLine() != null ? dateFormat.format(deals.getDealDeadLine()) : "Сделка еще не заключена."));
        }

        States closedState = statesRepository.findStatesByStateName("request.closed");

        DealsBorrowersRelations dbr = dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(deals.getDealBorrowerUser(), deals, statesRepository.findStatesByStateName("request.in_work"));
        dbr.setDbrState(closedState);
        dealsBorrowerRelationsRepository.save(dbr);

        Users borrowerUsers = deals.getDealBorrowerUser();
        deals.setDealClosureCode(closureCodesRepository.findClosureCodesByCloCodeName("lawsuit"));
        deals.setDealState(closedState);

        deals = dealsRepository.save(deals);
        notificationsService.addUserNotification(borrowerUsers, "Инвестором инициирован судебный процесс по сделке: " + deals.getDealId());
        roleCleanerService.cleanRoles(deals);
        return deals;
    }

    public Deals refundDeal(Long dealId, String currentUserName) throws InvalidValueException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значание параметра currentUserName"); }

        Deals deals = dealsRepository.findOne(dealId);
        if (deals == null) { throw new InvalidValueException("Сделка не найдена по ID: " + dealId); }
        if (!deals.getDealBorrowerUser().equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        if (!deals.getDealState().equals(statesRepository.findStatesByStateName("request.in_work"))) {
            throw new IllegalStateException("Некорректное состояние сделки! Невозможно инициировать возврат сделки в статусе: " + deals.getDealState().getStateNameLocale());
        }

        deals.setDealState(statesRepository.findStatesByStateName("request.on_refund"));

        DealsBorrowersRelations dbr = dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(deals.getDealBorrowerUser(), deals, statesRepository.findStatesByStateName("request.in_work"));
        if (dbr != null) {
            dbr.setDbrState(statesRepository.findStatesByStateName("request.on_refund"));
            dealsBorrowerRelationsRepository.save(dbr);
        }

        deals = dealsRepository.save(deals);
        notificationsService.addUserNotification(deals.getDealInvestorUser(), "Заемщиком инициирован возврат средств по сделке: " + dealId);
        roleCleanerService.cleanRoles(deals);
        return deals;
    }

    @Autowired
    public void setDealsRepository(DealsRepository dealsRepository) {
        this.dealsRepository = dealsRepository;
    }
    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Autowired
    public void setStatesRepository(StatesRepository statesRepository) {
        this.statesRepository = statesRepository;
    }
    @Autowired
    public void setClosureCodesRepository(ClosureCodesRepository closureCodesRepository) {
        this.closureCodesRepository = closureCodesRepository;
    }
    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    @Autowired
    public void setNotificationsService(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }
    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
    @Autowired
    public void setCommissionService(CommissionService commissionService) {
        this.commissionService = commissionService;
    }
    @Autowired
    public void setDealsBorrowerRelationsRepository(DealsBorrowerRelationsRepository dealsBorrowerRelationsRepository) {
        this.dealsBorrowerRelationsRepository = dealsBorrowerRelationsRepository;
    }
    @Autowired
    public void setRoleCleanerService(RoleCleanerService roleCleanerService) {
        this.roleCleanerService = roleCleanerService;
    }
}
