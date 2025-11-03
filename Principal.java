import java.util.*;

public class Principal {
    public static void main(String[] args) throws Exception {
        Configuracion cfg = new Configuracion("recursos/config.txt");
        int numClientes = cfg.getNumClientes();
        int mensajesPorCliente = cfg.getMensajesPorCliente();
        int numFiltros = cfg.getNumFiltros();
        int numServidores = cfg.getNumServidores();
        int capacidadEntrada = cfg.getCapacidadEntrada();
        int capacidadEntrega = cfg.getCapacidadEntrega();
        BuzonEntrada entrada = new BuzonEntrada(capacidadEntrada);
        Cuarentena cuarentena = new Cuarentena();
        BuzonEntrega entrega = new BuzonEntrega(capacidadEntrega);
        Coordinador coord = new Coordinador(numClientes, entrada, cuarentena, entrega);
        List<Thread> clientes = new ArrayList<>();
        for (int i = 0; i < numClientes; i++) clientes.add(new ClienteEmisor(i + 1, mensajesPorCliente, entrada, coord));
        List<Thread> filtros = new ArrayList<>();
        for (int i = 0; i < numFiltros; i++) filtros.add(new FiltroSpam(i + 1, entrada, cuarentena, entrega, coord, numServidores));
        AdministradorCuarentena admin = new AdministradorCuarentena(cuarentena, entrega);
        List<Thread> servidores = new ArrayList<>();
        for (int i = 0; i < numServidores; i++) servidores.add(new ServidorEntrega(i + 1, entrega));
        for (Thread t: clientes) t.start();
        for (Thread t: filtros) t.start();
        admin.start();
        for (Thread t: servidores) t.start();
        for (Thread t: clientes) t.join();
        for (Thread t: filtros) t.join();
        admin.join();
        for (Thread t: servidores) t.join();
        System.out.println("=== SISTEMA FINALIZADO ===");
        System.out.println("Buzón de entrada vacío: " + entrada.vacio());
        System.out.println("Cuarentena vacía: " + cuarentena.vacia());
        System.out.println("Buzón de entrega vacío: " + entrega.vacio());
    }
}
