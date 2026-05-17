package com.lss.longchain4jdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机票订单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    /**
     * 订单号/预订号
     */
    private String orderNo;
    
    /**
     * 乘客姓名
     */
    private String passengerName;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 出发城市
     */
    private String fromCity;
    
    /**
     * 到达城市
     */
    private String toCity;
    
    /**
     * 航班号
     */
    private String flightNo;
    
    /**
     * 舱位等级：经济舱/豪华经济舱/商务舱
     */
    private String cabinClass;
    
    /**
     * 起飞时间
     */
    private LocalDateTime departureTime;
    
    /**
     * 订单状态：已出票/已改签/已取消/已退款
     */
    private String status;
    
    /**
     * 支付金额
     */
    private Double amount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
