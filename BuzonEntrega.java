import java.util.ArrayDeque;
import java.util.Queue;

public class BuzonEntrega {
    private final Queue<Mensaje> cola = new ArrayDeque<>();
    private final int capacidad;

    private boolean finActivo = false;
    private int copiasFin = 0;
    private Mensaje mensajeFin = null;

    public BuzonEntrega(int capacidad) { this.capacidad = capacidad; }

    public synchronized void depositar(Mensaje m) {
        if (finActivo && m.tipo != Tipo.FIN) return; // ignora nuevos correos tras FIN
        while (m.tipo != Tipo.FIN && cola.size() >= capacidad) {
            try { wait(); } catch (InterruptedException ignored) {}
        }
        if (m.tipo == Tipo.FIN && !finActivo) {
            finActivo = true;
            mensajeFin = m;
        } else if (m.tipo != Tipo.FIN) {
            cola.add(m);
        }
        notifyAll();
    }

    /** Entrega siguiente mensaje; si ya se activó FIN, reparte copias. Devuelve null si ya no hay nada. */
    public synchronized Mensaje extraer() {
        if (finActivo && copiasFin > 0) {
            copiasFin--;
            return mensajeFin;
        }
        while (cola.isEmpty()) {
            if (finActivo && copiasFin == 0) return null; // cola vacía y todas las copias de FIN entregadas
            try { wait(); } catch (InterruptedException ignored) {}
            if (finActivo && copiasFin > 0) {
                copiasFin--;
                return mensajeFin;
            }
        }
        Mensaje m = cola.remove();
        notifyAll();
        return m;
    }

    /** Activa la “barrera” (una copia de FIN por servidor). */
    public synchronized void prepararFin(int servidores) {
        if (finActivo) {
            copiasFin = servidores;
            notifyAll();
        }
    }

    public synchronized boolean vacio() { return cola.isEmpty(); }
}
