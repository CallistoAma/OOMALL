package cn.edu.xmu.oomall.customer.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.dao.openfeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.CartItemPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CartDao {
    private static final Logger logger = LoggerFactory.getLogger(CartDao.class);
    public static final String KEY = "Cart%d";
    @Value("${oomall.customer.customer.timeout}")
    private long timeout;

    private ProductDao productDao;
    private CartItemPoMapper cartItemPoMapper;

    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    public CartDao(CartItemPoMapper cartItemPoMapper,RedisUtil redisUtil,ProductDao productDao){
        this.cartItemPoMapper=cartItemPoMapper;
        this.redisUtil=redisUtil;
        this.productDao=productDao;
    }

    /**
     * 获得bo对象
     * @author Zyh
     * <p>
     * date: 2022-11-20 11:46
     * @param po
     * @param redisKey
     * @return
     */
    private CartItem build(CartItemPo po, String redisKey){
        CartItem ret = CloneFactory.copy(new CartItem(), po);
        logger.debug("cart bo:{}",ret.toString());
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.build(ret);
        return ret;
    }
    /**
     * 把bo中设置dao
     * @author Zyh
     * <p>
     * date: 2022-11-20 11:46
     * @param bo
     */
    private CartItem build(CartItem bo){
        bo.setCartDao(this);
        bo.setProductDao(this.productDao);
        bo.setProduct(this.productDao.findById(bo.getProductId()));
        return bo;
    }

    /**
     * 按照id获得对象
     *
     * @author Zyh
     * @param id CartItem id
     * @return CartItem
     */
    public CartItem findById(Long id,UserDto user){
        if (id.equals(null)) {
            throw new IllegalArgumentException("findById: id is null");
        }
        logger.debug("findObjById: id = {}",id);
        String key = String.format(KEY, id);
        CartItem cartItem = (CartItem) redisUtil.get(key);
        if (!Objects.isNull(cartItem)) {
            cartItem = this.build(cartItem);
        } else {
            Optional<CartItemPo> ret = this.cartItemPoMapper.findById(id);
            if (ret.isPresent()){
                logger.debug("ret is present:{}",ret.toString());
                cartItem = this.build(ret.get(), key);
            }else{
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "购物车商品", id));
            }
        }
        if(!cartItem.getCreatorId().equals(user.getId())){
            throw new BusinessException(ReturnNo.CUSTOMER_CARTNOBELONGTO, String.format(ReturnNo.CUSTOMER_CARTNOBELONGTO.getMessage()));
        }
        return cartItem;
    }

    /**
     * 保存对象
     *
     * @author Zyh
     * @param cartItem 购车商品对象
     * @param userDto 用户
     * @return Shop
     */
    public String save(CartItem cartItem, UserDto userDto) throws RuntimeException{
        if (cartItem.getId().equals(null)){
            throw new IllegalArgumentException("save: cart id is null");
        }
        String key = String.format(KEY, cartItem.getId());
        cartItem.setModifier(userDto);
        cartItem.setGmtModified(LocalDateTime.now());
        CartItemPo po = CloneFactory.copy(new CartItemPo(),cartItem);
        po.setCustomerId(userDto.getId());
        logger.debug("save: po = {}", po);
        po=this.cartItemPoMapper.save(po);
        //cartItem=insert(cartItem,userDto);
        build(po, key); // 更新了数据，应该更新redis中缓存
        return key;
    }

    public CartItem insert(CartItem cartItem, UserDto userDto) throws RuntimeException{
        cartItem.setCreator(userDto);
        cartItem.setGmtCreate(LocalDateTime.now());
        CartItemPo po = CloneFactory.copy(new CartItemPo(), cartItem);
        po.setId(null);
        po.setCustomerId(userDto.getId());
        po=this.cartItemPoMapper.save(po);
        cartItem.setId(po.getId());
        //cartItem.setRegion(regionDao.findById(address.getRegionId()));
        return cartItem;
    }


    /**
     * 根据id物理删除
     * @author Zyh
     * @param id
     */
    public void delete(Long id) {
        this.cartItemPoMapper.deleteById(id);
    }

    /**
     * 根据顾客id找到所有items
     * @param id
     * @return
     */
    public List<CartItem> findAll(Long id){
        CartItemPo ex=new CartItemPo();
        ex.setCustomerId(id);
        return  this.cartItemPoMapper.findAll(Example.of(ex)).stream()
                .map(po->CloneFactory.copy(new CartItem(),po)).toList();
    }
}
