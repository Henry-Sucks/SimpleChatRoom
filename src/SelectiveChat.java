import java.net.Socket;

public interface SelectiveChat{
    abstract void toEveryone();
    void toSelectOne();
    void toOneself();
}
