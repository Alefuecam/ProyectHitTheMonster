package participants;

public class Counter {
    private int count = 0;
    public Counter(int count){
        this.count=0;
    }
    public synchronized void inc(){

            count++;
            System.out.println(count);
    }
}
