package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.customer.mapper.AddressNewMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@Repository
public class AddressDao {

    private Logger logger = LoggerFactory.getLogger(AddressDao.class);
    @Value("${oomall.customer.customer.timeout}")
    private long timeout;
    public static final String KEY = "Add%d";
    private AddressNewMapper addressMapper;
    private RegionDao regionDao;
    private RedisUtil redisUtil;
    @Autowired
    public AddressDao(AddressNewMapper addressMapper, RedisUtil redisUtil, RegionDao regionDao){
        this.redisUtil=redisUtil;
        this.addressMapper = addressMapper;
        this.regionDao=regionDao;
    }


    /**
     * 由po对象获得bo对象
     * 主要是设置bo对象中关联的dao对象，并把bo对象存在redis中
     * @param po po对象
     * @param redisKey redis的key，如果是null就不存redis
     * @return bo对象
     */
    private Address build(AddressPo po, String redisKey){
        logger.debug("build2");
        Address ret = CloneFactory.copy(new Address(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.build(ret);
        return ret;
    }

    private Address build(Address bo){
        logger.debug("build1");
        bo.setRegionDao(this.regionDao);
        bo.setAddressDao(this);
        bo.setRegion(this.regionDao.findById(bo.getRegionId()));
        return bo;
    }

    public List<Address> retrieveByCustomerId(Long customerId){
        assert(null != customerId):  new IllegalArgumentException();
        Pageable pageable = PageRequest.of(0, 20);
        List<AddressPo> pos = new ArrayList<AddressPo>();
        logger.debug("test");
        pos=this.addressMapper.findByCustomerId(customerId,pageable);
        /*Pageable pageable = PageRequest.of(0, MAX_RETURN, Sort.by("gmtCreate").descending());
          List<AddressPo> AddressPos =  this.addressMapper.findByCustomerIdEquals(customerId, pageable);*/
        return pos.stream().map(po -> {
            logger.debug("retrieveByCustomerId: po = {}",po);
            Address bo = this.build(po, null);
            return bo;
        }).collect(Collectors.toList());
    }

    public String save(Address address, UserDto userDto) throws RuntimeException{
        if (address.getId().equals(null)){
            throw new IllegalArgumentException("save: address id is null");
        }
        String key = String.format(KEY, address.getId());
        address.setModifier(userDto);
        address.setGmtModified(LocalDateTime.now());
        AddressPo po = CloneFactory.copy(new AddressPo(),address);
        po.setCustomerId(userDto.getId());
        logger.debug("save: po = {}", po);
        this.addressMapper.save(po);
        build(po, key); // 更新了数据，应该更新redis中缓存
        return key;
    }

    public Address insert(Address address, UserDto userDto) throws RuntimeException{
        address.setCreator(userDto);
        address.setGmtCreate(LocalDateTime.now());
        address.setBeDefault((byte) 0);
        AddressPo po = CloneFactory.copy(new AddressPo(), address);
        po.setId(null);
        po.setCustomerId(userDto.getId());
        logger.debug("insert: po = {}", po);
        this.addressMapper.save(po);
        /*Optional<AddressPo> po1=this.addressPoMapper.findById(1L);
        logger.debug("是否能查到？{}", po1.get().toString());*/
        address.setId(po.getId());
        address.setRegion(regionDao.findById(address.getRegionId()));
        return address;
    }

    public List<Address> findByCustomerId(Long id,Integer page,Integer pageSize) throws RuntimeException
    {
        List<Address> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        List<AddressPo> pos =  this.addressMapper.findByCustomerId(id,pageable);
        if(pos.size()==0)
        {
            throw new BusinessException(ReturnNo.ADDRESS_EMPTY, String.format(ReturnNo.ADDRESS_EMPTY.getMessage()));
        }
        ret = pos.stream().map(po -> CloneFactory.copy(new Address(),po)).collect(Collectors.toList());
        logger.info("bos size:{}", ret.size());
        return ret;
    }

    public Address findById(Long id,UserDto user){
        if (id.equals(null)) {
            throw new IllegalArgumentException("findById: id is null");
        }
        logger.debug("findObjById: id = {}",id);
        String key = String.format(KEY, id);
        Address address = (Address) redisUtil.get(key);
        if (!Objects.isNull(address)) {
            address = this.build(address);
        } else {

            Optional<AddressPo> ret = this.addressMapper.findById(id);
            if (ret.isPresent()){
                address = this.build(ret.get(), key);
            }else{
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "地址", id));
            }
        }
        if(!address.getCreatorId().equals(user.getId())){
            throw new BusinessException(ReturnNo.ADDRESS_NOTBELONGTO, String.format(ReturnNo.ADDRESS_NOTBELONGTO.getMessage()));
        }
        return address;
    }
    public Address findDefaultByCustomerId(Long id){
        Optional<AddressPo> ret= this.addressMapper.findByCustomerIdEqualsAndBeDefaultEquals(id, (byte) 1);
        return build(ret.get(),null);
    }

    public void delete(Long id){this.addressMapper.deleteById(id);}
}
