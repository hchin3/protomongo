package com.zirakzigil.protomongo.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @see https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-defn
 * @author harry
 */
@Aspect
@Component
public class LogAspect {

	private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

	/**
	 * Pointcut for anything in <code>com.zirakzigil.protomongo.service</code>
	 * package.
	 */
	@Pointcut("within(com.zirakzigil.protomongo.service..*)")
	public void servicePackagePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Advice to execute for matching pointcut after returning.
	 * 
	 * @param joinPoint
	 * @param retVal
	 */
	@AfterReturning(pointcut = "servicePackagePointcut()", returning = "retVal")
	public void logAfterReturning(final JoinPoint joinPoint, Object retVal) {
		LOG.info("{}.{}() returns {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), retVal);
	}

	/**
	 * Advice to execute for matching pointcut after throwing an exception.
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "servicePackagePointcut()", throwing = "e")
	public void logAfterThrowing(final JoinPoint joinPoint, final Throwable e) {
		LOG.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	/**
	 * Advice to execute around for matching pointcut.
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("servicePackagePointcut()")
	public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {

		if (LOG.isDebugEnabled()) {
			LOG.debug("@Around: Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}

		try {
			final Object retVal = joinPoint.proceed();
			if (LOG.isDebugEnabled()) {
				LOG.debug("@Around: Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
						joinPoint.getSignature().getName(), retVal);
			}
			return retVal;
		} catch (IllegalArgumentException e) {
			LOG.error("@Around: Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}

	}

}
