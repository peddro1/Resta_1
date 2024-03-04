package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.servidor.Servidor;
import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SampleController implements Initializable{
	
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private Pane pane;
    
	 @FXML
    private TextField IP_server;

    @FXML
    private Button conect;

    @FXML
    private Button exit;

    @FXML
    private TextArea history;

    @FXML
    private TextField message;
    
    @FXML
    private Label name;

    @FXML
    private TextField port;

    @FXML
    private Button send;
    
    @FXML
    private TextField textName;
    
 
    private Stage dialogStage;
    
    private Socket socket;
    
    private String remetente;
    

    static int userNum = 1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.IP_server.setText("127.0.0.1");
    	this.port.setText("54321");
    	//this.conectar();
    }
    
    
    public void conectar() {
        //Setando o título do AnchorPane com o nome do usuário do Chat
        this.dialogStage.setTitle(textName.getText());
        this.remetente = textName.getText();

        try {
            //Conectando ao Servidor do Chat
            socket = new Socket(IP_server.getText(), Integer.valueOf(port.getText()));
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

            //Enviando a primeira mensagem informando conexão (apenas para passar o nome do cliente)
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(remetente);
            mensagem.setTexto("Entrou no Chat!");
            mensagem.setAction(Mensagem.Action.CONNECT);

            //Instanciando uma ThreadCliente para ficar recebendo mensagens do Servidor
            ThreadClient thread = new ThreadClient(remetente, socket, history, anchor);
            thread.setName("Thread Cliente " + remetente);
            thread.start();

            //Saída de Dados do Cliente
            saida.writeObject(mensagem); //Enviando mensagem para Servidor
            
            userNum++;
            
            desabilitarTextFields();
            
           // Tabuleiro tabul = Tabuleiro.novoTabuleiro(anchor); // inicializa tabuleiro
            
            
        } catch (IOException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void handleButtonSair() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto("Saiu do Chat!");
            mensagem.setAction(Mensagem.Action.DISCONNECT);

            //Saída de Dados do Cliente
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem); //Enviando mensagem para Servidor

        } catch (IOException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.getDialogStage().close();
    }
    
    public void handleButtonEnviar() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto(this.message.getText());
            mensagem.setAction(Mensagem.Action.SEND);


            //Saída de Dados do Cliente
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem); //Enviando mensagem para Servidor

            this.message.setText("");
  
        } catch (IOException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void desabilitarTextFields() {
        //Desabilitando alguns TextField após conexão
        this.IP_server.setEditable(false);
        this.port.setEditable(false);
        this.textName.setEditable(false);
        this.conect.setDisable(true);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
