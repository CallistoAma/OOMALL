package cn.edu.xmu.oomall.customer.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.RegionPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="region-service")
public interface RegionMapper {
    @GetMapping("/regions/{id}")
    InternalReturnObject<RegionPo> findRegionById(@PathVariable Long id);
}
