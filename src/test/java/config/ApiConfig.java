package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:api.properties")
public interface ApiConfig extends Config {

    @Key("login")
    String login();

    @Key("password")
    String password();

    @Key("baseUrl")
    String baseUrl();
}
