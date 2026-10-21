package participants;

import java.net.InetAddress;
import java.util.Objects;

public class Jugadores {
    private InetAddress direccionIP;
    private int cont; //para saber cuantas veces ha ganado
    private int id;

    public  Jugadores(InetAddress x,int i){
        this.cont=0;
        this.direccionIP=x;
        this.id=i;
    }
    public int getId(){
        return id;
    }

    public InetAddress getDireccionIP() {
        return direccionIP;
    }
    public void inc(){
        this.cont++;
    }
    public int getCont(){
        return cont;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugadores jugadores = (Jugadores) o;
        return Objects.equals(id, jugadores.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
