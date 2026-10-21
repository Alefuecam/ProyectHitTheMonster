package participants;
import java.io.Serializable;
/*Esta clase solo sirve para iniciar la simulacion, crea n clientes */
public class Person {

    public static void main(String[] args) {
        int clientes = 3;
        for (int i = 1; i <= clientes; i++) {
            new Cliente(i*100).start();
           // new Cliente2().start();
        }
    }

}

