package cn.edu.xmu.oomall.customer.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.vo.CouponActVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CouponActPoMapper {


}
