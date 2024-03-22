package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.dao.openfeign.CouponActDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.CartItemPoMapper;
import cn.edu.xmu.oomall.customer.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;

@Repository
public class CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CartDao.class);
    public static final String KEY = "C%d";
    private RedisUtil redisUtil;
    @Value("${oomall.customer.customer.timeout}")
    private long timeout;

    private CartDao cartDao;
    private AddressDao addressDao;

    private CustomerPoMapper customerPoMapper;

    private CouponDao couponDao;

    private CouponActDao couponActDao;

    private CartItemPoMapper cartItemPoMapper;

    private ProductDao productDao;

    @Autowired
    @Lazy
    public CustomerDao(ProductDao productDao,CustomerPoMapper customerPoMapper, RedisUtil redisUtil, CartDao cartDao,AddressDao addressDao,CouponDao couponDao,CouponActDao couponActDao,CartItemPoMapper cartItemPoMapper){
        this.cartDao=cartDao;
        this.redisUtil=redisUtil;
        this.addressDao=addressDao;
        this.customerPoMapper=customerPoMapper;
        this.couponDao=couponDao;
        this.couponActDao=couponActDao;
        this.cartItemPoMapper=cartItemPoMapper;
        this.productDao=productDao;
    }

    /**
     * 获得bo对象
     * @author Zyh
     * @param po
     * @param redisKey
     * @return
     */
    private Customer build(CustomerPo po, String redisKey){
        logger.debug("customer build");
        Customer ret = CloneFactory.copy(new Customer(), po);
        ret.setId(po.getId());
        logger.debug("customer copy{}",ret.toString());
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.build(ret);
        return ret;
    }
    /**
     * 把bo中设置dao
     * @author Zyh
     * @param bo
     */
    private Customer build(Customer bo){
        bo.setCustomerDao(this);
        bo.setCartDao(this.cartDao);
        bo.setAddressDao(this.addressDao);
        bo.setCouponDao(this.couponDao);
        bo.setCouponActDao(this.couponActDao);
        bo.setProductDao(this.productDao);
        return bo;
    }

    /**
     * 按照id获得对象
     *
     * @author Zyh
     * @param id CartItem id
     * @return CartItem
     */
    public Customer findById(Long id){
        logger.debug("findObjById: id = {}",id);
        String key = String.format(KEY, id);
        Customer customer = (Customer) redisUtil.get(key);
        if (!Objects.isNull(customer)) {
            customer = this.build(customer);
        } else {
            Optional<CustomerPo> ret = this.customerPoMapper.findById(id);
            if (ret.isPresent()){
                logger.debug("findObjById: id = {}",id);
                customer = this.build(ret.get(), key);

            }else{
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "顾客", id));
            }
        }
        return customer;
    }

    public String save(Customer bo, UserDto userDto) throws RuntimeException
    {
        bo.setGmtModified(LocalDateTime.now());
        bo.setModifier(userDto);
        CustomerPo po = CloneFactory.copy(new CustomerPo(), bo);
        CustomerPo ret = this.customerPoMapper.save(po);
        if (IDNOTEXIST.equals(ret.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        }
        return String.format(KEY,bo.getId());
    }

    /**
     * 根据顾客id和page、pageSize获得购物车列表
     * @param id
     * @param page
     * @param pageSize
     * @return
     */

    public List<CartItem> retrieveCartList(Long id, int page, int pageSize)
    {
        CartItemPo ex=new CartItemPo();
        ex.setCustomerId(id);
        return this.cartItemPoMapper.findAll(Example.of(ex), PageRequest.of(page-1,pageSize))
                .map(po->CloneFactory.copy(new CartItem(),po))
                .stream().toList();
    }
}
