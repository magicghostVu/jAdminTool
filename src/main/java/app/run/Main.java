package app.run;

import config.ServerConfig;
import controller.AccountController;
import controller.InfoController;
import org.springframework.boot.SpringApplication;

/**
 * Created by Fresher on 19/06/2017.
 */
public class Main {
    public static void main(String[] args) {
        ServerConfig.initConfig();
        Class[] allController= {AccountController.class, InfoController.class};
        SpringApplication.run(allController, args);
    }
}
