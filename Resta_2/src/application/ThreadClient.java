package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


// Chat baseado no codigo https://github.com/ravarmes/sockets-chatreservado-javafx/tree/master 

public class ThreadClient extends UnicastRemoteObject implements ClientActions {
	private TextArea textArea;
	private String remetente;
	private AnchorPane anchor;
	private AnchorPane anchor2;
	private Stage dialogStage;
	private Actions actions;
	private Tabuleiro tabuleiro = null;
	boolean sair = false;
	static int playerNum = 0;
	
	public ThreadClient(String r, TextArea textArea, AnchorPane anchor, AnchorPane anchor2, Stage dialogStage, Actions actions) throws RemoteException{
	    super();
	    this.actions = actions;
		this.remetente = r;
	    this.textArea = textArea;
	    this.anchor = anchor;
	    this.anchor2 = anchor2;
	    this.dialogStage = dialogStage;
	}

	 public void conectar(Mensagem mensagem) throws RemoteException{
		 Platform.runLater(() ->  {
			 if(this.dialogStage.getTitle() == null) this.dialogStage.setTitle(mensagem.getNameThreadServer());
			 
			 this.tabuleiro = new Tabuleiro(anchor, anchor2, null, mensagem.getNameThreadServer(), dialogStage, this.remetente, this.actions);		 
		 });  // inicializa tabuleiro
		
	 }
	 
	 public void desconectar(Mensagem mensagem) throws RemoteException {
		 Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
	 }
	
	 public void receberMensagem(Mensagem mensagem) throws RemoteException {
	     Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
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
				
				 tabuleiro.getMatrixTab2()[k][l].getImageView().setX(x);
				 tabuleiro.getMatrixTab2()[k][l].getImageView().setY(y);
				 tabuleiro.getMatrixTab2()[ateX][ateY].setVazio(true);
				
				 
				 tabuleiro.updateTab2(anchor2); 
				 
				 if(dialogStage.getTitle().equals("Thread Servidor: 1")) { 
			
					 if(mensagem.isPlayer1()) {tabuleiro.closeDialog();} else { tabuleiro.openDialog1();} 
					
				 }
				 if(dialogStage.getTitle().equals("Thread Servidor: 2")) { 
					
					 if(mensagem.isPlayer1()) {tabuleiro.openDialog();} else { tabuleiro.closeDialog();} 
				 }
				  
			
			 }
		 });
	 }
	 
	 public String getRemetente() {
 		 return remetente;
	 }
	
	 public void setRemetente(String remetente) {
	 	 this.remetente = remetente;
	 }
}
