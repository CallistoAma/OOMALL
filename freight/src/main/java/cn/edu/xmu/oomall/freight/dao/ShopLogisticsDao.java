package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.mapper.jpa.ShopLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@Repository
public class ShopLogisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(ShopLogisticsDao.class);
    public static final String KEY = "SL%d";
    private RedisUtil redisUtil;
    private ShopLogisticsPoMapper shopLogisticsPoMapper;
    @Lazy
    private ExpressDao expressDao;
    private LogisticsAdaptorFactory logisticsAdaptorFactory;

    @Value("${oomall.freight.shop-logistics.timeout}")
    private int timeout;

    @Autowired
    @Lazy
    public ShopLogisticsDao(RedisUtil redisUtil, ShopLogisticsPoMapper shopLogisticsPoMapper,
                            ExpressDao expressDao, LogisticsAdaptorFactory logisticsAdaptorFactory) {
        this.redisUtil = redisUtil;
        this.shopLogisticsPoMapper = shopLogisticsPoMapper;
        this.expressDao = expressDao;
        this.logisticsAdaptorFactory = logisticsAdaptorFactory;
    }

    public ShopLogistics findById(Long shopId, Long id) {
        if (null == id) {
            throw new IllegalArgumentException("ShopLogistics.findById: shopLogistic id is null");
        }
        logger.debug("findObjById: id = {}", id);
        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            ShopLogistics shopLogistics = (ShopLogistics) redisUtil.get(key);
            this.build(shopLogistics);
            return shopLogistics;
        } else {
            Optional<ShopLogisticsPo> po = this.shopLogisticsPoMapper.findById(id);
            if (po.isPresent()) {
                if (!PLATFORM.equals(shopId) && !shopId.equals(po.get().getShopId())) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商铺物流", id, shopId));
                }
                return this.build(po.get(), Optional.of(key));
            }
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺物流", id));
        }
    }

    public void build(ShopLogistics bo) {
        bo.setShopLogisticsDao(this);
        bo.setExpressDao(this.expressDao);
        bo.setLogisticsAdaptor(this.logisticsAdaptorFactory);
    }

    public ShopLogistics build(ShopLogisticsPo po, Optional<String> redisKey) {
        ShopLogistics bo = CloneFactory.copy(new ShopLogistics(), po);
        this.build(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    public ShopLogistics insert(ShopLogistics bo, UserDto user) {
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        ShopLogisticsPo po = CloneFactory.copy(new ShopLogisticsPo(), bo);
        logger.debug("save: po = {}", po);
        po = shopLogisticsPoMapper.save(po);
        bo.setId(po.getId());
        return bo;
    }
}
