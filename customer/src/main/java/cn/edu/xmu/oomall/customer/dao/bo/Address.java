package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.dto.AddressDto;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({AddressVo.class, AddressPo.class, AddressDto.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address extends OOMallObject implements Serializable {


    private Long regionId;

    private String address;

    private String consignee;

    private String mobile;


    private Byte beDefault;

    private Region region;
    @Setter
    @ToString.Exclude
    @JsonIgnore
    private RegionDao regionDao;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private AddressDao addressDao;

    public void changeDefault(Byte status, UserDto user){
        beDefault=status;
        addressDao.save(this,user);
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id=id;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getBeDefault() {
        return beDefault;
    }

    public void setBeDefault(Byte beDefault) {
        this.beDefault = beDefault;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }


}
