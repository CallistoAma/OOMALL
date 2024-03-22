package cn.edu.xmu.oomall.customer.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CouponActDao {

    private ProductMapper productMapper;

    private static final Logger logger = LoggerFactory.getLogger(CouponActDao.class);

    @Autowired
    public CouponActDao(ProductMapper productMapper){this.productMapper=productMapper;}

    private CouponActivity build(CouponActPo po){
        CouponActivity bo = CloneFactory.copy(new CouponActivity(), po);
        bo.setShopId(po.getShop().getId());
        //bo.setProductDao(this);
        return bo;
    }

    public CouponActivity findById(Long id){
        logger.debug("find couponAct id:{}",id);
        InternalReturnObject<CouponActPo> ret= this.productMapper.findCouponActById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            CouponActivity couponAct=this.build(ret.getData());
            //couponAct.setShopId(ret.getData().getShop().getId());
            logger.debug("shopId:{}",couponAct.getShopId());
            return this.build(ret.getData());
        }
    }

    public CouponActivity findByActIdAndShopId(Long actId){
        logger.debug("find couponAct id:{}",actId);
        InternalReturnObject<CouponActPo> ret= this.productMapper.findCouponActByActIdAndShopId(0L,actId);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            CouponActivity couponAct=this.build(ret.getData());
            couponAct.setCreatorId(ret.getData().getCreator().getId());
            couponAct.setCreatorName(ret.getData().getCreator().getName());
            return couponAct;
        }
    }
}