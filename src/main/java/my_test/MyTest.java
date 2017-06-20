package my_test;

import com.mongodb.client.MongoDatabase;
import config.ServerConfig;
import config.constant.AccountType;
import config.constant.CollectionName;
import db.MongoDatabaseWrap;
import globalAppContext.GlobalApplicationContextWrap;
import junit.framework.TestCase;
import model.UserDAOImpl;
import model.UserModel;
import org.junit.Test;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class MyTest extends TestCase {


    static {
        ServerConfig.initConfig();
    }

    @Test
    public void testGetConfig() {
        ServerConfig.initConfig();
        assertEquals(new Integer(27017), ServerConfig.PORT_MONGO);
        assertEquals("127.0.0.1", ServerConfig.HOST_MONGO);
        assertEquals("j_admin_tool", ServerConfig.DB_NAME);
    }

    @Test
    public void testGetMongoDataBase() {
        ServerConfig.initConfig();
        MongoDatabase database = GlobalApplicationContextWrap.getInstance().getApplicationContext()
                .getBean("mongo_database_wrap", MongoDatabaseWrap.class).getMongoDatabase();
        //database.createCollection(CollectionName.USER.name());
        assertTrue(database != null);
    }

    @Test
    public void testCollectionExist() {
        ServerConfig.initConfig();
        MongoDatabaseWrap wraper = GlobalApplicationContextWrap.getInstance().getApplicationContext()
                .getBean("mongo_database_wrap", MongoDatabaseWrap.class);
        System.out.println(CollectionName.USER.getColletionName());
    }

    @Test
    public void testGson() {
        UserModel userModel = new UserModel("phuvh", AccountType.ADMIN);
        String json = userModel.toJsonString();
        System.out.println(json);
    }

    @Test
    public void testGetUDAOImpl(){
        UserDAOImpl userDAO= GlobalApplicationContextWrap.getInstance()
                .getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);
        UserModel model=  userDAO.getUserModelByUserName("phuvh");
        model.setPasswordChanged(true);
        //assertTrue(model.save());
    }
    @Test
    public void testModelExist(){
        UserModel userModel = new UserModel("phuvh1", AccountType.ADMIN);
        UserDAOImpl userDAO= GlobalApplicationContextWrap.getInstance()
                .getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);
        assertTrue(!userDAO.modelExist(userModel));
    }



}
