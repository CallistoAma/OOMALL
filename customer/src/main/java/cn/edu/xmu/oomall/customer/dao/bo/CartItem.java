package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.openfeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.catalina.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({CartItemPo.class, CartItemVo.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItem extends OOMallObject implements Serializable {


    private Long productId;

    private Integer quantity;

    private Long price;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CartDao cartDao;

    @Setter
    private Product product;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductDao productDao;

    public void changeCartItemInfo(UserDto user,Long productId,Integer quantity) {
        Product product=productDao.findById(productId);
        if(product.getQuantity()<quantity) {
            throw new BusinessException(ReturnNo.CUSTOMER_QUANTITYNOTALLOW,String.format(ReturnNo.CUSTOMER_QUANTITYNOTALLOW.getMessage()));
        }
        this.setPrice(product.getPrice());
        this.setQuantity(quantity);
        this.setProductId(productId);
        this.setProduct(product);
        this.cartDao.save(this,user);
    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {this.id=id;
    }

    public void setProductId(Long id) {this.productId=id;}
    public Long getProductId(){return this.productId;}

    public void setQuantity(Integer quantity){this.quantity=quantity;}

    public Integer getQuantity(){return this.quantity;}

    public void setPrice(Long price){this.price=price;}

    public Long getPrice(){return this.price;}

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

}
