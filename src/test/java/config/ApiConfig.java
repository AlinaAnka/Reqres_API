package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:api.properties")
public interface ApiConfig extends Config {

    String login();
    String password();
    String baseUrl();

    @DefaultValue("/api")
    String basePath();
}
