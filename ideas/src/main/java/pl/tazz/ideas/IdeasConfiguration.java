package pl.tazz.ideas;

import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@ConfigurationProperties(prefix = "ideas")

@Data
public class IdeasConfiguration {

    private String name;

    @Value("${paging.pageSize:3}")
    private int pagingPageSize;


}
