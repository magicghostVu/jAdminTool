package utils;

/**
 * Created by Fresher on 21/06/2017.
 */
public class MyPair<U, V> {
    private U _1;
    private V _2;
    public MyPair(U _1, V _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public U get_1() {
        return _1;
    }

    public V get_2() {
        return _2;
    }
}
