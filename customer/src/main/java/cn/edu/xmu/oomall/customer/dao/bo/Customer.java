package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CouponDao;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.CouponActDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.ProductDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@Data
@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({CustomerPo.class})
public class Customer extends OOMallObject implements Serializable {


    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Customer.class);

    private Long id;

    private String userName;
    private String password;

    private String name;

    private Integer point;

    private Byte invalid;

    private int beDeleted;

    private Long creatorId;

    private String creatorName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


    @JsonIgnore
    @ToString.Exclude
    private List<Address> addList;

    public List<Address> getAddList()
    {
        if(null == this.addList && null != this.addressDao){
            logger.debug("retrieve:{}",this.id);
            this.addList=this.addressDao.retrieveByCustomerId(this.id);
            logger.debug("getAddList: addList = {}", addList);
        }
        logger.debug("getAddList: addList = {}", this.addList);
        return this.addList;
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private AddressDao addressDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CartDao cartDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CouponDao couponDao;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CustomerDao customerDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CouponActDao couponActDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductDao productDao;

    public Address addAddress(Address address, UserDto user){
        List<Address> list=getAddList();
        logger.debug("!!!!!size:{}",list.size());
        if(list.size()==20){
            throw new BusinessException(ReturnNo.ADDRESS_OUTLIMIT, String.format(ReturnNo.ADDRESS_OUTLIMIT.getMessage()));
        }
        return addressDao.insert(address,user);

    }

    public List<String> recvCouponByActId(UserDto user,Long actId)
    {

        //先根据actId，通过openfeign调/couponactivities/{id}查询活动的信息
        //1、判断活动是否结束698  2、活动是否开始696 3、优惠券是否领完697
        //CouponActivity couponAct=this.couponActDao.findById(actId);
        CouponActivity couponAct=this.couponActDao.findByActIdAndShopId(actId);
        logger.debug("First find couponAct:{}",couponAct.toString());
        this.checkActivity(couponAct);
        //先根据actId、customerId在数据库中找出当前拥有的优惠券，建立bo。
        //判断当前有没有优惠券，
        List<Coupon> coupons = couponDao.retrieveCouponList(user,actId);
        logger.debug("coupons size:{}",coupons.size());
        //若有优惠券
        if(!coupons.isEmpty())
        {
            //若type为1，总量限制，每人领1张，此时又能查出优惠券，说明领过了，报699
            //若type为0，每人限领，比对当前数量和quantity，大于等于不能领，报699
            if( couponAct.getQuantityType().equals((byte)1)||coupons.size()>=couponAct.getQuantity()){
                throw new BusinessException(ReturnNo.COUPON_EXIST,ReturnNo.COUPON_EXIST.getMessage());
            }
        }
//        couponAct=this.couponActDao.findByActIdAndShopId(actId);
//        logger.debug("Second find couponAct:{}",couponAct.toString());
        List<String> couponSn=new ArrayList<>();
        if(couponAct.getQuantityType().equals((byte)1)){
            /*若Type为1，采取预先领取的方式，设计思路是，先调用product模块的api修改优惠券库存量，例如先取出100张，
            在redis缓存中存入100张的数据，在每次领取优惠券创建bo对象之前，先读redis检查当前优惠券是否还有库存，
            若有，则将其数量-1，若没有了需要再次调用api修改product模块中的库存量，并领取一定数量到redis中。
            若此时redis库存量为0，而quantity值也为0，说明优惠券被领完了，报错697*/
            logger.debug("QuantityType为1");
            this.couponDao.updateCouponQuantity(couponAct);
            couponSn.add(this.couponDao.insert(couponAct,user));
        }
        else{
            for (int i = 0; i < couponAct.getQuantity()-coupons.size(); i++) {
                couponSn.add(this.couponDao.insert(couponAct,user));
            }
        }
        logger.debug("couponSn:{}",couponSn.toString());
        return couponSn;
        //若没有，则buildcoupon对象,去领取，若Type为0，给他build quantity次bo
        //若Type为1，给他build 1次，需要去-1quantity。
        // （redis缓存将预先取出的100张--，在要领取时，先读redis中是否有当前优惠券的信息，
        // 没有的话需要调product模块的/shops/{shopId}/couponactivities/{id}
        // shopId在查询到的活动中可以获取，id是活动id
        // 去修改优惠券的库存，也就是-100）

        //每build一次，insert一次

        //zyhzyh
    }

    private void checkActivity(CouponActivity couponAct){
        if(couponAct.getEndTime().isBefore(LocalDateTime.now())){
            throw new BusinessException(ReturnNo.COUPON_END,String.format(ReturnNo.COUPON_END.getMessage()));
        }
        else if(couponAct.getBeginTime().isAfter(LocalDateTime.now()) || LocalDateTime.parse(couponAct.getCouponTime()).isAfter(LocalDateTime.now())){
                throw new BusinessException(ReturnNo.COUPON_NOTBEGIN,String.format(ReturnNo.COUPON_NOTBEGIN.getMessage()));
        }
    }





    /*Vaild：有效状态*/
    @ToString.Exclude
    @JsonIgnore
    public static final Byte Vaild=0;

    /*Banned：封禁状态*/
    @ToString.Exclude
    @JsonIgnore
    public static final Byte Banned=1;

    /*Deleted：删除状态*/
    @ToString.Exclude
    @JsonIgnore
    public static final Byte Deleted=-1;


    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(Vaild, "有效");
            put(Banned, "禁用");
            put(Deleted, "删除");
        }
    };

    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>() {
        {
            put(Vaild, new HashSet<>() {
                {
                    add(Banned);
                    add(Deleted);
                }
            });
            put(Banned, new HashSet<>() {
                {
                    add(Vaild);
                    add(Deleted);
                }
            });
            put(Deleted, new HashSet<>() {
                {
                }
            });
        }
    };



    public boolean allowStatus(Byte status) {
        if(this.beDeleted==1)
            return false;
        boolean ret = false;

        if (null != status && null != this.invalid) {
            Set<Byte> allowStatusSet = toStatus.get(this.invalid);
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }


    public void banUser(UserDto user)
    {
        if(this.allowStatus(Customer.Banned))
            this.invalid=Banned;
        else
            throw new BusinessException(ReturnNo.CUSTOMER_CANNOTBANNED);
        this.customerDao.save(this,user);
    }

    public void releaseUser(UserDto user)
    {
        if(this.allowStatus(Customer.Vaild))
            this.invalid=Vaild;
        else
            throw new BusinessException(ReturnNo.CUSTOMER_CANNOTVAILD);
        customerDao.save(this,user);
    }

    public List<CartItem> retrieveCartList(int page,int pageSize)
    {
        List<CartItem> list = this.customerDao.retrieveCartList(this.id,page,pageSize);
        return list;
    }

    public void addToCart(UserDto user,CartItem cartItem)
    {
        Product product=this.productDao.findById(cartItem.getProductId());
        //如果有相同商品，则数量相加
        List <CartItem> cartItems=this.customerDao.retrieveCartList(this.id,1,1000);
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                if(item.getQuantity()+cartItem.getQuantity()<=product.getQuantity())
                    item.setQuantity(item.getQuantity()+cartItem.getQuantity());
                else
                    throw new BusinessException(ReturnNo. GOODS_STOCK_SHORTAGE);
                return;
            }
        }
        //如果没有,则在商品库存充足的情况下新增
        if(cartItem.getQuantity()<=product.getQuantity())
           this.cartDao.insert(cartItem,user);
        else
            throw new BusinessException(ReturnNo. GOODS_STOCK_SHORTAGE);
    }

    public void delCustomer(UserDto user)
    {
        if(this.allowStatus(Customer.Deleted))
        {
            this.beDeleted=Deleted;
            this.customerDao.save(this,user);
        }
        else
            throw new BusinessException(ReturnNo.CUSTOMER_CANNOTDUBILEDELETE);

    }


    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {this.gmtCreate=gmtCreate;}
    public LocalDateTime getGmtCreate() {return this.gmtCreate;}


    @Override
    public void setGmtModified(LocalDateTime gmtModified) {

    }
    public void setUserName(String userName) {this.userName=userName;}
    public String getUserName(){return this.userName;}
    public void setPassword(String psw) {this.password=psw;}
    public String getPassword(){return this.password;}
    public void setName(String name) {this.name=name;}
    public String getName(){return this.name;}
    public void setInvalid(Byte invalid) {this.invalid=invalid;}
    public Byte getInvalid(){return this.invalid;}

    public void setPoint(Integer point) {this.point=point;}
    public Integer getPoint(){return this.point;}

    public void setId(Long id){this.id=id;}
    public Long getId(){return this.id;}

    public void setBeDeleted(int beDeleted){this.beDeleted=beDeleted;}
    public int getBeDeleted(){return this.beDeleted;}

    public void setCreatorId(Long creatorId){this.creatorId=creatorId;}
    public Long getCreatorId(){return this.creatorId;}

    public void setCreatorName(String creatorName){this.creatorName=creatorName;}
    public String getCreatorName(){return this.creatorName;}


}
