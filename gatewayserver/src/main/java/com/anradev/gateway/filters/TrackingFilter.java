package com.anradev.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {

	@Autowired
	FilterUtils filterUtils;

	@Autowired
	Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(requestHeaders)) {
			log.debug("correlation-id found in tracking filter: {}. ",
					filterUtils.getCorrelationId(requestHeaders));
		} else {
			String traceId = getCurrentTraceId();
			exchange = filterUtils.setCorrelationId(exchange, traceId);
			log.debug("correlation-id generated in tracking filter: {}.", traceId);
		}
		
		return chain.filter(exchange);
	}


	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		return filterUtils.getCorrelationId(requestHeaders) != null;
	}

	private String getCurrentTraceId() {
		String traceId = "";
		try {
			traceId = tracer.currentSpan().context().traceId();
		}
		catch (Exception e) {
			log.warn("correlation-id generated empty. Cannot get traceId of current Sleuth context: {}",
					e.getMessage());
		}
		return traceId;
	}

}