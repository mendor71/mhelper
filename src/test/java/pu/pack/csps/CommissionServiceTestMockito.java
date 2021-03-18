package pu.pack.csps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.pack.csps.entity.Deals;
import ru.pack.csps.entity.Users;
import ru.pack.csps.service.CommissionService;
import ru.pack.csps.util.ICommissionPolicy;
import ru.pack.csps.util.SimpleCommissionPolicy;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CommissionServiceTestMockito {
    private ICommissionPolicy commissionPolicy = new SimpleCommissionPolicy(10d);
    private CommissionService commissionService = new CommissionService();

    @Before
    public void init() {
        commissionService.setCommissionPolicy(commissionPolicy);
    }

    @Test
    public void testSimpleCommission() {
        Deals deals = new Deals();
        deals.setDealGivenSum(1000d);

        double commissionSum = commissionService.getCommissionSum(deals, new Users());
        assertEquals(commissionSum, 100d, 0);
    }

}
