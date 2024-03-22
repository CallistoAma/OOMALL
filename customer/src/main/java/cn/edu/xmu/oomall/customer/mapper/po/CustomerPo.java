package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="customer_customer")
@Data
@Builder
@AllArgsConstructor
@CopyFrom({Customer.class})
@NoArgsConstructor
public class CustomerPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 创建者
     */
    private String creatorName;

    /**
     * 修改者id
     */
    private Long modifierId;

    /**
     * 修改者
     */
    private String modifierName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    private String userName;
    private String password;
    private String name;
    private Byte invalid;
    private Integer beDeleted;
    private Integer point;

    public void setUserName(String userName) {this.userName=userName;}
    public String getUserName(){return this.userName;}
    public void setPassword(String psw) {this.password=psw;}
    public String getPassword(){return this.password;}
    public void setName(String name) {this.name=name;}
    public String getName(){return this.name;}
    public void setInvalid(Byte invalid) {this.invalid=invalid;}
    public Byte getInvalid(){return this.invalid;}
    public void setPoint(Integer point) {this.point=point;}
    public Integer getPoint(){return this.point;}
    public void setBeDeleted(int beDeleted){this.beDeleted=beDeleted;}
    public int getBeDeleted(){return this.beDeleted;}
    public void setCreatorId(Long creatorId){this.creatorId=creatorId;}
    public Long getCreatorId(){return this.creatorId;}
    public void setCreatorName(String creatorName){this.creatorName=creatorName;}
    public String getCreatorName(){return this.creatorName;}
    public void setGmtCreate(LocalDateTime gmtCreate) {this.gmtCreate=gmtCreate;}
    public LocalDateTime getGmtCreate() {return this.gmtCreate;}
    public void setId(Long id){this.id=id;}
    public Long getId(){return this.id;}

}
