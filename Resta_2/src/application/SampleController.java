package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.servidor.Servidor;
import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SampleController implements Initializable{
	
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private AnchorPane anchor2;
    
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
    
    private static int index = 0;
    
    private Actions actions;
   
    
    public SampleController(int inde) {
    	this.dialogStage = new Stage();
    	this.actions = null;
    	
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.IP_server.setText("127.0.0.1");
    	this.port.setText("54321");
    	//this.conectar();
    }
    
    
    public void conectar() {
        //Setando o título do AnchorPane com o nome do usuário do Chat
        this.dialogStage.setTitle(null);
        this.remetente = textName.getText();
       
        Mensagem mensagem = new Mensagem();
		
		mensagem.setRemetente(remetente);
		mensagem.setTexto("Entrou no Chat!");
        
        try {
        	
			this.actions = (Actions) Naming.lookup("rmi://localhost/actions");
			System.out.println("Objeto Localizado!");
			ThreadClient client;
        	client = new ThreadClient(remetente, history, anchor, anchor2, dialogStage, this.actions);
			this.actions.conectar(client, mensagem);
		
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.exit(0);
			e.printStackTrace();
		}
        //Instanciando uma ThreadCliente para ficar recebendo mensagens do Servidor
      
        desabilitarTextFields();
           
    }
    
    public void handleButtonSair() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto("Saiu do Chat!");
            
            this.actions.desconectar(null, mensagem);

            //Saída de Dados do Cliente
       

        } catch (IOException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.getDialogStage().close(); // fechar o jogo 
    }
    
    public void handleButtonEnviar() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto(this.message.getText());
            this.actions.enviarMensagem(mensagem);

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
