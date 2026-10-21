package participants;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Date;
import java.util.Random;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * @author Alejandro Fuentes
 * El servidor funciona de la siguiente manera: manda un mensaje via multicast nada n milisegundos
 * este mensaje contiene un valor aleatorio que representa una posicion en un table 3x4. Dicho valor
 * debe ser recibido por un cliente y representado en una interfaz grafica.
 *  El servidor igual establece una conexion TCP pues es via este protocolo que el cliente envia cunado
 *  da click al mounstro. El servidor guarda en una base de datos (Hashmap) que cliente gano la ronda.
 *  Inmediatamnete despues de recibir el click del cliente envia un mnesaje via mjulticast con nuevas
 *  posiciones.
 *  En caso de que algun jugador gane 5 rondas entonces dicho jugador gana la partida, el servidor
 *  procede a enviar via multicast el identificador del ganador y limpia la base de datos.
 *
 *
 */


public class Servidor extends Thread {
    public Servidor(){

    }

    public void run() {
        /*Multicast es una conexion de un sentido, por esto no aplica usar TCP pues no hace
         * falta confrmacion de recepcion, por esto se usan datagramas
         * pero cliente se comunica via TCP  */

        /*Se necesita una estructura de datos para saber quien gano*/
        HashMap<Integer,Integer> mapa=new HashMap<Integer,Integer>();
        //LinkedList<Jugadores> bd= new LinkedList<Jugadores>();
        try {

            int serverPort = 49152;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            //llamo a thread que hace multicast y manda valor donde aparecera mounstro
            Multicast multi=new Multicast();
            multi.start();

           while(true) {
            //llamo a thread que se encarga de conexion TCP, una por cliente
            Connection c = new Connection(listenSocket,mapa);
            c.start();
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
       }
    }
}
class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
   //SocketAddress direccion;
    InetAddress direccion;

    HashMap<Integer,Integer> mapa;
    //LinkedList<Jugadores> bd;


    public Connection(ServerSocket listenSocket, HashMap a) {//LinkedList a
        try {
            //Construyo puente tcp y recibo referencia a base de datos
            //bd=a;
            mapa=a;
            //System.out.println("TCP ");
            clientSocket = listenSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());


        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {

            String data = in.readUTF();         // recibo click de jugador, con id
           // System.out.println(System.nanoTime());//Para medir rendimiento
            Integer id=Integer.parseInt(data);
            direccion = clientSocket.getInetAddress();

            Jugadores j=new Jugadores(direccion,id);
            //reviso si el cliente ya existe, sino lo agrego
            if(!mapa.containsKey(id)){
                mapa.put(id,j.getCont());
            }
            //aumento valor
            mapa.put(id,mapa.get(id)+1);

            if(mapa.get(id)==5){//alguien gano ronda
                System.out.println("Ganoooo");
                new Multicast2(id).start();
                mapa.clear();
            }

            //IGNORAR es solo la implementacion de otra estructura de datos
            //Si el jugador no ha sido agregado se agrega
            /*if(!bd.contains(new Jugadores(direccion,id))){
                bd.add(new Jugadores(direccion,id));
            }
                //Incremento las victorias del cliente que llego primero
                for (int i = 0; i < bd.size(); i++) {
                    if (id.equals(bd.get(i).getId())) {
                        bd.get(i).inc();
                        //System.out.println("uno ");
                    }
                    if (bd.get(i).getCont() == 5) {
                        //el jugador gano
                        //envio multiast avisando
                       // System.out.println("Ganoooo");
                       //
                        //byte[] n=bd.get(i).getDireccionIP().getHostAddress().getBytes();

                        ///DatagramPacket messageOut = new DatagramPacket(n, n.length, group, 49155);
                        //socket.send(messageOut);
                        new Multicast2(bd.get(i).getId()).start();
                        bd.clear();//limpio base de datos
                    }
                }*/

                //out.writeUTF(data + "l"); // envio respuesta
                new Multicast3().start(); //mando nuevos valores
            //}
            //clientSocket.close();

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
/*La clase Multicast es un hilo que envia periodicamente un valor aleatorio*/
class Multicast extends Thread {
    MulticastSocket socket = null;

    int abc;
    Aleatorio a=new Aleatorio();

    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group
            socket = new MulticastSocket(49155);
            //System.out.println("Multicast ");
            String myMessage;//lo que mando

            byte[] m;
            socket.joinGroup(group);

            //aqui mando el mensaje repeditamente, los numeors aleatorios los tendria qu egenerar
            //dentro del ciclo para que si cambien cada vez
            /*no se si enviar una coordenada y luego otra o junstas
             * si envio una y luego otra podrian llegar en desorden
             * si las envio juntas el cliente tiene que intepretarlas*/
            while (true) {
                //envio un numero entre0 y 11 que es la casilla donde apareecra mounstro
                 //aleatorio = rand.nextInt(limite);//da numero entre 0 y 3
                abc=a.aleatorio();
                //System.out.println("multi1 "+abc);
                myMessage = Integer.toString(abc);//lo hago string


                m = myMessage.getBytes();//encoding de string a arreeglo de bytes
                //envio
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 49155);
                socket.send(messageOut);//como es datagrama no hay costo de entrar y salir (onstruir puente)

                //socket.leaveGroup(group);

                Thread.sleep(5000);

            }


        } /*catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        }*/ catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }  /*catch (InterruptedException e) {
            throw new RuntimeException(e);
        } */ catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) socket.close();
        }
    }
}
/*La clase multicast2 se encraga de lanzar el mensaje de que alguien gano
* no consiste de un while(true) por lo que solo se ejecuta una vez cuando se llama*/
class Multicast2 extends Thread {
    MulticastSocket socket = null;
    String myMessage1="X";
    int aux;

    public Multicast2(int ganador) {
        aux=ganador;
        myMessage1=String.valueOf(aux);
    }
    @Override
    public void run() {
        try {
            // an echo server
            // Person me= (Person)in.readObject();
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group
            socket = new MulticastSocket(49155);
           // System.out.println("Multicast2 gano id: ");
            //String myMessage;//lo que mando

            byte[] f;
            socket.joinGroup(group);
            //System.out.println("multi2 "+myMessage1);

            f = myMessage1.getBytes();//encoding de string a arreeglo de bytes
                //envio
            DatagramPacket messageOut = new DatagramPacket(f, f.length, group, 49155);
            socket.send(messageOut);//como es datagrama no hay costo de entrar y salir (onstruir puente)

            socket.leaveGroup(group);
          //  socket.close();

               // Thread.sleep(7000);


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } /*catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/finally {
            if (socket != null) socket.close();
        }
    }
}
/*La clase Multicast3 es usada para actualizar la posicion de los mounstros al igual
* que la clase Multicast, la diferencia es que noo conosiste de un while(true)
* esto es para que puedan haber rondas con mayor frecuencia, pues cada vez que el servidor recibe una respuesta
* se lanzan nuevas posiicones y puede inicar otra ronda */
class Multicast3 extends Thread {
    MulticastSocket socket = null;
    //Random rand= new Random();
    //int limite=12;
    // int aleatorio;
    int abc;
    Aleatorio a=new Aleatorio();

    @Override
    public void run() {
        try {
            // an echo server
            // Person me= (Person)in.readObject();
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group
            socket = new MulticastSocket(49155);
           // System.out.println("Multicast ");
            String myMessage;//lo que mando

            byte[] m;
            socket.joinGroup(group);

            //aqui mando el mensaje repeditamente, los numeors aleatorios los tendria qu egenerar
            //dentro del ciclo para que si cambien cada vez
            /*no se si enviar una coordenada y luego otra o junstas
             * si envio una y luego otra podrian llegar en desorden
             * si las envio juntas el cliente tiene que intepretarlas*/
            //while (true) {
                //envio un numero entre0 y 11 que es la casilla donde apareecra mounstro
                //aleatorio = rand.nextInt(limite);//da numero entre 0 y 3
                abc=a.aleatorio();
               // System.out.println("multi1 "+abc);
                myMessage = Integer.toString(abc);//lo hago string


                    m = myMessage.getBytes();//encoding de string a arreeglo de bytes
                //envio
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 49155);
                socket.send(messageOut);//como es datagrama no hay costo de entrar y salir (onstruir puente)

                //socket.leaveGroup(group);

                //Thread.sleep(7000);

            //}


        } /*catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        }*/ catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }  /*catch (InterruptedException e) {
            throw new RuntimeException(e);
        } */ /*catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/ finally {
            if (socket != null) socket.close();
        }
    }
}