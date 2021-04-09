package com.zirakzigil.protomongo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @see https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-defn
 * @author harry
 */
@Aspect
@Component
public class TrackDurationAspect {

	private static final Logger LOG = LoggerFactory.getLogger(TrackDurationAspect.class);

	/**
	 * Advice to execute for matching pointcut.
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.zirakzigil.protomongo.aop.TrackDuration)")
	public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {

		final long startTime = System.currentTimeMillis();
		final Object retVal = joinPoint.proceed();
		final long timeTaken = System.currentTimeMillis() - startTime;

		LOG.info("Execution duration by {} is {}ms.", joinPoint, timeTaken);

		return retVal;
	}

}
