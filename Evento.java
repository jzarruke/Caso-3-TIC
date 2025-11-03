public class Evento{
    private boolean activado = false;
    public synchronized void esperar(){
        while(!activado){
            try {
                wait();
            }catch (InterruptedException ingnored){}
        }
        activado = false; 
    }
    public synchronized void señalar(){
        activado = true;
        notifyAll();
    }
}