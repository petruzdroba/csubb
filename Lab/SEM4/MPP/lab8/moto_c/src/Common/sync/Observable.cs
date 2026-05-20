namespace moto_c.Common.sync;

public interface Observable
{
    void add(Observer o);

    void remove(Observer o);

    void notify(String message);
}