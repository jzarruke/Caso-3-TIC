import java.util.Random;

public class Cliente extends Thread {
    private final int id;
    private final int correos;
    private final BuzonEntrada entrada;
    private final Random rnd = new Random();

    public Cliente(int id, int correos, BuzonEntrada entrada) {
        super("Cliente-" + id);
        this.id = id;
        this.correos = correos;
        this.entrada = entrada;
    }

    @Override
    public void run() {
        entrada.depositar(Mensaje.inicio(id));
        for (int i = 1; i <= correos; i++) {
            boolean spam = rnd.nextBoolean();
            entrada.depositar(Mensaje.correo(id, id + "-" + i, spam));
        }
        entrada.depositar(Mensaje.fin("CLIENTE-" + id));
    }
}
