package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
public class Logistics extends OOMallObject {

    /**
     * 物流平台的名称
     */
    private String name;

    /**
     * 物流平台的id
     */
    private String appId;

    /**
     * 接入方在物流平台生成的唯一身份标识
     */
    private String appAccount;

    /**
     * 用于校验请求接口传入的秘钥
     */
    private String secret;

    private String snPattern;
    private String logisticsClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public String getName() {
        return name;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppAccount() {
        return appAccount;
    }

    public void setAppAccount(String appAccount) {
        this.appAccount = appAccount;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSnPattern() {
        return snPattern;
    }

    public void setSnPattern(String snPattern) {
        this.snPattern = snPattern;
    }

    public String getLogisticsClass() {
        return logisticsClass;
    }

    public void setLogisticsClass(String logisticsClass) {
        this.logisticsClass = logisticsClass;
    }
}
