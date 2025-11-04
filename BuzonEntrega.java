import java.util.ArrayDeque;
import java.util.Queue;
public class BuzonEntrega {
    private final Queue<Mensaje> cola = new ArrayDeque<>();
    private final int capacidad;
    private boolean finActivo = false;
    private int copiasFin = 0;
    private Mensaje mensajeFin = null;
    public BuzonEntrega(int capacidad) {
        if (capacidad <= 0) throw new IllegalArgumentException("capEntrega debe ser > 0");
        this.capacidad = capacidad;
    }
    public synchronized void depositar(Mensaje m) {
        
        while (!finActivo && cola.size() >= capacidad) {
            try { wait(); } catch (InterruptedException ignored) {}
        }
        cola.add(m);
        notifyAll();
    }
    public synchronized Mensaje extraer() {
        while (cola.isEmpty()) {
            if (finActivo && copiasFin > 0) {
                copiasFin--;
                notifyAll();
                return mensajeFin;
            }
            try { wait(); } catch (InterruptedException ignored) {}
        }
        Mensaje m = cola.remove();
        notifyAll();
        return m;
    }
    public synchronized void prepararFin(int servidores) {
        if (!finActivo) {
            finActivo = true;
            mensajeFin = Mensaje.fin("ENTREGA");
        }
        copiasFin = Math.max(copiasFin, servidores); 
        notifyAll();
    }
    public synchronized boolean vacio() { return cola.isEmpty(); }
}
