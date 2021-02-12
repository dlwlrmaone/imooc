package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * AOP通知分类：前置，后置，环绕，异常，最终
     */

    /**
     * AOP切面表达式
     * execution：代表所要执行的表达式主体
     * 第一处：* 代表方法返回类型
     * 第二处：包名（com.imooc.service.impl）代表aop监控类所在的包
     * 第三处：.. 代表该包及其子包下的所有类方法
     * 第四处：* 代表类名
     * 第五处：*（..）*代表类中的方法名，（..）代表方法中的任何参数
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint point) throws Throwable {

        //依次输入类名，方法名
        logger.info("===== {}.{}方法开始执行 =====", point.getTarget().getClass(),point.getSignature().getName());
        long beginTime = System.currentTimeMillis();
        //执行目标Service
        Object proceed = point.proceed();
        long endTime = System.currentTimeMillis();
        long workTime = endTime - beginTime;

        if (workTime > 3000){
            logger.error("===== {}.{}方法执行结束，耗时:{} 毫秒 =====",point.getTarget().getClass(),point.getSignature().getName(),workTime);
        }else if (workTime > 2000){
            logger.warn("===== {}.{}方法执行结束，耗时:{} 毫秒 =====",point.getTarget().getClass(),point.getSignature().getName(),workTime);
        }else {
            logger.info("===== {}.{}方法执行结束，耗时:{} 毫秒 =====",point.getTarget().getClass(),point.getSignature().getName(),workTime);
        }

        return proceed;
    }
}
