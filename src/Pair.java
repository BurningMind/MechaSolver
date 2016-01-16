import java.io.*;

public class Pair<A,B> implements Serializable {
    public final A a;
    public final B b;

    //Constructor
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
}
