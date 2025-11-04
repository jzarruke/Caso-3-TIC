import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) throws Exception {
        String archivo = (args.length > 0) ? args[0] : "recursos/config.txt";
        Configuracion cfg = Configuracion.cargar(archivo);
        BuzonEntrada entrada = new BuzonEntrada(cfg.capEntrada);
        BuzonEntrega entrega = new BuzonEntrega(cfg.capEntrega);
        Cuarentena cuarentena = new Cuarentena();
        Coordinador coord = new Coordinador(cfg.clientes, entrada, cuarentena, entrega);
        List<Thread> hilos = new ArrayList<>();
        for (int i = 1; i <= cfg.clientes; i++) {
            hilos.add(new Cliente(i, cfg.correos, entrada));
        }

        for (int i = 1; i <= cfg.filtros; i++) {
            hilos.add(new FiltroSpam(i, entrada, cuarentena, entrega, coord,
                                     cfg.servidores, cfg.filtros));
        }
        hilos.add(new AdministradorCuarentena(cuarentena, entrega, coord,
                                              cfg.servidores, cfg.filtros));

        for (int i = 1; i <= cfg.servidores; i++) {
            hilos.add(new ServidorEntrega(i, entrega));
        }
        for (Thread t : hilos) t.start();
        for (Thread t : hilos) t.join();
        System.out.println(
            "Ejecución terminada. " +
            "Entrada vacía=" + entrada.vacio() +
            ", Entrega vacía=" + entrega.vacio() +
            ", Cuarentena vacía=" + cuarentena.vacia()
        );
    }
}
