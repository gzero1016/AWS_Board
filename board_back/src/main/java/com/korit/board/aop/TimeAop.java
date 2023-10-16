package com.korit.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

// Aop 필수 어노테이션 Aspect, Component
@Aspect
@Component
public class TimeAop {

    @Pointcut("@annotation(com.korit.board.aop.annotation.TimeAop)")
    private void pointCut(){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();  // <전처리>
        Object target = proceedingJoinPoint.proceed();
        stopWatch.stop();   // <후처리>
        System.out.println(stopWatch.getTotalTimeSeconds() + "초");

        return target;
    }
}
