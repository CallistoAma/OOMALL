package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.dto.SimpleOnsaleDto;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.po.PagePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CartService {

    private Logger logger = LoggerFactory.getLogger(CartService.class);
    private CartDao cartDao;
    private CustomerDao customerDao;

    private ProductMapper productMapper;

    @Autowired
    public CartService(CartDao cartDao,CustomerDao customerDao,ProductMapper productMapper) {
        this.cartDao = cartDao;
        this.customerDao=customerDao;
        this.productMapper=productMapper;
    }

    public void updateCartItem(Long productId, Integer quantity, UserDto user, Long cartId) {
        CartItem ret = cartDao.findById(cartId,user);
        ret.changeCartItemInfo(user, productId, quantity);
    }

    public void deleteCartItem(UserDto user, Long cartId) {
        logger.debug("deleteCartItem: cartId = {}", cartId);
        this.cartDao.findById(cartId,user);
        this.cartDao.delete(cartId);
    }

    public void addToCart(UserDto user, CartItem cartItem)
    {
        //用户存在才能加入购物车
        Customer customer=this.customerDao.findById(user.getId());
        //团购和预售商品不能加入购物车，出615错误
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=productMapper.getAllOnsale(0L,cartItem.getProductId(),1,100);
        ReturnNo returnNo=ReturnNo.getByCode(internalReturnObject.getErrno());
        if(!ReturnNo.OK.equals(returnNo)) {
            throw new BusinessException(returnNo);
        }

        List<SimpleOnsaleDto> list=internalReturnObject.getData().getList();
        if(list.get(0).getType()==2||list.get(0).getType()==3)
            throw new BusinessException(ReturnNo.CUSTOMER_CARTNOTALLOW);
        customer.addToCart(user,cartItem);
    }

    public void clearGoods(UserDto user)
    {
        List<CartItem> cartItems = this.cartDao.findAll(user.getId());
        cartItems.forEach(cartItem-> this.cartDao.delete(cartItem.getId()));
    }
}
