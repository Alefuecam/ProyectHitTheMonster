package participants;

import java.net.*;
import java.io.*;

public class ClienteLogin {



    public static void main(String args[]) {

        int clientes = 500;
        long in=System.nanoTime();
        for (int i = 1; i <= clientes; i++) {
            new ConexionLogin().start();
            // new Cliente2().start();
        }
        long f=System.nanoTime()-in;
        System.out.println(f);
    }
}
class ConexionLogin extends Thread{

    public void run(){
        Socket s = null;

        try {
            int serverPort = 55555;

            s = new Socket("localhost", serverPort);
            //s = new Socket("127.0.0.1", serverPort);

            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            DataInputStream in = new DataInputStream(s.getInputStream());

            //long uno=System.nanoTime();
            //Este es el intento de login
            out.writeUTF("Hello");            // UTF is a string encoding

            String data = in.readUTF();
           // long dos=System.nanoTime()-uno;
            //System.out.println(dos);
           // System.out.println("Received: " + data + " from " + s.getInetAddress());
            Cliente inicio= new Cliente (data);
            s.close();

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (s != null) try {
                s.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
    }
}