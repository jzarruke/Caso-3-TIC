import java.util.Random;
public class FiltroSpam extends Thread {
    private final BuzonEntrada entrada;
    private final Cuarentena cuarentena;
    private final BuzonEntrega entrega;
    private final Coordinador coord;
    private final int servidores;
    private final int filtros;
    private final Random rnd = new Random();
    public FiltroSpam(int id,
                      BuzonEntrada entrada,
                      Cuarentena cuarentena,
                      BuzonEntrega entrega,
                      Coordinador coord,
                      int servidores,
                      int filtros) {
        super("FiltroSpam-" + id);
        this.entrada = entrada;
        this.cuarentena = cuarentena;
        this.entrega = entrega;
        this.coord = coord;
        this.servidores = servidores;
        this.filtros = filtros;
    }
    @Override
    public void run() {
        while (true) {
            Mensaje m = entrada.extraer();

            if (m.tipo == Tipo.CORREO) {
                if (m.esSpam) {
                    int ms = 200 + rnd.nextInt(2000);
                    cuarentena.agregar(m, ms);
                    Thread.yield();
                } else {
                    publicarEnEntrega(m);
                }
            } else if (m.tipo == Tipo.FIN) {
                if (m.id.startsWith("FIN-FILTRO-")) {
                    break; 
                } else if (m.id.startsWith("FIN-CLIENTE-") || m.id.startsWith("FIN-CLIENTE")) {
                    coord.registrarFinCliente();
                    coord.intentarCerrar(servidores, filtros);
                    continue;
                } else {
                    coord.registrarFinCliente();
                    coord.intentarCerrar(servidores, filtros);
                    continue;
                }
            }
            coord.intentarCerrar(servidores, filtros);
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
