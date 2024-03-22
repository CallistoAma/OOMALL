package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.RegionPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({RegionPo.class})
public class Region {
    private Long id;
    private String  name;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private RegionDao regionDao;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
