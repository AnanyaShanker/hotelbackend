package com.hotel.management.payment;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

   
    @AfterReturning(pointcut = "execution(* com.hotel.management.payment.PaymentService.processPayment(..))", returning = "result")
    public void logPaymentCompletion(JoinPoint jp, Object result) {
        System.out.println("[AOP] Payment processed successfully: " + result);
    }
}
