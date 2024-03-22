package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.Coupon;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.bo.redisBo.CouponQuantity;
import cn.edu.xmu.oomall.customer.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.CouponActPoMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.vo.CouponActVo;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.CouponPo;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@Repository
public class CouponDao {

    private Logger logger = LoggerFactory.getLogger(CouponDao.class);
    @Value("${oomall.customer.customer.timeout}")
    private long timeout;
    public static final String KEY = "Coupon%d";

    public static final String KEY_QUANTITY = "Coupon_Quantity%d";
    private CouponPoMapper couponPoMapper;

    private ProductMapper productMapper;

    private final SnowFlakeIdWorker snowFlakeIdWorker;
    private RedisUtil redisUtil;

    @Autowired
    public CouponDao(CouponPoMapper couponPoMapper, RedisUtil redisUtil,ProductMapper productMapper,SnowFlakeIdWorker snowFlakeIdWorker){
        this.redisUtil=redisUtil;
        this.couponPoMapper=couponPoMapper;
        this.productMapper=productMapper;
        this.snowFlakeIdWorker=snowFlakeIdWorker;
    }

    private Coupon build(CouponPo po, String redisKey){
        logger.debug("build2");
        Coupon ret = null;//CloneFactory.copy(new Coupon(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.build(ret);
        return ret;
    }


    private Coupon build(Coupon bo){
        logger.debug("build1");


        return bo;
    }
    public List<Coupon> retrieveCouponList(UserDto user,Long id){
        assert(null != id):  new IllegalArgumentException();
        Pageable pageable = PageRequest.of(0, MAX_RETURN);
        List<CouponPo> pos = new ArrayList<CouponPo>();
        logger.debug("test");
        pos=this.couponPoMapper.findByActivityIdEqualsAndCustomerIdEquals(id,user.getId(),pageable);
        return pos.stream().map(po -> {
            logger.debug("retrieveByActivityId: po = {}",po);
            Coupon bo = this.build(po, null);
            return bo;
        }).collect(Collectors.toList());
    }

    public void updateCouponQuantity(CouponActivity couponAct){

        //1、判断redis和库存都为0，则领完
        //2、若redis没有信息，或redis中库存为0，则要去product中的库存取
        String key = String.format(KEY_QUANTITY, couponAct.getId());
        CouponQuantity couponQuantity=(CouponQuantity) redisUtil.get(key);
        //若redis中没有该优惠券活动的信息 或 有信息但数量为0
        if(Objects.isNull(couponQuantity)||couponQuantity.getQuantity()==0){
            //调UPDATE /shops/{shopId}/couponactivities/{id}，先去那里-100
            //建立一个Vo：CouponActVo，发送消息
            //再存入redis。
            logger.debug("需补充redis，减少库存");
            if(couponAct.getQuantity()<=0){
                //判断redis和库存都为0，则领完
                throw new BusinessException(ReturnNo.COUPON_FINISH,ReturnNo.COUPON_FINISH.getMessage());
            }
            CouponActVo vo=CloneFactory.copy(new CouponActVo(),couponAct);
            vo.setQuantity(vo.getQuantity()-100);//一次领100张
            if(vo.getQuantity()<0){//库存不足100张则领到0张
                vo.setQuantity(0);
            }
            InternalReturnObject<ReturnObject> ret= this.productMapper.updateCouponAct(couponAct.getShopId(),couponAct.getId(),vo);
            ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
            if (!returnNo.equals(ReturnNo.OK)) {
                throw new BusinessException(returnNo, ret.getErrmsg());
            }else{
                logger.debug("add coupon amount:{}",couponAct.getQuantity()- vo.getQuantity());
                //向redis中新增优惠券数量
                redisUtil.set(key,new CouponQuantity(couponAct.getQuantity()- vo.getQuantity()-1),timeout);
            }
        }
        else{
            Long kk=redisUtil.decr(key,1);
            logger.debug("redis库存-1：{}",kk);
        }
    }

    public String insert(CouponActivity couponActivity,UserDto user) throws RuntimeException{
        CouponPo couponPo=new CouponPo();
        couponPo.setCouponSn('C'+snowFlakeIdWorker.nextId().toString());
        couponPo.setName(couponActivity.getName());
        couponPo.setCustomerId(user.getId());
        couponPo.setActivityId(couponActivity.getId());
        couponPo.setBeginTime(LocalDateTime.now());
        if(couponActivity.getValidTerm().equals(0)){
            couponPo.setEndTime(couponActivity.getEndTime());
        }
        else {
            logger.debug("has Validterm");
            couponPo.setEndTime(LocalDateTime.now().plusDays(couponActivity.getValidTerm()));
        }
        couponPo.setUsed((byte) 1);
        couponPo.setCreatorId(couponActivity.getCreatorId());
        couponPo.setCreatorName(couponActivity.getCreatorName());
        couponPo.setGmtCreate(LocalDateTime.now());
        logger.debug("insert: po = {}", couponPo);
        couponPo=this.couponPoMapper.save(couponPo);
        logger.debug("insert: po = {}", couponPo);
        return couponPo.getCouponSn();
    }
}
