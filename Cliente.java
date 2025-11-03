import java.util.*;

public class Cliente extends Thread {
    int idCliente;
    int numMensajes;
    BuzonEntrada entrada;
    Coordinador coord;
    Random rnd;

    public Cliente(int idCliente, int numMensajes, BuzonEntrada entrada, Coordinador coord) {
        super("Cliente-" + idCliente);
        this.idCliente = idCliente;
        this.numMensajes = numMensajes;
        this.entrada = entrada;
        this.coord = coord;
        this.rnd = new Random();
    }

    public void run() {
        entrada.depositar(Mensaje.inicio(idCliente));
        for (int i = 1; i <= numMensajes; i++) {
            boolean spam = rnd.nextBoolean();
            Mensaje m = Mensaje.correo(idCliente, "C" + idCliente + "-M" + i, spam);
            entrada.depositar(m);
        }
        entrada.depositar(Mensaje.fin("CLIENTE-" + idCliente));
        coord.registrarFinCliente();
    }
}
