package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import org.apache.catalina.User;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AddressService {


    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);
    private CustomerDao customerDao;

    private AddressDao addressDao;


    @Autowired
    public AddressService(CustomerDao customerDao,AddressDao addressDao){this.customerDao=customerDao;this.addressDao=addressDao;}

    public Address addAddress(UserDto user,Address address)
    {
        logger.debug("addAddress: id = {}",user.getId());
        Customer custo=customerDao.findById(user.getId());
        Address ret=custo.addAddress(address,user);
        return ret;
    }

    public List<Address> retrieveCustomerAddress(UserDto user,Integer page,Integer pageSize){
        List<Address> addlist=addressDao.findByCustomerId(user.getId(),page,pageSize);
        return addlist;
    }

    public void setDefault(UserDto user,Long id){
        Address oldDefault=addressDao.findDefaultByCustomerId(user.getId());
        logger.debug("defaultAddress={}",oldDefault.toString());
        Address newDefault=addressDao.findById(id,user);
        if(!oldDefault.getId().equals(newDefault.getId())) {
            oldDefault.changeDefault((byte) 0, user);
            newDefault.changeDefault((byte) 1, user);
        }
    }

    public void changeAddressInfoById(UserDto user,Long id,Address address)
    {
        Address oldAddress= this.addressDao.findById(id,user);
        address.setBeDefault(oldAddress.getBeDefault());
        this.addressDao.save(address,user);
    }

    public void deleteAddress(UserDto user,Long id){
        this.addressDao.findById(id,user);
        this.addressDao.delete(id);
    }
}
