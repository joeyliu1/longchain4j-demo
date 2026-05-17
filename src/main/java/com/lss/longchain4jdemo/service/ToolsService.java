package com.lss.longchain4jdemo.service;

import com.lss.longchain4jdemo.entity.Order;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 航空客服工具服务
 * 为 AI Agent 提供工具调用接口
 * 内部调用 AirlineService 完成业务逻辑
 */
@Service
public class ToolsService {

    @Autowired
    private AirlineService airlineService;

    /**
     * 查询机票订单
     */
    @Tool("查询机票订单信息，需要提供订单号和乘客姓名")
    public String queryOrder(
            @P("订单号或预订号") String orderNo,
            @P("乘客姓名") String passengerName) {
        System.out.println("调用 queryOrder 工具，订单号：" + orderNo + ", 姓名：" + passengerName);

        Order order = airlineService.findByOrderNoAndName(orderNo, passengerName);
        if (order == null) {
            return "未找到订单号为 " + orderNo + " 的订单，请检查订单号和乘客姓名是否正确。";
        }

        return String.format(
                "【订单信息】\n" +
                "订单号：%s\n" +
                "乘客姓名：%s\n" +
                "航班号：%s\n" +
                "行程：%s → %s\n" +
                "起飞时间：%s\n" +
                "舱位：%s\n" +
                "订单状态：%s\n" +
                "支付金额：¥%.2f",
                order.getOrderNo(),
                order.getPassengerName(),
                order.getFlightNo(),
                order.getFromCity(),
                order.getToCity(),
                order.getDepartureTime(),
                order.getCabinClass(),
                order.getStatus(),
                order.getAmount()
        );
    }

    /**
     * 改签机票
     */
    @Tool("改签机票，需要提供订单号、乘客姓名、新航班号、新起飞时间")
    public String changeOrder(
            @P("订单号或预订号") String orderNo,
            @P("乘客姓名") String passengerName,
            @P("新航班号") String newFlightNo,
            @P("新起飞时间，格式如 2026-05-21 14:30") String newDepartureTime) {
        System.out.println("调用 changeOrder 工具，订单号：" + orderNo);

        Order order = airlineService.findByOrderNoAndName(orderNo, passengerName);
        if (order == null) {
            return "未找到订单号为 " + orderNo + " 的订单，无法办理改签。";
        }

        if (!"已出票".equals(order.getStatus())) {
            return "当前订单状态为【" + order.getStatus() + "】，无法办理改签。";
        }

        // 计算改签费
        double changeFee = airlineService.calculateChangeFee(order.getCabinClass());

        return String.format(
                "【改签确认】\n" +
                "订单号：%s\n" +
                "原航班：%s (%s %s)\n" +
                "新航班：%s (%s)\n" +
                "舱位：%s\n" +
                "改签费用：¥%.2f\n" +
                "请确认是否支付改签费用，支付后即可完成改签。",
                order.getOrderNo(),
                order.getFlightNo(),
                order.getFromCity() + " → " + order.getToCity(),
                order.getDepartureTime(),
                newFlightNo,
                newDepartureTime,
                order.getCabinClass(),
                changeFee
        );
    }

    /**
     * 取消机票订单
     */
    @Tool("取消机票订单，需要提供订单号和乘客姓名")
    public String cancelOrder(
            @P("订单号或预订号") String orderNo,
            @P("乘客姓名") String passengerName) {
        System.out.println("调用 cancelOrder 工具，订单号：" + orderNo);

        Order order = airlineService.findByOrderNoAndName(orderNo, passengerName);
        if (order == null) {
            return "未找到订单号为 " + orderNo + " 的订单，无法办理取消。";
        }

        if (!"已出票".equals(order.getStatus())) {
            return "当前订单状态为【" + order.getStatus() + "】，无法办理取消。";
        }

        // 计算取消费
        double cancelFee = airlineService.calculateCancelFee(order.getCabinClass(), order.getAmount());
        double refundAmount = order.getAmount() - cancelFee;

        return String.format(
                "【取消确认】\n" +
                "订单号：%s\n" +
                "航班：%s (%s → %s)\n" +
                "原支付金额：¥%.2f\n" +
                "取消费用：¥%.2f\n" +
                "应退金额：¥%.2f\n" +
                "退款将在 7 个工作日内原路退回，请确认是否继续取消。",
                order.getOrderNo(),
                order.getFlightNo(),
                order.getFromCity(),
                order.getToCity(),
                order.getAmount(),
                cancelFee,
                refundAmount
        );
    }

    /**
     * 查询上海天气
     */
    @Tool("查询上海的天气")
    public String shanghaiWeather() {
        System.out.println("调用了 shanghaiWeather");
        return "上海当前天气：晴，温度 25°C，适宜出行。";
    }
}
