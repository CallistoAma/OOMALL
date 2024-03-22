package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.TestPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper extends JpaRepository<AddressPo,Long> {
}
