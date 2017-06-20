package config.constant;

/**
 * Created by Fresher on 20/06/2017.
 */
public enum AccountType {
    ADMIN(1),
    SUPER_ADMIN(0),
    NORMAL(2);

    int id;

    AccountType(int id) {
        this.id = id;
    }
}
