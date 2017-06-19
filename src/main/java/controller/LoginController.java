package controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Fresher on 19/06/2017.
 */

@RestController
@EnableAutoConfiguration
public class LoginController  {
    @RequestMapping(value = "/home")
    public String home(){
        return "home";
    }



}
