package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.openfeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({ProductPo.class})
public class Product {

    private Long id;
    private Integer quantity;

    private Long price;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private ProductDao productDao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
