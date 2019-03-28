package ui;

public interface Observer<E extends Event> {
    void update(MuzicaEvent e);
}
