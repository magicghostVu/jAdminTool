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
import org.apache.log4j.Logger;
import org.junit.Test;
import utils.MD5Utils;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testSaveAndUpdateModel(){
        ServerConfig.initConfig();
        UserDAOImpl userDAO= GlobalApplicationContextWrap.getInstance()
                .getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);
        UserModel model=  userDAO.getUserModelByUserName("phuvh");
        //UserModel model = new UserModel("phuvh", AccountType.SUPER_ADMIN);
        model.setPasswordChanged(true);
        model.setPasswordHash("81dc9bdb52d04dc20036dbd8313ed055");
        model.setAccessToken("iuhgdfijhgkfdj");
        boolean res=model.save();
        assertTrue(res);
    }
    @Test
    public void testMD5(){
        String md5Hash= MD5Utils.hashStringToMD5("1234");
        assertEquals("81dc9bdb52d04dc20036dbd8313ed055", md5Hash);
    }


    public void testGetModelByAccTK(){
        ServerConfig.initConfig();
        UserDAOImpl userDAO= GlobalApplicationContextWrap.getInstance()
                .getApplicationContext().getBean("user_DAO_impl", UserDAOImpl.class);
        UserModel model= userDAO.getModelByAccessToken("0018a292c81f4fba44125358a35b7dc1");
        assertTrue(model!=null);
    }


    public void testMap(){
        Map<Integer, Integer> mapTest= new HashMap<>();
        Integer res= mapTest.get(1);
        assertTrue(res==null);
    }

    public void testLog4j(){
        Logger logger= Logger.getLogger("FileLogServer");
        logger.error("This is message from log4j");

    }


}
