package hello.hello_spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {
    // 하위 모두 적용
    @Around("execution(* hello.hello_spring..*(..))\n")
//    @Around("execution(* hello.hello_spring.service..*(..))\n") // 서비스만 진행하는 경우
    public Object excute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try{
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish -start;
            System.out.println("END: " + joinPoint.toString() +" "+ timeMs + "ms");
        }
    }
}