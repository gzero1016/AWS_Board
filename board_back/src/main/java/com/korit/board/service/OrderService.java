package com.korit.board.service;

import com.korit.board.dto.OrderReqDto;
import com.korit.board.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    public boolean order(OrderReqDto orderReqDto) {
        return orderMapper.saveOrder(orderReqDto.toOrderEntity()) > 0;
    }
}
