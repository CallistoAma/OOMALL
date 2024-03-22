package cn.edu.xmu.oomall.customer.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointCuts {

    @Pointcut("@annotation(cn.edu.xmu.oomall.customer.aop.CustomerAudit)")
    public void auditAnnotation(){

    }
}
