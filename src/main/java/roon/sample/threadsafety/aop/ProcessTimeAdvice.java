package roon.sample.threadsafety.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProcessTimeAdvice {

    @Around("@annotation(roon.sample.threadsafety.annotation.MeasureProcessTime)")
    public void calcProcessTime(ProceedingJoinPoint pjt) {
        long start = System.currentTimeMillis();

        try {
            pjt.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));
    }
}
