package my_test;

import config.ServerConfig;
import config.ServerProperties;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class MyTest extends TestCase {
    public void testGetConfig(){

        ApplicationContext applicationContext= new ClassPathXmlApplicationContext("bean.xml");
        ServerProperties serverProperties= applicationContext.getBean("server-property", ServerProperties.class);

        ServerConfig.initConfig(serverProperties);


        assertEquals(new Integer(27017), ServerConfig.PORT_MONGO);
        assertEquals("127.0.0.1", ServerConfig.HOST_MONGO);


    }


}
