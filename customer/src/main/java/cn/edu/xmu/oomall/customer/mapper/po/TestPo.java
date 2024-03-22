package cn.edu.xmu.oomall.customer.mapper.po;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="customer_test")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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
