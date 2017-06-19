package config;

/**
 * Created by magic_000 on 20/06/2017.
 */
public class ServerConfig {
    public static String HOST_MONGO;
    public static Integer PORT_MONGO;

    public static void initConfig(ServerProperties serverProperties){
        //HOST_MONGO=

        HOST_MONGO= serverProperties.getString("host_mongo");
        PORT_MONGO= serverProperties.getInt("port_mongo");
    }


}
