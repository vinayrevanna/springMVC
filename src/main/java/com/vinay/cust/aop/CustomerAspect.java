package com.vinay.cust.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class CustomerAspect {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
    @Before("execution(* com.vinay.cust.controller.*.*(..))")
    public void before(JoinPoint joinPoint) {
        logger.info(" Allowed execution for {}", joinPoint.getSignature());
    }
    
//    @Before("execution(* com.vinay.cust.security.*.*(..))")
//    public void beforejwt(JoinPoint joinPoint) {
//        logger.info(" Allowed execution for {}", joinPoint.getSignature());
//    }
    
    @AfterThrowing(value = "execution(* com.vinay.cust.controller.*.*(..))")
    public void after(JoinPoint joinPoint) {
        
        logger.error("Oops! We have an Error. OK",joinPoint);
    }
    
    
}
