package config;

import com.google.gson.Gson;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class ServerConfig {
    public static String HOST_MONGO;
    public static Integer PORT_MONGO;
    public static String DB_NAME;
    public static String USERNAME_BITZERO_PREFIX;

    public static String BITZERO_SERVER_ADDRESS;
    public static Integer BITZERO_SERVER_PORT;

    public static long TIME_OUT_EXECUTE_TASK;

    public static Gson globalGson = new Gson();


    // refactor, config will run before AppContext run
    public static void initConfig() {
        ServerProperties properties = new ServerProperties();
        HOST_MONGO = properties.getString("host_mongo");
        PORT_MONGO = properties.getInt("port_mongo");
        DB_NAME = properties.getString("db_name");
        USERNAME_BITZERO_PREFIX= properties.getString("account_name_bitzero_prefix");
        BITZERO_SERVER_ADDRESS= properties.getString("bitzero_server_address");
        BITZERO_SERVER_PORT= properties.getInt("bitzero_server_port");
        TIME_OUT_EXECUTE_TASK= properties.getLong("timeout_execute_task");
    }


}
