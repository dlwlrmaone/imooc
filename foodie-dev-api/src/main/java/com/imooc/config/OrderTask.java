package com.imooc.config;

import com.imooc.service.OrdersService;
import com.imooc.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderTask.class);

    @Autowired
    private OrdersService ordersService;

    /**
     * 使用定时任务关闭超时未支付订单，存在的弊端
     * 1.存在时间差，程序不严谨
     * 10点39下单，11点检查不足一小时，12点检查超过一小时39分钟（假如一小时超时关闭订单）
     * 2.不支持集群
     * 如果使用集群后，会有多个定时任务，操作一个数据库
     * 解决方案：只使用一个节点来执行定时任务
     * 3.会对数据库进行全表扫描，影响数据库性能
     * 后续会使用消息队列来解决这些问题
     * 延时任务（队列）
     * 10点39下单，11点39检查，如果超时，直接关闭，不会出现问题（假如一小时超时关闭订单）
     */
    @Scheduled(cron = "${autoCloseOrder.task.timerTask}")
    public void autoCloseOrder(){

        ordersService.closeOrder();
        logger.info("正在执行超时未支付订单自动关闭定时任务！当前时间为：{}", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        System.out.println("正在执行超时未支付订单自动关闭定时任务！当前时间为：{}"+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
