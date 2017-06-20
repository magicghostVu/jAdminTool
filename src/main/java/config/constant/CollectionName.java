package config.constant;

/**
 * Created by Fresher on 20/06/2017.
 */
public enum CollectionName {


    ACTION("action"),
    USER("user");

    private String colletionName;

    CollectionName(String name){
        this.colletionName= name;
    }


    public String getColletionName() {
        return colletionName;
    }
}
