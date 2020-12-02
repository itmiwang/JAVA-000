package com.miwang.shardingsphere.mapper;

import com.miwang.shardingsphere.entity.POrder;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface POrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(POrder record);

    int insertSelective(POrder record);

    POrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrder record);

    int updateByPrimaryKey(POrder record);
    
    List<POrder> selectAll();
}