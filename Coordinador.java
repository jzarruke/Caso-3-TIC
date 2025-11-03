public class Coordinador {
    private final int totalClientes;
    private int finClientes = 0;

    private final BuzonEntrada entrada;
    private final Cuarentena cuarentena;
    private final BuzonEntrega entrega;

    private boolean finEntrega = false;
    private boolean finCuarentena = false;

    private final Evento posibleCierre = new Evento();

    public Coordinador(int totalClientes, BuzonEntrada entrada, Cuarentena cuarentena, BuzonEntrega entrega) {
        this.totalClientes = totalClientes;
        this.entrada = entrada;
        this.cuarentena = cuarentena;
        this.entrega = entrega;
    }

    public synchronized void registrarFinCliente() {
        finClientes++;
        if (finClientes >= totalClientes) posibleCierre.señalar();
    }
    public void intentarCerrar(int servidores) {
        synchronized (this) {
            boolean listo = (finClientes >= totalClientes) && entrada.vacio() && cuarentena.vacia();
            if (!listo) return;

            if (!finEntrega) {
                finEntrega = true;
                entrega.depositar(Mensaje.fin("ENTREGA"));
                entrega.prepararFin(servidores); 
            }
            if (!finCuarentena) {
                finCuarentena = true;
                cuarentena.agregar(Mensaje.fin("CUARENTENA"), 0);
            }
        }
    }
    public void esperarCierre() { posibleCierre.esperar(); }
}
