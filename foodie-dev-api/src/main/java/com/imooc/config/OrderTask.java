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

    @Scheduled(cron = "${autoCloseOrder.task.timerTask}")
    public void autoCloseOrder(){

        ordersService.closeOrder();
        logger.info("正在执行超时未支付订单自动关闭定时任务！当前时间为：{}", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        System.out.println("正在执行超时未支付订单自动关闭定时任务！当前时间为：{}"+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
