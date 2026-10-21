package participants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
/*La clase Cliente simula a un jugador, este usa la clase Juego para crear una GUI.
* La clase Cliente se encarga de unirse a un grupo multicast para recibir la ubicacion del mounstro y poder
* mostrarla en la interfaz.*/

public class Cliente extends Thread {
    public int id;
    String data;

    Juego frame;
    public Cliente(int i){
        frame= new Juego(i);//Identificador para simular competencia
        frame.setVisible(true);
        //this.id=i;
    }
    public Cliente(String ip){
        this.data=ip;

    }

    public void run() {


        MulticastSocket socket = null;
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group
            socket = new MulticastSocket(49155);

            char x,y;//para coordenadas
            int id;

            socket.joinGroup(group);
            while(true) {
                byte[] buffer = new byte[100];
                //System.out.println("Waiting for messages");
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                socket.receive(messageIn);//recieve es bloqueante
                frame.clearBoard();
                String c = new String(messageIn.getData()).trim();
                if(c.length()>2){
                    System.out.print("gano: ");
                    System.out.println(c);
                    //llamo a metodo que limpia board
                    frame.clearBoard();
                }
                else {
                    id = Integer.parseInt(c);
                   // System.out.println("La casilla tiene id: " + id + " from: " + messageIn.getAddress());
                    frame.genM(id);//metodo que relfeja mounstro en gui de cliente
                }
                //socket.leaveGroup(group);
            }

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }finally {
            if (socket != null) socket.close();
        }
    }
}
/*class ConnectionTCP extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private Socket s=null;

    public ConnectionTCP() {
        try {
            int serverPort=49152;
            s = new Socket("localhost",serverPort);//TCP
            //clientSocket = listenSocket.accept();
            out = new DataOutputStream(s.getOutputStream());
            in = new DataInputStream(s.getInputStream());


        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // an echo server
            // Person me= (Person)in.readObject();
            //for(int i=0;i<4;i++) {
            String data = in.readUTF();         // recibo click de jugador

            System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress());

            out.writeUTF(data + "l"); // envio respuesta
            //}
            //clientSocket.close();

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        /*} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}*/
