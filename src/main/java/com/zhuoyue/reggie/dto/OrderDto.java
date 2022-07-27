package com.zhuoyue.reggie.dto;

import com.zhuoyue.reggie.entity.OrderDetail;
import com.zhuoyue.reggie.entity.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto extends Orders {
   private List<OrderDetail>  orderDetails = new ArrayList<>();
}
