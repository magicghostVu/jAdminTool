package globalAppContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Fresher on 20/06/2017.
 */
public class GlobalApplicationContextWrap {
    private ApplicationContext applicationContext;
    private static GlobalApplicationContextWrap ourInstance = new GlobalApplicationContextWrap();
    public static GlobalApplicationContextWrap getInstance() {
        return ourInstance;
    }
    private GlobalApplicationContextWrap() {
        this.applicationContext= new ClassPathXmlApplicationContext("bean.xml");

    }
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
