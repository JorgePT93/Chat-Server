package cl.ucn.disc.dsm.chat.model;
/**
 * @author: Jorge
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server extends Thread{

    //Atributos de la Clase.
    private static ArrayList<BufferedWriter> clients;
    private static ServerSocket server;
    private String name;
    private Socket con;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;


    /**
     * Metodo Main
     * @param args
     */
    public static void main(String []args) {

        try{
            //Se instancia el servidor con todos los elementos necesarios.
            JLabel lblMessage = new JLabel("Puerto del Servidor:");
            JTextField txtPort = new JTextField("9000");        //<----------- SE RECOMIENDA ENCARECIDAMENTE USAR ESTE PUERTO.
            Object[] texts = {lblMessage, txtPort };
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPort.getText()));
            clients = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null,"Activando servidor en el puerto: "+
                    txtPort.getText());

            while(true){
                System.out.println("Esperando conexion...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Server(con);
                t.start();
            }

        }catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * Metodo "run".
     * Cada vez que un nuevo cliente ingrese al servidor, este metodo funciona como "trigger", alojandolo a un hilo y revisando si hay algun mensaje nuevo.
     * Si eso ocurre, se lee y se activa el metodo "sendToAll" para poder enviar el mensaje a los demas usuarios conectados al chat.
     */
    public void run(){
        try{

            String msg;
            OutputStream ou =  this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bufferedWriter = new BufferedWriter(ouw);
            clients.add(bufferedWriter);
            name = msg = bufferedReader.readLine();

            while(!"Logout".equalsIgnoreCase(msg) && msg != null)
            {
                msg = bufferedReader.readLine();
                sendToAll(bufferedWriter, msg);
                System.out.println(msg);
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Metodo que envia los mensajes a todos los usuarios conectados al servidor.
     * Cuando un cliente envia un mensaje, el servidor lo recive y lo envia a
     * los demas usuarios coenctados.
     *
     * @param bufferedWriterOutput
     * @param message
     * @throws IOException en caso de algun error.
     */
    public void sendToAll(BufferedWriter bufferedWriterOutput, String message) throws  IOException
    {
        BufferedWriter bwS;

        for(BufferedWriter bw :  clients){
            bwS = (BufferedWriter)bw;
            if(!(bufferedWriterOutput == bwS)){
                bw.write(name + " dice-> " + message+"\r\n");
                bw.flush();
            }
        }
    }

    /**
     * Constructor de la clase Server.
     * @param con
     */
    public Server(Socket con){
        this.con = con;
        try {
            inputStream  = con.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}/*Fin de la clase Server*/
