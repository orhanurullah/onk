package com.onk.aspect;

import com.onk.component.MessageService;
import com.onk.core.results.DataResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ManagerAspect {

    private final Logger logger = LoggerFactory.getLogger(ManagerAspect.class);
    private final MessageService messageService;

    public ManagerAspect(MessageService messageService) {
        this.messageService = messageService;
    }

    @Around("execution(* com.onk.serviceImpl.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(messageService.getMessage("log.work.method", new Object[]{joinPoint.getSignature().getName()}));
        var data = joinPoint.proceed();
        logger.info(messageService.getMessage("log.over.method", new Object[]{joinPoint.getSignature().getName()}));
        return data;
    }
    @Around("execution(* com.onk.security.auth.SecurityManager.getCurrentUser(..))")
    public Object securityManagerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(messageService.getMessage("log.securityManager.access.true", null));
        return joinPoint.proceed();
    }
    @AfterReturning(pointcut = "execution(* com.onk.serviceImpl.*.*(..))",
            returning = "result")
    public void logMessage(JoinPoint joinPoint, Object result){
        if (result instanceof DataResult<?> dataResult) {
            String message = dataResult.getMessage();
            logger.info(messageService.getMessage("log.afterReturning",
                    new Object[]{joinPoint.getSignature().getName(), message}));
        }
    }

    @AfterThrowing(pointcut = "execution(* com.onk.*.*.*(..))",
            throwing = "exResult")
    public void logErrorMessage(JoinPoint joinPoint, Exception exResult){
        String message = exResult.getMessage();
        logger.error(messageService.getMessage("log.afterThrowing",
                new Object[]{joinPoint.getSignature().getName(), message}));
    }

}
