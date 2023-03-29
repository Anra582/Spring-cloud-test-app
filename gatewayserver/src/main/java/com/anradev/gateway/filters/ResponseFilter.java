package com.anradev.gateway.filters;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ResponseFilter {

    @Autowired
	FilterUtils filterUtils;
 
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
              HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
              String correlationId = filterUtils.getCorrelationId(requestHeaders);
              log.debug("Adding the correlation id to the outbound headers. {}", correlationId);
              exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
              log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
          }));
    }
}
