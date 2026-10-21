package participants;

import java.net.*;
import java.io.*;



public class ServidorLogin {

    static int cont=0;
    public static void main(String args[]) {
        Counter counter= new Counter(0);

        try {
            int serverPort = 55555;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            Servidor empieza= new Servidor();
            empieza.start();
            while (true) {
               // System.out.println("Waiting for messages...");
                /*Queremos un socket para cada cliente nuevo y un hilo por cliente*/
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                Login c=new Login(clientSocket,counter);
                c.start();
                System.out.println(cont);
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }

}

class Login extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private Counter counter;


    public Login(Socket aClientSocket,Counter i) {
        this.counter=i;

        try {
            clientSocket = aClientSocket;
            //construyo puente

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {

            // an echo server
            String data = in.readUTF();         // recibo solicitud
           // cont++;
            //synchronized (this){counter.inc();}
            counter.inc();
           // System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress());

            out.writeUTF("228.5.6.7");                // envio respuesta
            clientSocket.close();

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
