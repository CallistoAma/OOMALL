package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(ShopLogisticsPo.class)
@Data
public class ShopLogistics extends OOMallObject implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);
    //假设account为属性
    private String account;
    //假设shopId为属性
    private Long shopId;

    private String secret;

    public Logistics getLogistics() {
        //TODO：dgn3-009（包裹api）用到了get，但没有添加所有属性和对应的DAO，所以直接new一个返回了，后续应从数据库获得
        return new Logistics("顺丰快递", "123", "123", "123", "123", "zTOAdaptor", 1L);
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    private Logistics logistics;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ShopLogisticsDao shopLogisticsDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ExpressDao expressDao;

    @ToString.Exclude
    @JsonIgnore
    private LogisticsAdaptor logisticsAdaptor;

    /**
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    public Express createExpress(Long shopId, Express express, UserDto user) {
        express.setShopId(shopId);
        express.setStatus(Express.UNSHIPPED);
        logger.debug("createExpress: logisticsAdaptor = {}", logisticsAdaptor);
        PostCreatePackageAdaptorDto adaptorDto = this.logisticsAdaptor.createPackage(this, express);
        if (adaptorDto.getBillCode() != null) {
            express.setBillCode(adaptorDto.getBillCode());
        }
        this.expressDao.insert(express, user);
        logger.debug("createExpress: dto = {}", adaptorDto);
        return express;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setLogisticsAdaptor(LogisticsAdaptorFactory factory) {
        this.logisticsAdaptor = factory.createLogisticAdaptor(this.getLogistics());
    }

}
