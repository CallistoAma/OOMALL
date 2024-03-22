package cn.edu.xmu.oomall.customer.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({CartItem.class})
public class CartItemDto {
    private Long productId;
    private Integer quantity;

    public CartItemDto(CartItem cartItem)
    {
        super();
        CloneFactory.copy(this,cartItem);
    }
}