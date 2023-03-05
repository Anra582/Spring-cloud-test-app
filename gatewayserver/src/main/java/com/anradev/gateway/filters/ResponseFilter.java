package com.anradev.gateway.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseFilter {
 
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
	FilterUtils filterUtils;
 
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
              HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
              String correlationId = filterUtils.getCorrelationId(requestHeaders);
              logger.debug("Adding the correlation id to the outbound headers. {}", correlationId);
              exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
              logger.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
          }));
    }
}
