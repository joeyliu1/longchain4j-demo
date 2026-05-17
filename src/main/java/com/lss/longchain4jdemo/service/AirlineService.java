package com.lss.longchain4jdemo.service;

import com.lss.longchain4jdemo.entity.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 航空客服业务服务
 * 提供机票订单的核心业务逻辑
 */
@Service
public class AirlineService {

    // 模拟订单数据库
    private static final Map<String, Order> ORDER_DATABASE = new ConcurrentHashMap<>();

    static {
        // 初始化测试数据
        initTestData();
    }

    private static void initTestData() {
        ORDER_DATABASE.put("CN123456", Order.builder()
                .orderNo("CN123456")
                .passengerName("刘德华")
                .idCard("440300198001011234")
                .fromCity("上海")
                .toCity("北京")
                .flightNo("CA1501")
                .cabinClass("经济舱")
                .departureTime(LocalDateTime.of(2026, 5, 20, 10, 30))
                .status("已出票")
                .amount(1200.0)
                .createTime(LocalDateTime.now())
                .build());

        ORDER_DATABASE.put("CN789012", Order.builder()
                .orderNo("CN789012")
                .passengerName("张学友")
                .idCard("440300197505055678")
                .fromCity("广州")
                .toCity("成都")
                .flightNo("3U8888")
                .cabinClass("商务舱")
                .departureTime(LocalDateTime.of(2026, 5, 22, 14, 0))
                .status("已出票")
                .amount(3500.0)
                .createTime(LocalDateTime.now())
                .build());

        ORDER_DATABASE.put("CN345678", Order.builder()
                .orderNo("CN345678")
                .passengerName("郭富城")
                .idCard("440300198508089012")
                .fromCity("深圳")
                .toCity("杭州")
                .flightNo("ZH9876")
                .cabinClass("豪华经济舱")
                .departureTime(LocalDateTime.of(2026, 5, 25, 18, 30))
                .status("已出票")
                .amount(1800.0)
                .createTime(LocalDateTime.now())
                .build());
    }

    /**
     * 根据订单号和姓名查询订单
     */
    public Order findByOrderNoAndName(String orderNo, String passengerName) {
        Order order = ORDER_DATABASE.get(orderNo);
        if (order == null) {
            return null;
        }
        if (!order.getPassengerName().equals(passengerName)) {
            return null;
        }
        return order;
    }

    /**
     * 根据订单号查询订单（不验证姓名）
     */
    public Order findByOrderNo(String orderNo) {
        return ORDER_DATABASE.get(orderNo);
    }

    /**
     * 创建新订单
     */
    public Order createOrder(Order order) {
        ORDER_DATABASE.put(order.getOrderNo(), order);
        return order;
    }

    /**
     * 更新订单状态
     */
    public Order updateOrderStatus(String orderNo, String status) {
        Order order = ORDER_DATABASE.get(orderNo);
        if (order != null) {
            order.setStatus(status);
        }
        return order;
    }

    /**
     * 改签订单
     */
    public Order changeOrder(String orderNo, String newFlightNo, LocalDateTime newDepartureTime) {
        Order order = ORDER_DATABASE.get(orderNo);
        if (order != null) {
            order.setFlightNo(newFlightNo);
            order.setDepartureTime(newDepartureTime);
            order.setStatus("已改签");
        }
        return order;
    }

    /**
     * 取消订单
     */
    public Order cancelOrder(String orderNo) {
        Order order = ORDER_DATABASE.get(orderNo);
        if (order != null) {
            order.setStatus("已取消");
        }
        return order;
    }

    /**
     * 计算改签费用
     */
    public double calculateChangeFee(String cabinClass) {
        switch (cabinClass) {
            case "商务舱":
                return 0.0;
            case "豪华经济舱":
                return 30.0;
            case "经济舱":
            default:
                return 50.0;
        }
    }

    /**
     * 计算取消费用
     */
    public double calculateCancelFee(String cabinClass, double amount) {
        switch (cabinClass) {
            case "商务舱":
                return 25.0;
            case "豪华经济舱":
                return 50.0;
            case "经济舱":
            default:
                return 75.0;
        }
    }

    /**
     * 获取所有订单（用于测试）
     */
    public List<Order> findAllOrders() {
        return new ArrayList<>(ORDER_DATABASE.values());
    }
}
