package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemPoMapper extends JpaRepository<CartItemPo,Long> {

    @Query(value ="select DISTINCT a from CartItemPo a where a.customerId=:customerId and a.id=:id")
    Optional<CartItemPo> findByCustomerIdEqualsAndIdEquals(Long customerId, Long id);
}
