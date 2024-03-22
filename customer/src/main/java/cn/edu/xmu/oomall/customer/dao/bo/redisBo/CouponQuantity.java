package cn.edu.xmu.oomall.customer.dao.bo.redisBo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CouponQuantity extends OOMallObject implements Serializable {

    private Integer quantity;

    public CouponQuantity(Integer quantity){
        this.quantity=quantity;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

}
