package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.ExpressPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressPoMapper extends JpaRepository<ExpressPo, Long> {
    ExpressPo findByBillCode(String billCode);
}