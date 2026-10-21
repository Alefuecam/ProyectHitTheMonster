package participants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;
import java.io.*;
/*La clase Juego tiene la funcion de crear el GUI y de tomar accion cuando se de click en el mounstro
* para esto invoca a la clase ConexionTCP (se encuentra al final)
* ConexionTCP se activa cuando el cliente da click sobre el mounstro.  */
public class Juego extends JFrame {


    private JPanel panel;
    private JLabel[] holes = new JLabel[12];
    private int[] board = new int[100];

    private int score = 0;
    private int id;

    //ConnectionTCP c=new ConnectionTCP();
    public Juego(int id){
        initGUI();
        clearBoard();
        initEvents();
        this.id=id;
    }
    public void setId(int id){
        this.id=id;
    }
    public void initGUI(){
        setTitle("Mounstro");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 608, 500);

        JPanel contentPane = new JPanel();
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 51, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Mounstro");
        lblTitle.setForeground(new Color(153, 204, 0));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblTitle.setBounds(0, 0, 602, 47);
        contentPane.add(lblTitle);

        panel = new JPanel();
        panel.setBackground(new Color(0, 102, 0));
        panel.setBounds(32, 105, 535, 546);
        panel.setLayout(null);
        contentPane.add(panel);

        holes[0] = new JLabel("2,0");
        holes[0].setName("0");
        holes[0].setBounds(0, 264, 132, 132);
        panel.add(holes[0]);

        holes[1] = new JLabel("2,1");
        holes[1].setName("1");
        holes[1].setBounds(132, 264, 132, 132);
        panel.add(holes[1]);

        holes[2] = new JLabel("2,2");
        holes[2].setName("2");
        holes[2].setBounds(264, 264, 132, 132);
        panel.add(holes[2]);

        holes[3] = new JLabel("2,3");
        holes[3].setName("3");
        holes[3].setBounds(396, 264, 132, 132);
        panel.add(holes[3]);

        holes[4] = new JLabel("1,0");
        holes[4].setName("4");
        holes[4].setBounds(0, 132, 132, 132);
        panel.add(holes[4]);

        holes[5] = new JLabel("1,1");
        holes[5].setName("5");
        holes[5].setBounds(132, 132, 132, 132);
        panel.add(holes[5]);

        holes[6] = new JLabel("1,2");
        holes[6].setName("6");
        holes[6].setBounds(264, 132, 132, 132);
        panel.add(holes[6]);

        holes[7] = new JLabel("1,3");
        holes[7].setName("7");
        holes[7].setBounds(396, 132, 132, 132);
        panel.add(holes[7]);

        holes[8] = new JLabel("0,0");
        holes[8].setName("8");
        holes[8].setBounds(0, 0, 132, 132);
        panel.add(holes[8]);

        holes[9] = new JLabel("0.1");
        holes[9].setName("9");
        holes[9].setBounds(132, 0, 132, 132);
        panel.add(holes[9]);

        holes[10] = new JLabel("0,2");
        holes[10].setName("10");
        holes[10].setBounds(264, 0, 132, 132);
        panel.add(holes[10]);

        holes[11] = new JLabel("0,3");
        holes[11].setName("11");
        holes[11].setBounds(396, 0, 132, 132);
        panel.add(holes[11]);

        setContentPane(contentPane);
    }
    public void clearBoard(){
        for(int i = 0; i < 12; i++){
            holes[i].setIcon(loadImage("C:/Users/alefu/Desktop/hole.jpg"));
            board[i] = 0;
        }
    }
    public void genM(int aux){
        //int moleID = aux;
        clearBoard();
        board[aux] = 1;
        holes[aux].setIcon(loadImage("C:/Users/alefu/Desktop/monster.jpg"));
    }
    private ImageIcon loadImage(String path){
        Image image = new ImageIcon(path).getImage();
        Image scaledImage = image.getScaledInstance(132, 132,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    private void initEvents(){
        for(int i = 0; i < holes.length; i++){
            holes[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    JLabel lbl = (JLabel)e.getSource();
                    int id = Integer.parseInt(lbl.getName());
                    pressedButton(id);
                }
            });
        }
    }

    private void pressedButton(int id){
        int val = board[id];

        //if val is 1 = mole
        //if val is 0 = empty hole
        if(val==1){
            //long mili=System.nanoTime();
            //System.out.println(mili);
           new ConexionTCP(this.id).start();
           // new ConnectionTCP().start();
            //cuando lo pique aqui envio la respuesta tcp
        }else{ //val==0
            score--;//no pasa nada si no le atina
        }

       // lblScore.setText("Score: " + score); //update the score

        clearBoard();

        //genMole();
    }

}
class ConexionTCP extends Thread {
    public int identificacion;
    public ConexionTCP(int i){
        this.identificacion=i;
    }

    public void run() {


        Socket s = null; //T
        try {

            int serverPort = 49152;//puerto para TCP?
            s = new Socket("localhost",serverPort);//TCP

            DataOutputStream out = new DataOutputStream(s.getOutputStream());//TCP
            DataInputStream in = new DataInputStream(s.getInputStream());//TCP

            String iden=Integer.toString(this.identificacion);
            //long mili=System.nanoTime();
            //System.out.println(mili);
            out.writeUTF(iden);  //envia cundo pique jugador// UTF is a string encoding

           // String data = in.readUTF();
            //long nano=System.nanoTime()-mili;
            //System.out.println(nano);

            //socket.leaveGroup(group);
           // s.close();

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