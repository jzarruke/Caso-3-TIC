public class ServidorEntrega extends Thread {
    private final BuzonEntrega entrega;
    public ServidorEntrega(int id, BuzonEntrega entrega) {
        super("ServidorEntrega-" + id);
        this.entrega = entrega;
    }
    @Override
    public void run() {
        while (true) {
            Mensaje m = entrega.extraer();
            if (m.tipo == Tipo.FIN) break;
            try { Thread.sleep(50 + (int)(Math.random()*100)); }
            catch (InterruptedException ignored) {}
        }
    }
}
