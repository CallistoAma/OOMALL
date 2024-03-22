package cn.edu.xmu.oomall.customer.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
    private ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductDao.class);
    @Autowired
    public ProductDao(ProductMapper productMapper){this.productMapper=productMapper;}


    private Product build(ProductPo po){
        Product bo = CloneFactory.copy(new Product(), po);
        bo.setProductDao(this);
        return bo;
    }
    public Product findById(Long id){
        logger.debug("find product id:{}",id);
        InternalReturnObject<ProductPo> ret= this.productMapper.findProductById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return this.build(ret.getData());
        }
    }
}
