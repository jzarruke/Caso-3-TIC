public class Coordinador {
    private final int totalClientes;
    private int finClientes = 0;

    private final BuzonEntrada entrada;
    private final Cuarentena cuarentena;
    private final BuzonEntrega entrega;

    private boolean finEntrega = false;
    private boolean finCuarentena = false;
    private boolean finFiltros = false;

    public Coordinador(int totalClientes,
                       BuzonEntrada entrada,
                       Cuarentena cuarentena,
                       BuzonEntrega entrega) {
        this.totalClientes = totalClientes;
        this.entrada = entrada;
        this.cuarentena = cuarentena;
        this.entrega = entrega;
    }

    public synchronized void registrarFinCliente() {
        finClientes++;
        notifyAll();
    }
    public void intentarCerrar(int servidores, int filtros) {
        synchronized (this) {
            boolean listo = (finClientes >= totalClientes
                           && entrada.vacio()
                           && cuarentena.vacia());
            if (!listo) return;

            if (!finEntrega) {
                finEntrega = true;
                entrega.prepararFin(servidores);
            }
            if (!finCuarentena) {
                finCuarentena = true;
                cuarentena.agregar(Mensaje.fin("CUARENTENA"), 0);
            }
            if (!finFiltros) {
                finFiltros = true;
                for (int i = 0; i < filtros; i++) {
                    
                    entrada.depositar(Mensaje.fin("FILTRO-" + i));
                }
            }
        }
    }
}
