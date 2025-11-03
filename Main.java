import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader("recursos/config.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.isEmpty() || s.startsWith("#")) continue;
                int k = s.indexOf('=');
                if (k > 0) p.setProperty(s.substring(0, k).trim(), s.substring(k + 1).trim());
            }
        }
        int numClientes = Integer.parseInt(p.getProperty("num_clientes"));
        int mensajesPorCliente = Integer.parseInt(p.getProperty("mensajes_por_cliente"));
        int numFiltros = Integer.parseInt(p.getProperty("num_filtros"));
        int numServidores = Integer.parseInt(p.getProperty("num_servidores"));
        int capacidadEntrada = Integer.parseInt(p.getProperty("capacidad_entrada"));
        int capacidadEntrega = Integer.parseInt(p.getProperty("capacidad_entrega"));
        BuzonEntrada entrada = new BuzonEntrada(capacidadEntrada);
        Cuarentena cuarentena = new Cuarentena();
        BuzonEntrega entrega = new BuzonEntrega(capacidadEntrega);
        Coordinador coord = new Coordinador(numClientes, entrada, cuarentena, entrega);
        List<Thread> clientes = new ArrayList<>();
        for (int i = 0; i < numClientes; i++) clientes.add(new Cliente(i + 1, mensajesPorCliente, entrada, coord));
        List<Thread> filtros = new ArrayList<>();
        for (int i = 0; i < numFiltros; i++) filtros.add(new FiltroSpam(i + 1, entrada, cuarentena, entrega, coord, numServidores));
        AdministradorCuarentena admin = new AdministradorCuarentena(cuarentena, entrega);
        List<Thread> servidores = new ArrayList<>();
        for (int i = 0; i < numServidores; i++)
            servidores.add(new ServidorEntrega(i + 1, entrega));
        for (Thread t: clientes) t.start();
        for (Thread t: filtros) t.start();
        admin.start();
        for (Thread t: servidores) t.start();
        for (Thread t: clientes) t.join();
        for (Thread t: filtros) t.join();
        admin.join();
        for (Thread t : servidores) t.join();
        System.out.println("=== SISTEMA FINALIZADO ===");
        System.out.println("Buzón de entrada vacío: " + entrada.vacio());
        System.out.println("Cuarentena vacía: " + cuarentena.vacia());
        System.out.println("Buzón de entrega vacío: " + entrega.vacio());
    }
}
