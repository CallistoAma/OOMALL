package cn.edu.xmu.oomall.customer.controller.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressVo {

    @NotNull(message = "地区id不能为空")
    private Long regionId;
    @NotNull(message = "地址不能为空")
    private String address;
    @NotNull(message = "联系人姓名不能为空")
    private String consignee;
    @NotNull(message = "联系电话不能为空")
    private String mobile;

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
