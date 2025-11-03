import java.io.*;
import java.util.*;

public class Configuracion {
    private final Properties p = new Properties();

    public Configuracion(String ruta) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String s;
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.isEmpty() || s.startsWith("#")) continue;
                int k = s.indexOf('=');
                if (k > 0) p.setProperty(s.substring(0, k).trim(), s.substring(k + 1).trim());
            }
        }
    }

    public int getNumClientes() {
        return Integer.parseInt(p.getProperty("num_clientes"));
    }

    public int getMensajesPorCliente() {
        return Integer.parseInt(p.getProperty("mensajes_por_cliente"));
    }

    public int getNumFiltros() {
        return Integer.parseInt(p.getProperty("num_filtros"));
    }

    public int getNumServidores() {
        return Integer.parseInt(p.getProperty("num_servidores"));
    }

    public int getCapacidadEntrada() {
        return Integer.parseInt(p.getProperty("capacidad_entrada"));

    }

    public int getCapacidadEntrega() {
        return Integer.parseInt(p.getProperty("capacidad_entrega"));
    }
}
