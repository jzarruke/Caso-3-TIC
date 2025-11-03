import java.util.*;

public class ServidorEntrega extends Thread {
    int idServidor;
    BuzonEntrega entrega;
    Random rnd;

    public ServidorEntrega(int idServidor, BuzonEntrega entrega) {
        super("ServidorEntrega-" + idServidor);
        this.idServidor = idServidor;
        this.entrega = entrega;
        this.rnd = new Random();
    }

    public void run() {
        while (true) {
            Mensaje m = entrega.extraer();
            if (m == null) break;
            if (m.tipo == Tipo.FIN) break;
            try {
                Thread.sleep(200 + rnd.nextInt(400));
            } catch (InterruptedException ignored) {}
        }
    }
}
