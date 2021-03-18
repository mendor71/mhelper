package pu.pack.csps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import ru.pack.csps.entity.*;
import ru.pack.csps.exceptions.IllegalDealAccessException;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.repository.*;
import ru.pack.csps.service.CommissionService;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.service.dao.DealsService;
import ru.pack.csps.service.dao.NotificationsService;
import ru.pack.csps.util.DATEUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealsServiceTest {
    @Mock private DealsRepository dealsRepository;
    @Mock private UsersRepository usersRepository;
    @Mock private StatesRepository statesRepository;
    @Mock private RolesRepository rolesRepository;
    @Mock private DealsBorrowerRelationsRepository dealsBorrowerRelationsRepository;
    @Mock private ClosureCodesRepository closureCodesRepository;
    @Mock private Page page;

    @Mock private SettingsService settingsService;
    @Mock private NotificationsService notificationsService;
    @Mock private CommissionService commissionService;

    private DealsService dealsService;

    private Users borrowerUser;
    private Users investorUser;
    private Users freeManUser;

    private Deals testDeal;
    private Deals testDeal_1;

    @Before
    public void initTest() throws Exception {
        freeManUser = new Users();
        freeManUser.setUserName("freeManUser");

        borrowerUser = new Users(1L);
        borrowerUser.setUserName("borrower");
        borrowerUser.getRoleList().add(new Roles(1,"ROLE_BORROWER"));

        investorUser = new Users(2L);
        investorUser.setUserName("investor");
        investorUser.getRoleList().add(new Roles(1,"ROLE_INVESTOR"));

        testDeal = new Deals(1L,10, 1000d, 1100d);
        testDeal.setDealInvestorUser(investorUser);
        testDeal.setDealRegDate(new Date());
        testDeal.setDealState(new States(1,"request.registrated"));
        testDeal.setDealDeadLine(DATEUtil.addDays(new Date(), -1));

        testDeal_1 = new Deals(2L,10, 1000d, 1100d);
        testDeal_1.setDealInvestorUser(investorUser);
        testDeal_1.setDealRegDate(new Date());
        testDeal_1.setDealState(new States(1,"request.registrated"));

        when(usersRepository.findUsersByUserName("borrower")).thenReturn(borrowerUser);
        when(usersRepository.findUsersByUserName("investor")).thenReturn(investorUser);
        when(usersRepository.findUsersByUserName("freeManUser")).thenReturn(freeManUser);

        when(rolesRepository.findRolesByRoleName("ROLE_INVESTOR")).thenReturn(new Roles(2,"ROLE_INVESTOR"));
        when(rolesRepository.findRolesByRoleName("ROLE_BORROWER")).thenReturn(new Roles(2,"ROLE_BORROWER"));

        when(statesRepository.findStatesByStateName("request.registrated")).thenReturn(new States(1,"request.registrated"));
        when(statesRepository.findStatesByStateName("request.on_confirm")).thenReturn(new States(1,"request.on_confirm"));
        when(statesRepository.findStatesByStateName("request.in_work")).thenReturn(new States(1,"request.in_work"));
        when(statesRepository.findStatesByStateName("request.closed")).thenReturn(new States(1,"request.closed"));

        when(dealsRepository.save(testDeal)).thenReturn(testDeal);
        when(dealsRepository.findByDealGivenSumGreaterThanEqualAndDealTermDaysBetweenAndDealStateStateId(isA(Double.class), isA(Integer.class), isA(Integer.class), isA(Integer.class), isA(Pageable.class))).thenReturn(page);
        when(dealsRepository.findByDealGivenSumLessThanAndDealTermDaysBetweenAndDealStateStateId(isA(Double.class), isA(Integer.class), isA(Integer.class), isA(Integer.class), isA(Pageable.class))).thenReturn(page);
        when(dealsRepository.findOne(isA(Long.class))).thenReturn(testDeal);
        when(dealsRepository.findOne(-1L)).thenReturn(null);
        when(dealsRepository.findOne(2L)).thenReturn(testDeal_1);

        when(settingsService.getProperty("app_term_range_days")).thenReturn(3);

        when(closureCodesRepository.findClosureCodesByCloCodeName("closed_by_investor")).thenReturn(new ClosureCodes(3L, "closed_by_investor", "closed_by_investor"));
        when(closureCodesRepository.findClosureCodesByCloCodeName("lawsuit")).thenReturn(new ClosureCodes(1L, "lawsuit", "lawsuit"));

        when(notificationsService.addUserNotification(isA(Users.class), isA(String.class))).thenReturn(new Notifications());
        when(page.getContent()).thenReturn(new ArrayList());

        when(dealsBorrowerRelationsRepository.findByDbrBorrowerUserAndDbrDealAndDbrState(isA(Users.class), isA(Deals.class), isA(States.class))).thenReturn(new DealsBorrowersRelations());

        dealsService = new DealsService();
        dealsService.setDealsRepository(dealsRepository);
        dealsService.setUsersRepository(usersRepository);
        dealsService.setStatesRepository(statesRepository);
        dealsService.setRolesRepository(rolesRepository);
        dealsService.setSettingsService(settingsService);
        dealsService.setNotificationsService(notificationsService);
        dealsService.setClosureCodesRepository(closureCodesRepository);
        dealsService.setDealsBorrowerRelationsRepository(dealsBorrowerRelationsRepository);
        dealsService.setCommissionService(commissionService);
    }

    @Test(expected = InvalidValueException.class)
    public void test() throws Exception {
        dealsService.getDealByDealId(-1L);
    }

    @Test(expected = IllegalDealAccessException.class)
    public void testCreateNewDeal_borrower() throws Exception {
        dealsService.createNewDeal(-1L, testDeal, "borrower");
    }

    @Test
    public void testCreateNewDeal_investor() throws Exception {
        Deals d = dealsService.createNewDeal(-1L, testDeal, "investor");
        assertNotNull(d);
        assertNotNull(d.getDealInvestorUser());
        assertNotNull(d.getDealState());
        assertNotNull(d.getDealRegDate());
        assertTrue(d.getDealInvestorUser().getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR")));

        d = dealsService.createNewDeal(testDeal, "investor");
        assertNotNull(d);
        assertNotNull(d.getDealInvestorUser());
        assertNotNull(d.getDealState());
        assertNotNull(d.getDealRegDate());
        assertTrue(d.getDealInvestorUser().getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_INVESTOR")));
    }

    @Test
    public void testFindDealsForUser() throws Exception {
        List<Deals> deals = dealsService.findDealsForBorrower(-1L, "borrower", 1000d, 1100d, 10);
        assertNotNull(deals);
    }

    @Test(expected = IllegalDealAccessException.class)
    public void testSetDealRequestByBorrower_investor() throws Exception {
        dealsService.setDealRequestFromBorrower(-1L, 1L, "investor");
    }

    @Test
    public void testSetDealRequestByBorrower_freeManUser() throws Exception {
        assertTrue(freeManUser.getRoleList().stream().noneMatch(v -> v.getRoleName().equals("ROLE_BORROWER")));
        Deals deals = dealsService.setDealRequestFromBorrower(-1L, 1L, "freeManUser");
        assertNotNull(deals);
        assertEquals(deals.getDealState().getStateName(), "request.on_confirm");
        assertTrue(deals.getDealBorrowerUser().getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER")));

        freeManUser.setRoleList(new ArrayList<>());
        deals = dealsService.setDealRequestFromBorrower(1L, "freeManUser");
        assertNotNull(deals);
        assertEquals(deals.getDealState().getStateName(), "request.on_confirm");
        assertTrue(deals.getDealBorrowerUser().getRoleList().stream().anyMatch(v -> v.getRoleName().equals("ROLE_BORROWER")));
    }

    @Test(expected = AccessDeniedException.class)
    public void testConfirmDealByInvestor_notInvestorUser() throws Exception {
        dealsService.confirmDealByInvestor(1L, "borrower");
    }

    @Test
    public void testConfirmDealByInvestor_investorUser() throws Exception {
        testDeal.setDealBorrowerUser(borrowerUser);

        Deals deals = dealsService.confirmDealByInvestor(1L, "investor");
        assertEquals(deals.getDealState().getStateName(), "request.in_work");
        assertTrue(deals.getDealInvestorConfirmed());
        assertNotNull(deals.getDealConclusionDate());
    }

    @Test(expected = AccessDeniedException.class)
    public void testRejectDealByInvestor_notInvestorUser() throws Exception {
        dealsService.rejectDealByInvestor(1L, "borrower");
    }

    @Test
    public void testRejectDealByInvestor_investorUser() throws Exception {
        testDeal.setDealBorrowerUser(borrowerUser);

        Deals deals = dealsService.rejectDealByInvestor(1L, "investor");
        assertEquals(deals.getDealState().getStateName(), "request.registrated");
        assertNull(deals.getDealBorrowerUser());
    }

    @Test(expected = AccessDeniedException.class)
    public void testCloseDealByInvestor_notInvestorUser() throws Exception {
        dealsService.closeDealByInvestor(1L, "borrower");
    }

    @Test
    public void testCloseDealByInvestor_investorUser() throws Exception {
        testDeal.setDealBorrowerUser(borrowerUser);

        Deals deals = dealsService.closeDealByInvestor(1L, "investor");
        assertNull(deals.getDealBorrowerUser());
        assertEquals(deals.getDealClosureCode().getCloCodeName(), "closed_by_investor");
        assertEquals(deals.getDealState().getStateName(), "request.closed");
    }

    @Test(expected = AccessDeniedException.class)
    public void testStartLawSuit_notInvestorUser() throws Exception {
        dealsService.startLawSuit(1L, "borrower");
    }

    @Test(expected = IllegalDealAccessException.class)
    public void testStartLawSuit_investorUser_illegalDealState() throws Exception {
        testDeal_1.setDealBorrowerUser(borrowerUser);

        Deals deals = dealsService.startLawSuit(2L, "investor");
        assertNotNull(deals.getDealBorrowerUser());
        assertEquals(deals.getDealClosureCode().getCloCodeName(), "lawsuit");
        assertEquals(deals.getDealState().getStateName(), "request.closed");
    }

    @Test
    public void testStartLawSuit_investorUser() throws Exception {
        testDeal.setDealBorrowerUser(borrowerUser);

        Deals deals = dealsService.startLawSuit(1L, "investor");
        assertNotNull(deals.getDealBorrowerUser());
        assertEquals(deals.getDealClosureCode().getCloCodeName(), "lawsuit");
        assertEquals(deals.getDealState().getStateName(), "request.closed");
    }
}
