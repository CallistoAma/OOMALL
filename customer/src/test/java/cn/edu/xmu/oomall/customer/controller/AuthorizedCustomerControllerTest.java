/*
package cn.edu.xmu.oomall.customer.controller;


import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.CustomerTestApplication;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.dao.bo.redisBo.CouponQuantity;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.CouponActPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.RegionPo;
import cn.edu.xmu.oomall.customer.mapper.openfeign.vo.CouponActVo;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import com.google.protobuf.Any;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;

import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = CustomerTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuthorizedCustomerControllerTest {

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private RegionMapper regionMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private Customer customer;

    private static String userToken, UnregisteredShopToken, adminToken, WrongUserToken,userToken1,retrieveSuccess,deleteCartSuccess,cartId3,multiAddress,emptyAddress,hasDefaultAddress, inAuditShopToken, impossibleToken;
    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        // 未创建过商铺的用户(shopId = -1)
        UnregisteredShopToken = jwtHelper.createToken(3L, "zyh", -1L, 2, 3600);
        // 正常顾客的用户(shopId == -1)
        userToken = jwtHelper.createToken(3L, "zyh", -100L, 2, 3600);
        userToken1 = jwtHelper.createToken(1L, "123", -100L, 2, 3600);
        retrieveSuccess =jwtHelper.createToken(13L, "123", -100L, 2, 3600);
        multiAddress = jwtHelper.createToken(44L, "123", -100L, 2, 3600);
        emptyAddress = jwtHelper.createToken(8L, "123", -100L, 2, 3600);
        hasDefaultAddress= jwtHelper.createToken(13L, "123", -100L, 2, 3600);
        deleteCartSuccess= jwtHelper.createToken(706L, "123", -100L, 2, 3600);
        cartId3= jwtHelper.createToken(5211L, "123", -100L, 2, 3600);
        // 平台管理人员(shopId = 0)
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        WrongUserToken = jwtHelper.createToken(-1L, "zyh", -100L, 2, 3600);
    }

    */
/* PUT API /carts/{id}      描述：买家修改购物车单个商品的数量或规格 *//*


    //修改成功
    @Test
    void testUpdateCartItemWhenItemIsExist() throws Exception {

        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(4840L);
        productPo.setPrice(114514L);
        productPo.setQuantity(15);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4840L))).thenReturn(ret);

        InternalReturnObject<ProductPo> ret1 = new InternalReturnObject<ProductPo>();
        ProductPo productPo2 =new ProductPo();
        productPo2.setId(4123L);
        productPo2.setPrice(114514L);
        productPo2.setQuantity(15);
        ret1.setData(productPo2);
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4123L))).thenReturn(ret1);

        CartItem cartItem=new CartItem();
        cartItem.setProductId(4123L);
        cartItem.setQuantity(10);
        String body=JacksonUtil.toJson(cartItem);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/carts/{id}",3L)
                        .header("authorization", cartId3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //商品不存在 （cartId有误）
    @Test
    void testUpdateCartItemWhenCartItemIsEmpty() throws Exception {
        CartItem cartItem=new CartItem();
        cartItem.setProductId(4123L);
        cartItem.setQuantity(10);
        String body=JacksonUtil.toJson(cartItem);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/carts/{id}",-1L)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    //规格有误 （productId有误）
    @Test
    void testUpdateCartItemWhenProductIdIsWrong() throws Exception {
        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(4840L);
        productPo.setPrice(114514L);
        productPo.setQuantity(15);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4840L))).thenReturn(ret);

        InternalReturnObject<ProductPo> ret2 = new InternalReturnObject<ProductPo>();
        ret2.setData(null);
        ret2.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        ret2.setErrmsg(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(-1L))).thenReturn(ret2);

        CartItem cartItem=new CartItem();
        cartItem.setProductId(-1L);
        cartItem.setQuantity(10);
        String body=JacksonUtil.toJson(cartItem);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/carts/{id}",3)
                        .header("authorization", cartId3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    //数量超过库存量
    @Test
    void testUpdateCartItemWhenQuantityExceed() throws Exception {

        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(4840L);
        productPo.setPrice(114514L);
        productPo.setQuantity(15);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4840L))).thenReturn(ret);

        InternalReturnObject<ProductPo> ret1 = new InternalReturnObject<ProductPo>();
        ProductPo productPo2 =new ProductPo();
        productPo2.setId(4123L);
        productPo2.setPrice(114514L);
        productPo2.setQuantity(15);
        ret1.setData(productPo2);
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4123L))).thenReturn(ret1);

        CartItem cartItem=new CartItem();
        cartItem.setProductId(4123L);
        cartItem.setQuantity(9999);
        String body=JacksonUtil.toJson(cartItem);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/carts/{id}",3)
                        .header("authorization", cartId3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_QUANTITYNOTALLOW.getErrNo())));
    }

     //DELETE API /carts/{id}      描述：买家删除购物车中的商品

    //删除成功

    @Test
    void testDeleteCartItemWhenItemIsExist() throws Exception {
        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(4163L);
        productPo.setPrice(114514L);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4163L))).thenReturn(ret);


        this.mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}",20)
                        .header("authorization", deleteCartSuccess)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //购物车商品删除成功，且redis命中
    @Test
    void testDeleteCartItemWhenRedis() throws Exception {

        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(706L);
        productPo.setPrice(114514L);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(706L))).thenReturn(ret);

        CartItem bo=new CartItem();
        bo.setId(20L);
        bo.setProductId(706L);
        bo.setQuantity(1);
        bo.setPrice(21226L);
        bo.setCreatorId(706L);
        bo.setCreatorName("孙小凡");
        bo.setGmtCreate(LocalDateTime.now());
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(bo);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", 20)
                        .header("authorization", deleteCartSuccess)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //商品不存在 (CArtId有误)
    @Test
    void testDeleteCartItemWhenItemIsNotExist() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}",-1)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }
    //试图删除非本人的购物车商品
    @Test
    void testDeleteCartItemWhenCartItemNotBelong() throws Exception {

        InternalReturnObject<ProductPo> ret = new InternalReturnObject<ProductPo>();
        ProductPo productPo =new ProductPo();
        productPo.setId(4163L);
        productPo.setPrice(114514L);
        ret.setData(productPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findProductById(Mockito.eq(4163L))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/carts/{id}", 20)
                        .header("authorization", userToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CARTNOBELONGTO.getErrNo())));
    }
    //POST API /addresses      描述：买家添加地址


    //正确添加
    @Test
    void testAddAddressWhenSuccess() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(2417L);
        regionPo.setName("人民北路");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2417L))).thenReturn(ret);

        InternalReturnObject<RegionPo> ret2 = new InternalReturnObject<RegionPo>();
        RegionPo regionPo2 =new RegionPo();
        regionPo2.setId(1L);
        regionPo2.setName("学武楼");
        ret2.setData(regionPo2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(1L))).thenReturn(ret2);


        AddressVo vo=new AddressVo();
        vo.setRegionId(1L);
        vo.setAddress("厦门大学666");
        vo.setConsignee("甘霖娘");
        vo.setMobile("13000000000");
        String body=JacksonUtil.toJson(vo);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                    .header("authorization",userToken1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("厦门大学666")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.consignee", is("甘霖娘")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mobile", is("13000000000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.beDefault", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.region.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.region.name", is("学武楼")));
    }

    //地址数量超过上限
   @Test
    void testAddAddressWhenAddressAmountExceed() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(67750L);
        regionPo.setName("人民北路");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(67750L))).thenReturn(ret);

        InternalReturnObject<RegionPo> ret2 = new InternalReturnObject<RegionPo>();
        RegionPo regionPo2 =new RegionPo();
        regionPo2.setId(2L);
        regionPo2.setName("学武楼");
        ret2.setData(regionPo2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2L))).thenReturn(ret2);


        AddressVo vo=new AddressVo();
        vo.setRegionId(2L);
        vo.setAddress("厦门大学666");
        vo.setConsignee("甘霖娘");
        vo.setMobile("13000000000");
        String body=JacksonUtil.toJson(vo);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .header("authorization",multiAddress)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADDRESS_OUTLIMIT.getErrNo())));
    }


    //顾客id不存在
    @Test
    void testAddAddressWhenCustomerIdIsNotExist() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(2417L);
        regionPo.setName("人民北路");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2417L))).thenReturn(ret);

        InternalReturnObject<RegionPo> ret2 = new InternalReturnObject<RegionPo>();
        RegionPo regionPo2 =new RegionPo();
        regionPo2.setId(3L);
        regionPo2.setName("学武楼");
        ret2.setData(regionPo2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(3L))).thenReturn(ret2);


        AddressVo vo=new AddressVo();
        vo.setRegionId(3L);
        vo.setAddress("厦门大学666");
        vo.setConsignee("甘霖娘");
        vo.setMobile("13000000000");
        String body=JacksonUtil.toJson(vo);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .header("authorization",WrongUserToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    //地区id不存在
    @Test
    void testAddAddressWhenRegionIdIsInvalid() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(2417L);
        regionPo.setName("人民北路");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2417L))).thenReturn(ret);

        InternalReturnObject<RegionPo> ret2 = new InternalReturnObject<RegionPo>();
        ret2.setData(null);
        ret2.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        ret2.setErrmsg(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(-1L))).thenReturn(ret2);


        AddressVo vo=new AddressVo();
        vo.setRegionId(-1L);
        vo.setAddress("厦门大学666");
        vo.setConsignee("甘霖娘");
        vo.setMobile("13000000000");
        String body=JacksonUtil.toJson(vo);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .header("authorization",userToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }
    //GET API /addresses      描述：买家查看收货地址列表


    //顾客正确查看收货地址列表
    @Test
    void testRetrieveAddressWhenSuccess() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.get("/addresses")
                        .header("authorization",retrieveSuccess)
                        .param("page","1")
                        .param("pageSize","5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '10')].address", hasItem("广场南路95号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '11')].address", hasItem("广场南路95号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '12')].address", hasItem("广场南路95号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '13')].address", hasItem("广场南路95号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(5)));
    }
    //当前没有收货地址
    @Test
    void testRetrieveAddressWhenaddressIsEmpty() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.get("/addresses")
                        .header("authorization",emptyAddress)
                        .param("page","1")
                        .param("pageSize","5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADDRESS_EMPTY.getErrNo())));
    }




    //POST API /addresses/{id}/default      描述：买家设置默认地址


    //成功设置默认地址
    @Test
    void testSetDefaultAddressWhenSuccess() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(264962L);
        regionPo.setName("广场南路95号");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(264962L))).thenReturn(ret);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses/{id}/default",10)
                        .header("authorization",hasDefaultAddress))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }



    //PUT API /addresses/{id}      描述：买家修改自己的地址信息
    //成功修改地址信息
    @Test
    void testChangeAddressInfoWhenSuccess() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(2417L);
        regionPo.setName("人民北路");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2417L))).thenReturn(ret);

        InternalReturnObject<RegionPo> ret2 = new InternalReturnObject<RegionPo>();
        RegionPo regionPo2 =new RegionPo();
        regionPo2.setId(10L);
        regionPo2.setName("厦门大学信息学院");
        ret2.setData(regionPo);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(10L))).thenReturn(ret2);

        Address address=new Address();
        address.setRegionId(10L);
        address.setAddress("厦门大学信息学院");
        address.setConsignee("zyh");
        address.setMobile("13000000000");
        String body=JacksonUtil.toJson(address);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/addresses/{id}",1)
                        .header("authorization",userToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //地址Id不存在
    @Test
    void testChangeAddressInfoWhenRegionIdIsNotExist() throws Exception{

        Address address=new Address();
        address.setRegionId(10L);
        address.setAddress("厦门大学信息学院");
        address.setConsignee("zyh");
        address.setMobile("13000000000");
        String body=JacksonUtil.toJson(address);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/addresses/{id}",-1)
                        .header("authorization",userToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    //地址信息有误
    @Test
    void testChangeAddressInfoWhenAddressIsWrong() throws Exception{

        Address address=new Address();
        address.setRegionId(10L);
        address.setAddress("厦门大学信息学院");
        address.setConsignee("zyh");
        String body= JacksonUtil.toJson(address);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/addresses/{id}",5)
                        .header("authorization",userToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FIELD_NOTVALID.getErrNo())));
    }
    //DELETE API /addresses/{id}      描述：删除地址

    //成功删除地址信息
    @Test
    void testDeleteAddressInfoWhenSuccess() throws Exception{

        InternalReturnObject<RegionPo> ret = new InternalReturnObject<RegionPo>();
        RegionPo regionPo =new RegionPo();
        regionPo.setId(2417L);
        regionPo.setName("大同街");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(2417L))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/addresses/{id}",1)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //地址Id不存在
    @Test
    void testDeleteAddressInfoWhenAddressIdIsNotExist() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/addresses/{id}",-1)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    //POST APi /couponactivities/{id}/coupons
    //顾客成功领取优惠券，活动type为每人限领多张，且当前没有该优惠券,且validTerm为0，且当前在领取优惠券的时间内

    @Test
    void testRecvCouponWhenSuccess1() throws Exception{

        //模拟第二次查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(3);
        po2.setQuantityType((byte) 0);
        po2.setValidTerm(0);
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));


        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //顾客成功领取优惠券，活动type为每人限领多张，且有该优惠券,但不满，且validTerm为0，且当前在领取优惠券的时间内
    @Test
    void testRecvCouponWhenSuccess2() throws Exception{

        //模拟第二次查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(3);
        po2.setQuantityType((byte) 0);
        po2.setValidTerm(0);
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));


        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    //顾客成功领取优惠券，活动type为每人限领多张，且没有该优惠券，且validTerm为4（>0），且当前在领取优惠券的时间内
    @Test
    void testRecvCouponWhenSuccess3() throws Exception{

        //模拟第二次查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(3);
        po2.setQuantityType((byte) 0);
        po2.setValidTerm(4);
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));


        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }


    //顾客成功领取优惠券，活动type为总量限制，1人一张，且没有该优惠券，且validTerm为0，且当前在领取优惠券的时间内，且redis中无信息，product中有库存但少于100张
    @Test
    void testRecvCouponWhenSuccess4() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(5);
        po2.setQuantityType((byte) 1);//活动type为总量限制，1人一张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        //模拟查询redis库存
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);

        //模拟更新product库存
        InternalReturnObject<ReturnObject> ret = new InternalReturnObject<ReturnObject>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        CouponActVo vo =new CouponActVo();
        Mockito.when(productMapper.updateCouponAct(Mockito.eq(1L),Mockito.eq(5L), Mockito.any(CouponActVo.class))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    //顾客成功领取优惠券，活动type为总量限制，1人一张，且没有该优惠券，且validTerm为0，且当前在领取优惠券的时间内，且redis中无信息，product中有库存但多于100张
    @Test
    void testRecvCouponWhenSuccess5() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(105);
        po2.setQuantityType((byte) 1);//活动type为总量限制，1人一张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        //模拟查询redis库存
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);

        //模拟更新product库存
        InternalReturnObject<ReturnObject> ret = new InternalReturnObject<ReturnObject>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        CouponActVo vo =new CouponActVo();
        Mockito.when(productMapper.updateCouponAct(Mockito.eq(1L),Mockito.eq(5L), Mockito.any(CouponActVo.class))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    //顾客成功领取优惠券，活动type为总量限制，1人一张，且没有该优惠券，且validTerm为0，且当前在领取优惠券的时间内，且redis中有信息
    @Test
    void testRecvCouponWhenSuccess6() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(105);
        po2.setQuantityType((byte) 1);//活动type为总量限制，1人一张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        //模拟查询redis库存
        CouponQuantity couponQuantity=new CouponQuantity(5);
        Mockito.when(redisUtil.get("Coupon_Quantity5")).thenReturn(couponQuantity);

        //模拟查询redis库存减少
        Mockito.when(redisUtil.decr("Coupon_Quantity5",1)).thenReturn(4L);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }


    //顾客领取优惠券失败，活动type为个人限领，1人多张，且有该优惠券，且已领满
    @Test
    void testRecvCouponFailedwhenType0AndExceed() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(1);//一人最多1张
        po2.setQuantityType((byte) 0);//活动type为个人限领，1人多张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_EXIST.getErrNo())));
    }

    //顾客领取优惠券失败，活动type为总量限，1人多张，且有该优惠券，且已领满
    @Test
    void testRecvCouponFailedwhenType1AndExceed() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(1);
        po2.setQuantityType((byte) 0);//活动type为个人限领，1人多张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_EXIST.getErrNo())));
    }


    //顾客领取优惠券失败，未到领取优惠券时间
    @Test
    void testRecvCouponFailedwhenTimeTooEarly() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(1);
        po2.setQuantityType((byte) 0);//活动type为个人限领，1人多张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().plusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_NOTBEGIN.getErrNo())));
    }

    //顾客领取优惠券失败，活动结束
    @Test
    void testRecvCouponFailedwhenTimeTooLate() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(1);
        po2.setQuantityType((byte) 0);//活动type为个人限领，1人多张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(3));
        po2.setCouponTime(LocalDateTime.now().minusDays(2).toString());
        po2.setEndTime(LocalDateTime.now().minusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_END.getErrNo())));
    }


//顾客失败领取优惠券，活动type为总量限制，1人领1张，但redis与库存均为0
    @Test
    void testRecvCouponFailedWhenFinish() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(0);
        po2.setQuantityType((byte) 1);//活动type为总量限制，1人一张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        //模拟查询redis库存
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);

        //模拟更新product库存
        InternalReturnObject<ReturnObject> ret = new InternalReturnObject<ReturnObject>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        CouponActVo vo =new CouponActVo();
        Mockito.when(productMapper.updateCouponAct(Mockito.eq(1L),Mockito.eq(5L), Mockito.any(CouponActVo.class))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_FINISH.getErrNo())));
    }


//顾客失败领取优惠券，查询活动失败，活动不存在
    @Test
    void testRecvCouponFailedWhenActIsNOtExist() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();

        ret2.setData(null);
        ret2.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        ret2.setErrmsg(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(-1L))).thenReturn(ret2);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",-1)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }


    //顾客失败领取优惠券，更新product模块库存出错
    @Test
    void testRecvCouponFailedWhenUpdateWrong() throws Exception{

        //模拟查询活动
        InternalReturnObject<CouponActPo> ret2 = new InternalReturnObject<CouponActPo>();
        CouponActPo po2 =new CouponActPo();
        po2.setId(5L);
        po2.setName("店铺zyh-test活动");
        po2.setShop(new IdNameTypeDto(1L,"zyh的小店",(byte)1));
        po2.setQuantity(1);
        po2.setQuantityType((byte) 1);//活动type为总量限制，1人一张
        po2.setValidTerm(0);//validTerm为0
        po2.setBeginTime(LocalDateTime.now().minusDays(2));
        po2.setCouponTime(LocalDateTime.now().minusDays(1).toString());
        po2.setEndTime(LocalDateTime.now().plusDays(1));
        po2.setCreator(new IdNameTypeDto(1L,"admin", (byte) 1));

        ret2.setData(po2);
        ret2.setErrno(ReturnNo.OK.getErrNo());
        ret2.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(productMapper.findCouponActByActIdAndShopId(Mockito.eq(0L),Mockito.eq(5L))).thenReturn(ret2);

        //模拟查询redis库存
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);

        //模拟更新product库存
        InternalReturnObject<ReturnObject> ret = new InternalReturnObject<ReturnObject>();
        ret.setErrno(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo());
        ret.setErrmsg(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage());
        CouponActVo vo =new CouponActVo();
        Mockito.when(productMapper.updateCouponAct(Mockito.eq(1L),Mockito.eq(5L), Mockito.any(CouponActVo.class))).thenReturn(ret);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/couponactivities/{id}/coupons",5)
                        .header("authorization",userToken1))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

}
*/
