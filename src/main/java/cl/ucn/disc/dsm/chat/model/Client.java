package cl.ucn.disc.dsm.chat.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener, KeyListener {

    //Atributos de la clase.
    private static final long serialVersionUID = 1L;
    private JTextArea text;
    private JTextField message;
    private JButton buttonSend;
    private JButton buttonLogout;
    private JLabel lblHistoric;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream outputStream;
    private Writer outputWriter;
    private BufferedWriter bufferedWriter;
    private JTextField ip;
    private JTextField port;
    private JTextField name;


    /**
     * Main del cliente. Se crea un unico cliente por vez, lo conecta al
     * server socket y lo pone en "modo escucha".
     * @param args
     * @throws IOException
     */
    public static void main(String []args) throws IOException{

        Client app = new Client();
        app.connect();
        app.listen();
    }

    /**
     * Metodo que hace que un cliente se conecte al socket server.
     * Se crea un cliente socket y realiza la comunicacion con este.
     * @throws IOException en caso de algun error.
     */
    public void connect() throws IOException {
        socket = new Socket(ip.getText(),Integer.parseInt(port.getText()));
        outputStream = socket.getOutputStream();
        outputWriter = new OutputStreamWriter(outputStream);
        bufferedWriter = new BufferedWriter(outputWriter);
        bufferedWriter.write(name.getText()+"\r\n");
        bufferedWriter.flush();
    }

    /**
     * Metodo que envia un mensaje de un usuario al server socket.
     * Cada vez que se escribe un mensaje y se apreta la tecla "Enter",
     * este se envia al servidor.
     * @param msg
     * @throws IOException en caso de algun error.
     */
    public void sendMessage(String msg) throws IOException{

        if(msg.equals("Salir")){
            bufferedWriter.write("Desconectado \r\n");
            text.append("Desconectado \r\n");
        }else{
            bufferedWriter.write(msg+"\r\n");
            text.append( name.getText() + " dice -> " +         message.getText()+"\r\n");
        }
        bufferedWriter.flush();
        message.setText("");
    }

    /**
     * Metodo que permite a un cliente recibir los mensajes
     * del servidor. Cada vez que es es enviado un mensaje,
     * el metodo es procesado por el servidor y envia el mensaje
     * a todos los clientes conectados.
     * @throws IOException en caso de algun error.
     */
    public void listen() throws IOException{

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while(!"Logout".equalsIgnoreCase(msg))

            if(bfr.ready()){
                msg = bfr.readLine();
                if(msg.equals("Logout"))
                    text.append("Server out! \r\n");
                else
                    text.append(msg+"\r\n");
            }
    }


    /***
     * Metodo usado cuando el cliente se quiere desconectar del servidor.
     * El cliente es desconectado del server socket
     * @throws IOException en caso de algun error.
     */
    public void salir() throws IOException{

        sendMessage("Salir");
        bufferedWriter.close();
        outputWriter.close();
        outputStream.close();
        socket.close();
    }

    /**
     * Metodo que recibe las acciones de los botones del usuario.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if(e.getActionCommand().equals(buttonSend.getActionCommand()))
                sendMessage(message.getText());
            else
            if(e.getActionCommand().equals(buttonLogout.getActionCommand()))
                salir();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Metodo que funciona como "trigger" cuando se presiona la tecla "Enter"
     * @param e
     */

    //Si la tecla es presionada...
    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                sendMessage(message.getText());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    //Si la tecla es soltada...
    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Contructor de la clase Client.
     * @throws IOException en caso de algun error.
     */
    public Client() throws IOException{
        JLabel lblMessage = new JLabel("Check!");
        ip = new JTextField("127.0.0.1");
        port = new JTextField("12345");
        name = new JTextField("Cliente");
        Object[] texts = {lblMessage, ip, port, name};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        text              = new JTextArea(10,20);
        text.setEditable(false);
        text.setBackground(new Color(240,240,240));
        message = new JTextField(20);
        lblHistoric = new JLabel("Historial");
        lblMsg        = new JLabel("Mensaje");
        buttonSend = new JButton("Enviar");
        buttonSend.setToolTipText("Send Message");
        buttonLogout = new JButton("Salir");
        buttonLogout.setToolTipText("Salir del Chat");
        buttonSend.addActionListener(this);
        buttonLogout.addActionListener(this);
        buttonSend.addKeyListener(this);
        message.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(text);
        text.setLineWrap(true);
        pnlContent.add(lblHistoric);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(message);
        pnlContent.add(buttonLogout);
        pnlContent.add(buttonSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
        message.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(name.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }



}/*Fin de la clase Cliente*/