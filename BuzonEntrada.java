import java.util.ArrayDeque;
import java.util.Queue;

public class BuzonEntrada {
    private final Queue<Mensaje> cola = new ArrayDeque<>();
    private final int capacidad;

    public BuzonEntrada(int capacidad) { this.capacidad = capacidad; }

    public synchronized void depositar(Mensaje m) {
        while (cola.size() >= capacidad) {
            try { wait(); } catch (InterruptedException ignored) {}
        }
        cola.add(m);
        notifyAll();
    }

    public synchronized Mensaje extraer() {
        while (cola.isEmpty()) {
            try { wait(); } catch (InterruptedException ignored) {}
        }
        Mensaje m = cola.remove();
        notifyAll();
        return m;
    }

    public synchronized boolean vacio() { return cola.isEmpty(); }
}
