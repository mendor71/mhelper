//package pu.pack.csps;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import ru.pack.csps.entity.InvestRequests;
//import ru.pack.csps.repository.InvestRequestsRepository;
//
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
//public class InvestRequestRepositoryTest {
//
//    @Autowired
//    private InvestRequestsRepository irRepository;
//
//    @Ignore
//    @Test
//    public void testFindAll() {
//        Iterable<InvestRequests> all = irRepository.findAll();
//
//        for (InvestRequests i: all) {
//            System.out.println(i);
//        }
//    }
//
//    @Ignore
//    @Test
//    public void testFindForMe() {
//        List<InvestRequests> investRequestsForUser_h = irRepository.findInvestRequestsForUser_H(10, 1, 1000d, 10, 0d);
//        List<InvestRequests> investRequestsForUser_l = irRepository.findInvestRequestsForUser_L(10, 1, 1000d, 10, 0d);
//
//        investRequestsForUser_h.addAll(investRequestsForUser_l);
//        System.out.println(investRequestsForUser_h);
//    }
//}
