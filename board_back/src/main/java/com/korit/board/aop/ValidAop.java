package com.korit.board.aop;

import com.korit.board.exception.ValidException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.Map;

/* AOP는 필터와 같은 역할을 한다. */

@Aspect // Aop 는 무조건 달아줘야함
@Component // IoC 등록을 위해 필수
public class ValidAop {

//    @Pointcut("execution(* com.korit.board.controller.*.*(..))")
    @Pointcut("@annotation(com.korit.board.aop.annotation.ValidAop)")
    private void pointCut() {}

    @Around("pointCut()") /* 포인트컷 */
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {  //around 기본구성

        Object[] args = proceedingJoinPoint.getArgs();

        BeanPropertyBindingResult bindingResult = null;

        // .proceed() 메소드가 호출되기 < 전 처리 >
        for(Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }

        // bindingResult 가 null 경우
        if(bindingResult == null) {
            return proceedingJoinPoint.proceed();
        }

        // 몸통
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fe -> {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            });
            throw new ValidException(errorMap);
        }

        Object target = proceedingJoinPoint.proceed(); // 메소드 body < 호출 >
        // .proceed() 메소드가 호출되기 < 후 처리 >

        return target;
    }

}