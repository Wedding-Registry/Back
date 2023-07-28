package com.wedding.serviceapi.common.aop;

import com.wedding.serviceapi.auth.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ControllerLogAspect {


    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping() {}


    @Before("getMapping() || postMapping() || putMapping() || deleteMapping()")
    public void atWithin(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        LoginUserVo loginUserVo = (LoginUserVo) Arrays.stream(args).filter(arg -> arg instanceof LoginUserVo).findFirst().orElse(null);

        String controllerLog = "[" + joinPoint.getSignature().getName() + " controller]";

        if (loginUserVo != null) {
            controllerLog = controllerLog + " userId = {}, boardId = {}";
            log.info(controllerLog, loginUserVo.getUserId(), loginUserVo.getBoardsId());
        } else {
            log.info(controllerLog);
        }
    }
}
