package cn.edu.xmu.oomall.customer.controller.vo;


import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@Data
@NoArgsConstructor
public class CartItemVo {

    @NotNull(message = "商品不能为空")
    private Long productId;
    @Min(value = 1, message = "至少购买一个")
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
