import java.util.LinkedList;
import java.util.List;

public class Cuarentena {
    private final List<Mensaje> lista = new LinkedList<>();
    private boolean finRecibido = false;

    public synchronized void agregar(Mensaje m, int tiempoMs) {
        if (m.tipo == Tipo.FIN) { 
            finRecibido = true;
            notifyAll();
            return;
        }
        m.tiempoCuarentena = tiempoMs;
        lista.add(m);
        notifyAll();
    }
    public synchronized List<Mensaje> revisar(int deltaMs) {
        List<Mensaje> listos = new LinkedList<>();
        for (int i = 0; i < lista.size(); ) {
            Mensaje m = lista.get(i);
            m.tiempoCuarentena -= deltaMs;
            if (m.tiempoCuarentena <= 0) {
                listos.add(m);
                lista.remove(i);
            } else {
                i++;
            }
        }
        return listos;
    }

    public synchronized boolean vacia() { return lista.isEmpty(); }
    public synchronized boolean fin() { return finRecibido; }
}
