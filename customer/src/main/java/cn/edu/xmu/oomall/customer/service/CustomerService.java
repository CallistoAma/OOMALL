package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private CustomerDao customerDao;
    private RedisUtil redisUtil;
    public CustomerService(CustomerDao customerDao,RedisUtil redisUtil)
    {
        this.customerDao=customerDao;
        this.redisUtil=redisUtil;
    }
    public void banUser(UserDto user,Long id) {
        Customer customer=this.customerDao.findById(id);
        customer.banUser(user);
    }
    public void releaseUser(UserDto user,Long id) {
        Customer customer=customerDao.findById(id);
        customer.releaseUser(user);
    }

    public List<CartItem> retrieveCartList(UserDto userDto,int page,int pageSize)
    {
        Customer customer=this.customerDao.findById(userDto.getId());
        List<CartItem> list=customer.retrieveCartList(page,pageSize);
        return list;
    }

    public void delUserById(UserDto user,Long id)
    {
        Customer customer=this.customerDao.findById(id);
        customer.delCustomer(user);
    }
}