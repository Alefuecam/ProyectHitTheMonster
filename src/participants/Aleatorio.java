package participants;

import java.util.Random;
public class Aleatorio {
    private int ran;
    public Aleatorio(){

    }
    Random rand= new Random();
    int limite=12;
    public  int aleatorio() {


        return rand.nextInt(limite);
    }
}
