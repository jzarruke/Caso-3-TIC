import java.util.List;
public class AdministradorCuarentena extends Thread {
    private final Cuarentena cuarentena;
    private final BuzonEntrega entrega;

    public AdministradorCuarentena(Cuarentena cuarentena, BuzonEntrega entrega) {
        super("AdministradorCuarentena");
        this.cuarentena = cuarentena;
        this.entrega = entrega;
    }
    @Override
    public void run() {
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            List<Mensaje> listos = cuarentena.revisar(1000);
            for (Mensaje m : listos) publicarEnEntrega(m);
            if (cuarentena.fin() && cuarentena.vacia()) break;
        }
    }
    private void publicarEnEntrega(Mensaje m) {
        boolean hecho = false;
        while (!hecho) {
            try {
                entrega.depositar(m);
                hecho = true;
            } catch (Exception ignored) {}
            if (!hecho) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
        }
    }
}
