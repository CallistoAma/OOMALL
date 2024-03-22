package cn.edu.xmu.oomall.customer.controller;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.customer.aop.CustomerAudit;
import cn.edu.xmu.oomall.customer.controller.dto.CartItemDto;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.TestMapper;
import cn.edu.xmu.oomall.customer.service.CouponService;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.controller.dto.AddressDto;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.service.AddressService;
import cn.edu.xmu.oomall.customer.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AuthorizedCustomerController {

    private CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizedCustomerController.class);
    private AddressService addressService;

    private CouponService couponService;

    private CustomerService customerService;



    @Autowired
    public AuthorizedCustomerController(AddressService addressService, CartService cartService, CouponService couponService,CustomerService customerService) {
        this.addressService = addressService;
        this.cartService=cartService;
        this.couponService = couponService;
        this.customerService=customerService;
    }


    /**
     * 管理员查看逻辑删除顾客
     * @param shopId
     * @param id
     * @return
     */
    @CustomerAudit(departName ="shops")
    @DeleteMapping("/shops/{shopId}/customers/{id}")
    public  ReturnObject delUserById(@LoginUser UserDto user,
                                     @PathVariable("shopId")Long shopId,
                                     @PathVariable("id")Long id){
        this.customerService.delUserById(user,id);
        return new ReturnObject();
    }

    /**
     * 平台管理员封禁买家
     * @param user
     * @param shopId
     * @param id
     * @return
     */
    @CustomerAudit (departName = "shops")
    @PutMapping("/shops/{did}/customers/{id}/ban")
    public ReturnObject adminBanCustomer(@LoginUser UserDto user, @PathVariable("did")Long shopId,@PathVariable("id") Long id)
    {
        this.customerService.banUser(user,id);
        return new ReturnObject ();
    }

    /**
     * 平台管理员解禁买家
     * @param user
     * @param shopId
     * @param id
     * @return
     */
    @CustomerAudit(departName = "shops")
    @PutMapping("/shops/{did}/customers/{id}/release")
    public ReturnObject adminReleaseCustomer(@LoginUser UserDto user, @PathVariable("did")Long shopId,@PathVariable("id") Long id)
    {
        customerService.releaseUser(user,id);
        return new ReturnObject ();
    }

    /**
     * 买家获得购物车列表
     * @param user
     * @param page
     * @param pageSize
     * @return
     */
    @CustomerAudit(departName = "carts")
    @GetMapping("/carts")
    public ReturnObject customerGetProductList(@LoginUser UserDto user,
                                               @RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "5") int pageSize){
        List<CartItem> list= this.customerService.retrieveCartList(user,page,pageSize);
        List<CartItemDto> dtoList =list.stream().map(bo-> CloneFactory.copy(new CartItemDto(),bo)).collect(Collectors.toList());
        return new ReturnObject(new PageDto<>(dtoList,page,pageSize));
    }

    /**
     * 买家将商品加入购物车
     * @param user
     * @param cartItemVo
     * @return
     */
    @CustomerAudit(departName = "carts")
    @PostMapping("/carts")
    public ReturnObject addToCart(@LoginUser UserDto user, @Validated @RequestBody CartItemVo cartItemVo)
    {
        CartItem cartItem=CloneFactory.copy(new CartItem(),cartItemVo);
        this.cartService.addToCart(user,cartItem);
        return new ReturnObject();
    }

    /**
     * 买家清空购物车
     * @param user
     * @return
     */
    @CustomerAudit(departName = "carts")
    @DeleteMapping("/carts")
    public ReturnObject clearGoods(@LoginUser UserDto user)
    {
        this.cartService.clearGoods(user);
        return new ReturnObject();
    }


    @PutMapping("/carts/{id}")
    @CustomerAudit(departName = "carts")
    public ReturnObject updateCartItem(@RequestBody @Validated CartItemVo cartItemVo, @LoginUser UserDto user, @PathVariable("id") Long cartId){
        cartService.updateCartItem(cartItemVo.getProductId(),cartItemVo.getQuantity(),user,cartId);
        return new ReturnObject();
    }

    @DeleteMapping("/carts/{id}")
    @CustomerAudit(departName = "carts")
    public ReturnObject deleteCartItem(@PathVariable("id")  Long id,@LoginUser UserDto user){
        logger.debug("deleteCartItem-cartId:{}",user.toString());
        cartService.deleteCartItem(user,id);
        return new ReturnObject();
    }

    @PostMapping("/addresses")
    @CustomerAudit(departName = "addresses")
    public ReturnObject addAddress(@LoginUser UserDto user, @RequestBody @Validated AddressVo addressVo){
        logger.debug("userId:{}",user.toString());
        Address address = CloneFactory.copy(new Address(), addressVo);
        address.setBeDefault((byte) 0);
        Address ret=addressService.addAddress(user,address);
        logger.debug("after insert:{}",ret);
        return new ReturnObject(new AddressDto(ret));
    }

    @GetMapping("/addresses")
    @Transactional(propagation = Propagation.REQUIRED)
    @CustomerAudit(departName = "addresses")
    public ReturnObject retrieveAddress(@LoginUser UserDto user,@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10")Integer pageSize){
        List<Address> addresses=this.addressService.retrieveCustomerAddress(user,page,pageSize);
        return new ReturnObject(new PageDto<>(addresses, page, pageSize));
    }

    @PostMapping("/addresses/{id}/default")
    @CustomerAudit(departName = "addresses")
    public ReturnObject setDefaultAdd(@LoginUser UserDto user,@PathVariable Long id){
        addressService.setDefault(user,id);
        return new ReturnObject();
    }

    @PutMapping("addresses/{id}")
    @CustomerAudit(departName = "addresses")
    public ReturnObject changeAddressInfo(@PathVariable Long id,@RequestBody @Validated AddressVo addressVo,@LoginUser UserDto user){
        Address address =CloneFactory.copy(new Address(),addressVo);
        address.setId(id);
        addressService.changeAddressInfoById(user,id,address);
        return new ReturnObject();
    }

    @DeleteMapping("/addresses/{id}")
    @CustomerAudit(departName = "addresses")
    public ReturnObject deleteAddress(@LoginUser UserDto user,@PathVariable Long id){
        addressService.deleteAddress(user,id);
        return new ReturnObject();
    }

    @PostMapping("/couponactivities/{id}/coupons")
    @CustomerAudit(departName = "couponactivities")
    public ReturnObject recvCoupon(@LoginUser UserDto user, @PathVariable Long id){
        List<String> couponSnList=couponService.recvCoupon(user,id);
        return new ReturnObject(couponSnList);
    }
}
