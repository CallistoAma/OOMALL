package cn.edu.xmu.oomall.customer.controller;


import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.CustomerTestApplication;
import cn.edu.xmu.oomall.customer.controller.dto.SimpleOnsaleDto;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.customer.mapper.openfeign.po.ProductPo;
import cn.edu.xmu.oomall.customer.mapper.po.PagePo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;

@SpringBootTest(classes = CustomerTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class LyrControllerTest {
    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;

    private static String userToken;
    private static String userToken4;

    private static String userToken15054;

    private static String userToken16666;

    private static String adminToken;

    private static String userTokenNotExist;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        userToken =jwtHelper.createToken(5L,"lyr",-100L,2,3600);
        userToken4 =jwtHelper.createToken(4L,"lyr",-100L,2,3600);
        userToken15054=jwtHelper.createToken(15054L,"lyr",-100L,2,3600);
        userToken16666=jwtHelper.createToken(16666L,"lyr",-100L,2,3600);
        userTokenNotExist=jwtHelper.createToken(99999L,"lyr",-100L,2,3600);
        adminToken=jwtHelper.createToken(1L,"lyrAdmin",0L,1,3600);
    }

    /*DELETE 管理员查看逻辑删除顾客*/
    @Test
    void testAdminDeleteCustomer() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/customers/{id}","0","3")
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /*DELETE 管理员查看逻辑删除顾客,不能重复删除*/
    @Test
    void testAdminDeleteCustomerNotAdmit() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/customers/{id}","0","24655")
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CANNOTDUBILEDELETE.getErrNo())));
    }


    /*PUT 平台管理员封禁买家*/
    @Test
    void testAdminBanCustomer() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{did}/customers/{id}/ban",0,5)
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /*PUT 平台管理员封禁买家,不能被封禁，失败*/
    @Test
    void testAdminBanCustomerNotAllow() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{did}/customers/{id}/ban",0,24655)
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CANNOTBANNED.getErrNo())));
    }

    /*PUT 平台管理员解禁买家*/
    @Test
    void testAdminReleaseCustomer() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{did}/customers/{id}/release",0,24652)
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /*PUT 平台管理员解禁买家,不能被解禁，失败*/
    @Test
    void testAdminReleaseCustomerNotAllow() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{did}/customers/{id}/release",0,24655)
                        .header("authorization",adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CANNOTVAILD.getErrNo())));
    }

    /*GET 买家获得购物车列表,成功*/
    @Test
    void testCustomerGetPCartList()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/carts")
                        .header("authorization",userToken4)
                        .param("page","1")
                        .param("pageSize","5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    /*GET 买家获得购物车列表，找不到买家，失败*/
    @Test
    void testCustomerGetPCartListNotFound()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/carts")
                        .header("authorization",userTokenNotExist)
                        .param("page","1")
                        .param("pageSize","5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }


    /**
     *买家将商品加入购物车,成功
     * @throws Exception
     */

    @Test
    void testCustomerAddToCart() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(4);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)0);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,5,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);

        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     *买家将商品加入购物车,类型为团购,失败
     * @throws Exception
     */

    @Test
    void testCustomerAddToCartNotAllow2() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(4);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)2);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,5,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);

        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CARTNOTALLOW.getErrNo())));
    }

    /**
     *买家将商品加入购物车,类型为预售，失败
     * @throws Exception
     */

    @Test
    void testCustomerAddToCartNotAllow3() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(4);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)3);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,5,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);

        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_CARTNOTALLOW.getErrNo())));
    }


    /**
     *买家将商品加入购物车,购物车中没有对应商品且库存不足，失败
     * @throws Exception
     */

    @Test
    void testCustomerAddToCartNotInCartGreaterThanStock() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(6);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)0);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,5,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);

        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.GOODS_STOCK_SHORTAGE.getErrNo())));
    }

    /**
     *买家将商品加入购物车,购物车中有对应商品且库存不足，失败
     * @throws Exception
     */

    @Test
    void testCustomerAddToCartInCartGreaterThanStock() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(6);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)0);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,5,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);


        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken15054)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.GOODS_STOCK_SHORTAGE.getErrNo())));
    }

    /**
     *买家将商品加入购物车,购物车中有对应商品且库存足，成功
     * @throws Exception
     */
    @Test
    void testCustomerAddToCartInCartStockEnough() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(6);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)0);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,100,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);


        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken15054)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     *买家将商品加入购物车,购物车中没有对应商品，对应商品库存足，成功
     * @throws Exception
     */
    @Test
    void testCustomerAddToCartNotInCartStockEnough() throws Exception{
        //传入body
        CartItemVo cartItemVo=new CartItemVo();
        cartItemVo.setProductId(1552L);
        cartItemVo.setQuantity(6);

        //模拟productMapper.getAllOnsale的返回对象
        SimpleOnsaleDto simpleOnsaleDto=new SimpleOnsaleDto(1552L,550L, LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),(byte)0);
        List<SimpleOnsaleDto> list=new ArrayList<>();
        list.add(simpleOnsaleDto);
        PagePo<SimpleOnsaleDto> pagePo=new PagePo<>(list,1,100);
        InternalReturnObject<PagePo<SimpleOnsaleDto>> internalReturnObject=new InternalReturnObject<>(pagePo);

        //模拟productMapper.findProductById的返回对象
        ProductPo productPo=new ProductPo(1552L,100,550L);
        InternalReturnObject<ProductPo> internalReturnObject1 =new InternalReturnObject<>(productPo);


        Mockito.when(productMapper.getAllOnsale(0L,1552L,1,100)).thenReturn(internalReturnObject);
        Mockito.when(productMapper.findProductById(1552L)).thenReturn(internalReturnObject1);


        String body= JacksonUtil.toJson(cartItemVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .header("authorization",userToken16666)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }



    /*DELETE 买家清空购物车*/
    @Test
    void testCustomerClearGoods() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/carts")
                        .header("authorization",userToken4)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
