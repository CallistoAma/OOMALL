//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@Document("regionTemplate")
@CopyFrom({PieceTemplate.class, RegionTemplate.class})
public class PieceTemplatePo {

    @MongoId(FieldType.OBJECT_ID)
    private String objectId;
    private Integer firstItems;
    private Long firstPrice;
    private Integer additionalItems;
    private Long additionalPrice;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getFirstItems() {
        return firstItems;
    }

    public void setFirstItems(Integer firstItems) {
        this.firstItems = firstItems;
    }

    public Long getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Long firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Integer getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(Integer additionalItems) {
        this.additionalItems = additionalItems;
    }

    public Long getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(Long additionalPrice) {
        this.additionalPrice = additionalPrice;
    }
}
