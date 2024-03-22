package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopLogisticsPoMapper extends JpaRepository<ShopLogisticsPo, Long> {
}
