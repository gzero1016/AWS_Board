package com.korit.board.repository;

import com.korit.board.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    public int saveOrder(Order order);
}
