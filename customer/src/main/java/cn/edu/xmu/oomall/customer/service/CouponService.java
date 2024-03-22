package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CouponDao;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private CouponDao couponDao;
    private CustomerDao customerDao;
    @Autowired
    public CouponService(CouponDao couponDao, CustomerDao customerDao){this.couponDao=couponDao;this.customerDao=customerDao;}

    public List<String> recvCoupon(UserDto user, Long id)
    {
        Customer customer= customerDao.findById(user.getId());
        return customer.recvCouponByActId(user,id);

    }
}
