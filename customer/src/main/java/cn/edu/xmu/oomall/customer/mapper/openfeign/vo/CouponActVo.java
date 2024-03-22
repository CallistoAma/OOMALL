package cn.edu.xmu.oomall.customer.mapper.openfeign.vo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import jakarta.validation.constraints.NotNull;

@CopyFrom({CouponActivity.class})
public class CouponActVo{

    @NotNull(message = "活动名不能为空")
    private String name;
    @NotNull(message = "优惠券开领时间不能为空")
    private String couponTime;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    @NotNull(message = "优惠券类型不能为空")
    private Byte quantityType;
    @NotNull(message = "优惠券有效时间不能为空")
    private Integer validTerm;
    @NotNull(message = "活动类型不能为空")
    private String strategy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(String couponTime) {
        this.couponTime = couponTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Byte getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Byte quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
