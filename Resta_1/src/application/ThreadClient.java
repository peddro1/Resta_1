package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import chat.servidor.ThreadServidor;
import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;
import chat.servidor.objeto.Mensagem.Action;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


// Chat baseado no codigo https://github.com/ravarmes/sockets-chatreservado-javafx/tree/master 

public class ThreadClient extends Thread {
	private Socket socket;
	private TextArea textArea;
	private String remetente;
	private AnchorPane anchor;
	private Tabuleiro tabuleiro;
	boolean sair = false;
	static int playerNum = 0;
	
	public ThreadClient(String r, Socket s, TextArea textArea, AnchorPane anchor) {
	    this.remetente = r;
	    this.socket = s;
	    this.textArea = textArea;
	    this.anchor = anchor;
	}

	@Override
	public void run() {
	    try {
	        while (!sair) {
	            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
	            Mensagem mensagem = (Mensagem) entrada.readObject();
	            Action action = mensagem.getAction();
	
	            switch (action) {
	                case CONNECT:
	                    conectar(mensagem);
	                    break;
	                case DISCONNECT:
	                    desconectar(mensagem);
	                    break;
	                case SEND:
	                    receberMensagem(mensagem);
	                    break;
	                case USERS_ONLINE:
	                    atualizarUsuarios(mensagem);
	                    break;
	                case UPDATE_TABLE:
	                	updateTable(mensagem);
	                	break;
	                default:
	                    break;
	            }
	        }
	    } catch (IOException | ClassNotFoundException ex) {
	        Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
	    }
	 }
	 
	 public void conectar(Mensagem mensagem) {
		 Platform.runLater(() ->  {
			 this.tabuleiro = Tabuleiro.novoTabuleiro(anchor, socket);
			 /*
			 if(mensagem.getNameThreadServer().equals("Thread Servidor: 2") && mensagem.isPlayer1()) {
				 System.out.println(3);
				 tabuleiro.blockTab();
			 } 
			 if(mensagem.getNameThreadServer().equals("Thread Servidor: 1") && mensagem.isPlayer1()) {
				 System.out.println(1);
				 tabuleiro.freeTab();
			 
			 }
			 */ 
		 });  // inicializa tabuleiro
		 Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
		 
	 }
	
	 public void desconectar(Mensagem mensagem) throws IOException {
		 Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
	
	     if (mensagem.getRemetente().equals(this.remetente)) {
	         this.socket.close();
	         this.sair = true;
	     }
	 }
	
	 public void receberMensagem(Mensagem mensagem) throws IOException {
	     Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
	 }
	
	 public void atualizarUsuarios(Mensagem mensagem) {
	     ArrayList<String> usuariosOnline = mensagem.getUsuariosOnline();
	 }
	 
	 public void updateTable(Mensagem mensagem) {
		 Platform.runLater(() -> {
			 if(mensagem.getPosicoes() != null) {
				 int k = mensagem.getPosicoes().getMatrixX();
				 int l = mensagem.getPosicoes().getMatrixY();
				 double x = mensagem.getPosicoes().getFinalPositionX();
				 double y = mensagem.getPosicoes().getFinalPositionY();
				 int ateX = mensagem.getPosicoes().getMatrixXAte();
				 int ateY = mensagem.getPosicoes().getMatrixYAte(); 
				 tabuleiro.getMatrixTab()[k][l].getImageView().setX(x);
				 tabuleiro.getMatrixTab()[k][l].getImageView().setY(y);
				 tabuleiro.getMatrixTab()[ateX][ateY].setVazio(true);
				 tabuleiro.updateTab(anchor);
				 
				 /*
				 if(mensagem.getNameThreadServer().equals("Thread Servidor: 1") && mensagem.isPlayer1()) {
					 tabuleiro.blockTab();
				 } else if(mensagem.getNameThreadServer().equals("Thread Servidor: 2") && !mensagem.isPlayer1()) {
					 tabuleiro.blockTab();
				 }else if(mensagem.getNameThreadServer().equals("Thread Servidor: 2") && mensagem.isPlayer1()) {
					 tabuleiro.freeTab();
				 }else {
					 tabuleiro.freeTab();
				 }
				 */
			 }
		 });
	 }
	 	 
}
