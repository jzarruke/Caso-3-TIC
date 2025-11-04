import java.io.BufferedReader;
import java.io.FileReader;
public class Configuracion {
    public final int clientes, correos, filtros, servidores, capEntrada, capEntrega;
    public Configuracion(int c, int e, int f, int s, int ce, int cd) {
        this.clientes = c; this.correos = e; this.filtros = f;
        this.servidores = s; this.capEntrada = ce; this.capEntrega = cd;
    }
    public static Configuracion cargar(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int c  = Integer.parseInt(br.readLine().trim());
            int e  = Integer.parseInt(br.readLine().trim());
            int f  = Integer.parseInt(br.readLine().trim());
            int s  = Integer.parseInt(br.readLine().trim());
            int ce = Integer.parseInt(br.readLine().trim());
            int cd = Integer.parseInt(br.readLine().trim());
            return new Configuracion(c, e, f, s, ce, cd);
        }
    }
}
