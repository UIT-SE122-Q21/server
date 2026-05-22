package edu.uit.se122.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.TimeZone;

@SpringBootApplication
/*@EnableRedisRepositories(
        basePackages = "edu.uit.se122.server.promotion.internal.repository",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)*/
/*@EnableJpaRepositories(
        basePackages = "edu.uit.se122.server",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ,
                pattern = "edu.uit.se122.server.promotion.internal.repository.redis.*"
        )
)*/
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
