package cn.edu.xmu.oomall.payment.mapper.generator;

import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface RefundTransPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @Delete({
        "delete from payment_refund_trans",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @Insert({
        "insert into payment_refund_trans (`out_no`, `trans_no`, ",
        "`amount`, `status`, `success_time`, ",
        "`adjust_id`, `adjust_name`, ",
        "`adjust_time`, `user_received_account`, ",
        "`pay_trans_id`, `shop_channel_id`, ",
        "`creator_id`, `creator_name`, ",
        "`modifier_id`, `modifier_name`, ",
        "`gmt_create`, `gmt_modified`, ",
        "`div_amount`, `shop_id`, ",
        "`ledger_id`)",
        "values (#{outNo,jdbcType=VARCHAR}, #{transNo,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=BIGINT}, #{status,jdbcType=TINYINT}, #{successTime,jdbcType=TIMESTAMP}, ",
        "#{adjustId,jdbcType=BIGINT}, #{adjustName,jdbcType=VARCHAR}, ",
        "#{adjustTime,jdbcType=TIMESTAMP}, #{userReceivedAccount,jdbcType=VARCHAR}, ",
        "#{payTransId,jdbcType=BIGINT}, #{shopChannelId,jdbcType=BIGINT}, ",
        "#{creatorId,jdbcType=BIGINT}, #{creatorName,jdbcType=VARCHAR}, ",
        "#{modifierId,jdbcType=BIGINT}, #{modifierName,jdbcType=VARCHAR}, ",
        "#{gmtCreate,jdbcType=TIMESTAMP}, #{gmtModified,jdbcType=TIMESTAMP}, ",
        "#{divAmount,jdbcType=BIGINT}, #{shopId,jdbcType=BIGINT}, ",
        "#{ledgerId,jdbcType=BIGINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(RefundTransPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @InsertProvider(type=RefundTransPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(RefundTransPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @SelectProvider(type=RefundTransPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="adjust_id", property="adjustId", jdbcType=JdbcType.BIGINT),
        @Result(column="adjust_name", property="adjustName", jdbcType=JdbcType.VARCHAR),
        @Result(column="adjust_time", property="adjustTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="user_received_account", property="userReceivedAccount", jdbcType=JdbcType.VARCHAR),
        @Result(column="pay_trans_id", property="payTransId", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_channel_id", property="shopChannelId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="div_amount", property="divAmount", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="ledger_id", property="ledgerId", jdbcType=JdbcType.BIGINT)
    })
    List<RefundTransPo> selectByExample(RefundTransPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "`id`, `out_no`, `trans_no`, `amount`, `status`, `success_time`, `adjust_id`, ",
        "`adjust_name`, `adjust_time`, `user_received_account`, `pay_trans_id`, `shop_channel_id`, ",
        "`creator_id`, `creator_name`, `modifier_id`, `modifier_name`, `gmt_create`, ",
        "`gmt_modified`, `div_amount`, `shop_id`, `ledger_id`",
        "from payment_refund_trans",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="adjust_id", property="adjustId", jdbcType=JdbcType.BIGINT),
        @Result(column="adjust_name", property="adjustName", jdbcType=JdbcType.VARCHAR),
        @Result(column="adjust_time", property="adjustTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="user_received_account", property="userReceivedAccount", jdbcType=JdbcType.VARCHAR),
        @Result(column="pay_trans_id", property="payTransId", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_channel_id", property="shopChannelId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="div_amount", property="divAmount", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="ledger_id", property="ledgerId", jdbcType=JdbcType.BIGINT)
    })
    RefundTransPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RefundTransPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") RefundTransPo row, @Param("example") RefundTransPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RefundTransPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") RefundTransPo row, @Param("example") RefundTransPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RefundTransPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(RefundTransPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    @Update({
        "update payment_refund_trans",
        "set `out_no` = #{outNo,jdbcType=VARCHAR},",
          "`trans_no` = #{transNo,jdbcType=VARCHAR},",
          "`amount` = #{amount,jdbcType=BIGINT},",
          "`status` = #{status,jdbcType=TINYINT},",
          "`success_time` = #{successTime,jdbcType=TIMESTAMP},",
          "`adjust_id` = #{adjustId,jdbcType=BIGINT},",
          "`adjust_name` = #{adjustName,jdbcType=VARCHAR},",
          "`adjust_time` = #{adjustTime,jdbcType=TIMESTAMP},",
          "`user_received_account` = #{userReceivedAccount,jdbcType=VARCHAR},",
          "`pay_trans_id` = #{payTransId,jdbcType=BIGINT},",
          "`shop_channel_id` = #{shopChannelId,jdbcType=BIGINT},",
          "`creator_id` = #{creatorId,jdbcType=BIGINT},",
          "`creator_name` = #{creatorName,jdbcType=VARCHAR},",
          "`modifier_id` = #{modifierId,jdbcType=BIGINT},",
          "`modifier_name` = #{modifierName,jdbcType=VARCHAR},",
          "`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP},",
          "`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP},",
          "`div_amount` = #{divAmount,jdbcType=BIGINT},",
          "`shop_id` = #{shopId,jdbcType=BIGINT},",
          "`ledger_id` = #{ledgerId,jdbcType=BIGINT}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(RefundTransPo row);
}