package cn.edu.xmu.oomall.customer.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.controller.dto.SimpleOnsaleDto;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.vo.CouponActVo;
import cn.edu.xmu.oomall.customer.mapper.po.PagePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="product-service")
public interface ProductMapper {

    @GetMapping("/products/{id}")
    InternalReturnObject<ProductPo> findProductById(@PathVariable Long id);

    @GetMapping("/couponactivities/{id}")
    InternalReturnObject<CouponActPo> findCouponActById(@PathVariable Long id);

    @GetMapping("/shops/{shopId}/couponactivities/{id}")
    InternalReturnObject<CouponActPo> findCouponActByActIdAndShopId(@PathVariable Long shopId,@PathVariable Long id);

    @PutMapping("/shops/{shopId}/couponactivities/{id}")
    InternalReturnObject<ReturnObject> updateCouponAct(@PathVariable Long shopId, @PathVariable Long actId, @Validated @RequestBody CouponActVo couponActVo);

    @GetMapping("/shops/{shopId}/products/{id}/onsales")
    InternalReturnObject<PagePo<SimpleOnsaleDto>> getAllOnsale (@PathVariable(value = "shopId",required = true) Long shopId,
                                                                @PathVariable(value = "id",required = true) Long id,
                                                                @RequestParam(required = false,defaultValue = "0") Integer page,
                                                                @RequestParam(required = false,defaultValue = "10") Integer pageSize);
}
