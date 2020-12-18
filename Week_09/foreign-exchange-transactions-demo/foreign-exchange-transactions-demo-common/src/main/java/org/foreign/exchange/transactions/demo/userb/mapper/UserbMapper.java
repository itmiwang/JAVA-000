package org.foreign.exchange.transactions.demo.userb.mapper;

import org.apache.ibatis.annotations.Update;
import org.foreign.exchange.transactions.demo.userb.entity.AssetLog;

import java.math.BigDecimal;

/**
 * @author guozq
 * @date 2020-12-18-2:29 下午
 */
public interface UserbMapper {
    @Update("UPDATE `dollar` SET `asset` = `asset` + #{count} WHERE (`id` = #{id}); ")
    int updateDoller(BigDecimal count, Integer id);
    
    @Update("UPDATE `rmb` SET `asset` = `asset` + #{count} WHERE (`id` = #{id}); ")
    int updateRmb(BigDecimal count, Integer id);
    
    @Update("INSERT INTO `asset_log` ( `doller_asset`, `rmb_asset`, `status`, `create_time`,`update_time`)" +
            " VALUES (#{dollerAsset}, #{rmbAsset}, #{status}, #{createTime} , #{update_time}); ")
    int insertAssertLog(AssetLog assetLog);
    
    @Update("UPDATE `asset_log` SET `status` = #{status} WHERE (`id` = #{id}); ")
    int updateAssertLogStatus(Integer status, Integer id);
}
