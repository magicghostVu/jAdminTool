package model;

import org.bson.Document;

/**
 * Created by Fresher on 20/06/2017.
 */
public abstract class AbstractModel {
    abstract public String toJsonString();
    abstract public Document toDocument();
}
