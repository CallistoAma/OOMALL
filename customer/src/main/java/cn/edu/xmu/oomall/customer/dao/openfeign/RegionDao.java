package cn.edu.xmu.oomall.customer.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
import cn.edu.xmu.oomall.customer.dao.bo.Region;
import cn.edu.xmu.oomall.customer.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.RegionPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RegionDao {
    private RegionMapper regionMapper;

    @Autowired
    public RegionDao(RegionMapper regionMapper){this.regionMapper=regionMapper;}


    private Region build(RegionPo po){
        Region bo = CloneFactory.copy(new Region(), po);
        bo.setRegionDao(this);
        return bo;
    }
    public Region findById(Long id){
        InternalReturnObject<RegionPo> ret= this.regionMapper.findRegionById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return this.build(ret.getData());
        }
    }
}
