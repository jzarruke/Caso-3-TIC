public class Mensaje {
    public final Tipo tipo;
    public final int idCliente;
    public final String id;
    public final boolean esSpam;
    public int tiempoCuarentena; 

    public Mensaje(Tipo t, int c, String id, boolean s, int q) {
        this.tipo = t;
        this.idCliente = c;
        this.id = id;
        this.esSpam = s;
        this.tiempoCuarentena = q;
    }

    public static Mensaje inicio(int cliente) {
        return new Mensaje(Tipo.INICIO, cliente, "INICIO-" + cliente, false, 0);
    }

    public static Mensaje correo(int cliente, String id, boolean esSpam) {
        return new Mensaje(Tipo.CORREO, cliente, id, esSpam, 0);
    }

    public static Mensaje fin(String quien) {
        return new Mensaje(Tipo.FIN, -1, "FIN-" + quien, false, 0);
    }
}
