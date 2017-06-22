package app.run;

import config.ServerConfig;
import controller.AccountController;
import org.springframework.boot.SpringApplication;

/**
 * Created by Fresher on 19/06/2017.
 */
public class Main {
    public static void main(String[] args) {
        ServerConfig.initConfig();
        SpringApplication.run(AccountController.class, args);
    }
}
