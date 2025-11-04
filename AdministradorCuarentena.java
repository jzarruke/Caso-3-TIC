import java.util.List;
public class AdministradorCuarentena extends Thread {
    private final Cuarentena cuarentena;
    private final BuzonEntrega entrega;
    private final Coordinador coord;
    private final int servidores;
    private final int filtros;
    public AdministradorCuarentena(Cuarentena cuarentena,
                                   BuzonEntrega entrega,
                                   Coordinador coord,
                                   int servidores,
                                   int filtros) {
        super("AdministradorCuarentena");
        this.cuarentena = cuarentena;
        this.entrega = entrega;
        this.coord = coord;
        this.servidores = servidores;
        this.filtros = filtros;
    }
    @Override
    public void run() {
        while (true) {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            List<Mensaje> listos = cuarentena.revisar(200);
            for (Mensaje m : listos) publicarEnEntrega(m);
            coord.intentarCerrar(servidores, filtros);

            if (cuarentena.fin() && cuarentena.vacia()) break;
        }
        coord.intentarCerrar(servidores, filtros);
    }
    private void publicarEnEntrega(Mensaje m) {
        boolean hecho = false;
        while (!hecho) {
            try {
                entrega.depositar(m);
                hecho = true;
            } catch (Exception ignored) {
                try { Thread.sleep(20); } catch (InterruptedException ignored2) {}
            }
        }
    }
}
