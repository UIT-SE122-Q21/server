package edu.uit.se122.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
