import java.util.*;

public class FiltroSpam extends Thread {
    int idFiltro;
    int numServidores;
    BuzonEntrada entrada;
    Cuarentena cuarentena;
    BuzonEntrega entrega;
    Coordinador coord;
    Random rnd;

    public FiltroSpam(int idFiltro, BuzonEntrada entrada, Cuarentena cuarentena, BuzonEntrega entrega, Coordinador coord, int numServidores) {
        super("FiltroSpam-" + idFiltro);
        this.idFiltro = idFiltro;
        this.entrada = entrada;
        this.cuarentena = cuarentena;
        this.entrega = entrega;
        this.coord = coord;
        this.numServidores = numServidores;
        this.rnd = new Random();
    }

    public void run() {
        while (true) {
            Mensaje m = entrada.extraer();
            if (m.tipo == Tipo.INICIO) continue;
            if (m.tipo == Tipo.FIN) break;
            if (m.esSpam) {
                int duracion = 10000 + rnd.nextInt(10001);
                cuarentena.agregar(m, duracion);
            } else entrega.depositar(m);
            coord.intentarCerrar(numServidores);
        }
    }
}
