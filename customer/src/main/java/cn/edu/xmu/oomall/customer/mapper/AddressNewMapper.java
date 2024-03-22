package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressNewMapper extends JpaRepository<AddressPo,Long> {


    @Query(value = "select DISTINCT a from AddressPo a where a.customerId=:customerId and a.beDefault=:status")
    Optional<AddressPo> findByCustomerIdEqualsAndBeDefaultEquals(Long customerId,Byte status);

    List<AddressPo> findByCustomerId(Long customerId,Pageable pageable);
}
