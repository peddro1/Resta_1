package chat.servidor.objeto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import application.Actions;
import application.ThreadBotao;
import application.ThreadClient;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Tabuleiro{
	
	private AnchorPane anchor;
	
	private AnchorPane anchor2;
	
	private Stage dialogStage;
	
	private Stage modal;
	
	private Actions actions;
	
	private String name;
	
	private static Tabuleiro instancia = null; 

	private ThreadBotao[][] matrixTab;
	
	private ThreadBotao[][] matrixTab2;
	
	private int ateX = -1; private int ateY = -1;
	
	private int actX = -1; private int actY = -1;

	public Tabuleiro(AnchorPane anchor, AnchorPane anchor2, Socket socket, String name, Stage dialogStage, String player, Actions actions) {
		this.actions = actions;
		this.anchor = anchor;
		this.anchor2 = anchor2;
		this.dialogStage = dialogStage;
		this.name = name;
		if(this.name.equals("Thread Servidor: 1")) {
			this.matrixTab = new ThreadBotao[7][7];
			this.matrixTab2 = new ThreadBotao[7][7];
			this.setMatrixTab2(matrixTab);
		
			for(int i = 169, k = 0; k < 7; i=i+55, k++) {
				for(int j = 44, l = 0; l < 7; j=j+48, l++ ) {
					
					if((k < 2 && l < 2) || (k > 4 && l < 2) || (k < 2 && l > 4 ) || (k > 4 && l > 4)) {    // calculo com base nas posicoes
						this.matrixTab[k][l] = new ThreadBotao(i, j, this);   
						this.matrixTab[k][l].setVazio(true);    // vazio = true para as casas indisponiveis
					}
					else if(k == 3 && l == 3) {
						this.matrixTab[k][l] = new ThreadBotao(i, j, this);   
						this.matrixTab[k][l].setVazio(true);    // vazio = true para as casas indisponiveis
					}
					else {
						this.matrixTab[k][l] = new ThreadBotao(i, j, this);   // para as casa com peças
					}
				}
			}
			this.updateTab(anchor);
			
		}
		if(this.name.equals("Thread Servidor: 2")) {
			this.matrixTab2 = new ThreadBotao[7][7];
		
			for(int i = 169, k = 0; k < 7; i=i+55, k++) {
				for(int j = 44, l = 0; l < 7; j=j+48, l++ ) {
					
					if((k < 2 && l < 2) || (k > 4 && l < 2) || (k < 2 && l > 4 ) || (k > 4 && l > 4)) {    // calculo com base nas posicoes
						this.matrixTab2[k][l] = new ThreadBotao(i, j, this);   
						this.matrixTab2[k][l].setVazio(true);    // vazio = true para as casas indisponiveis
					}
					else if(k == 3 && l == 3) {
						this.matrixTab2[k][l] = new ThreadBotao(i, j, this);   
						this.matrixTab2[k][l].setVazio(true);    // vazio = true para as casas indisponiveis
					}
					else {
						this.matrixTab2[k][l] = new ThreadBotao(i, j, this);   // para as casa com peças
					}
				}
			}
			this.setMatrixTab(matrixTab2);
			limparChildrenEspecificos();
			this.updateTab(anchor);	
			this.updateTab2(anchor2);
			this.openDialog();
		}
	}
	

	
	public static Tabuleiro novoTabuleiro(AnchorPane anchor, AnchorPane anchor2, Socket socket, String name, Stage dialogStage, String player, Actions actions){
        if(instancia == null){
            instancia = new Tabuleiro(anchor, anchor2, socket, name, dialogStage, player, actions );
        }
        return instancia;
    }
	
	public void updateTab(AnchorPane anchor) {
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {	
    			if(this.getMatrixTab()[k][l] != null && !this.getMatrixTab()[k][l].isVazio() ) {
    				anchor.getChildren().remove(this.getMatrixTab()[k][l].getImageView());
        			anchor.getChildren().add(this.getMatrixTab()[k][l].getImageView());      // preenche tabuleiro
        			
        		}else if(this.getMatrixTab()[k][l] != null && this.getMatrixTab()[k][l].isVazio()) {
        			anchor.getChildren().remove(this.getMatrixTab()[k][l].getImageView());
        		}
            }
        }
	}
	
	public void updateTab2(AnchorPane anchor2) {
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {	
    			if(this.getMatrixTab2()[k][l] != null && !this.getMatrixTab2()[k][l].isVazio() ) {
    				anchor2.getChildren().remove(this.getMatrixTab2()[k][l].getImageView());
        			anchor2.getChildren().add(this.getMatrixTab2()[k][l].getImageView());      // preenche tabuleiro
        			
        		}else if(this.getMatrixTab2()[k][l] != null && this.getMatrixTab2()[k][l].isVazio()) {
        			anchor2.getChildren().remove(this.getMatrixTab2()[k][l].getImageView());
        		}
        		
            }
        }
	}
	
	
	private void limparChildrenEspecificos() {
		List<Node> nodesToRemove = new ArrayList<>();
		for (Node node : anchor.getChildren()) {
		    if (node instanceof ImageView) {
		        nodesToRemove.add(node);
		    }
		}
		anchor.getChildren().removeAll(nodesToRemove);
    }
	
	public static Tabuleiro getInstancia() {
        return instancia;
    }
	
	public static void setInstancia(Tabuleiro instancia) {
		Tabuleiro.instancia = instancia;
	}
	
	public ThreadBotao[][] getMatrixTab(){
		return this.matrixTab;
	}
	
	public void setMatrixTab(ThreadBotao[][] matrixTab){
		this.matrixTab = matrixTab;
	}
	
	public ThreadBotao[][] getMatrixTab2(){
		return this.matrixTab2;
	}
	
	public void setMatrixTab2(ThreadBotao[][] matrixTab){
		this.matrixTab2 = matrixTab;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ThreadBotao findBotaoByX2(double initPositionX, double finalPositionX, double initPositionY, double finalPositionY,  ThreadBotao thread  ) {
		
		double yPlus = finalPositionY + 10;
		double yLess = finalPositionY - 10;
		double maior, menor;
		
		if(initPositionX > finalPositionX) {
			maior = initPositionX;
			menor = finalPositionX;
		}else {
			maior = finalPositionX;
			menor = initPositionX;
		}
		ThreadBotao threadBotao = null;
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		
        		if(this.matrixTab2[k][l] != null) {
        			if(this.matrixTab2[k][l].getImageView().getX() > menor  && this.matrixTab2[k][l].getImageView().getX() < maior
                			&& this.matrixTab2[k][l].getImageView().getY() < yPlus && this.matrixTab2[k][l].getImageView().getY() > yLess
                			&& !this.getMatrixTab2()[k][l].isVazio()) {
                			this.matrixTab2[k][l].setVazio(true);
                			this.ateX = k;
                			this.ateY = l;
                			System.out.println(k +"/"+ l);	
                			
                	}
        		}
        		if(thread.equals(this.matrixTab2[k][l])) {
       
        			this.actX = k; this.actY = l;

        			this.matrixTab2[this.actX][this.actY].setVazio(true);
        			this.updateTab2(this.anchor2);
        			this.matrixTab2[this.actX][this.actY] = this.createClone(finalPositionX, finalPositionY);
        			
        			//System.out.println("Passou no if");
        		}
        		
            }
        }
		
		System.out.println("Passou");
		this.updateTab2(this.anchor2);
		
		this.checkEndGame2();
		this.sendMessage(this.actX, this.actY, finalPositionX, finalPositionY, this.ateX, this.ateY);
		this.updateTab2(this.anchor2);
		
		return thread;
	}
	
	public ThreadBotao findBotaoByY2(double initPositionX, double finalPositionX, double initPositionY, double finalPositionY, ThreadBotao thread ) {

		double xPlus = initPositionX + 10;
		double xLess = initPositionX - 10;
		
		double maior, menor;
		if(initPositionY > finalPositionY) {
			maior = initPositionY;
			menor = finalPositionY;
		}else {
			maior = finalPositionY;
			menor = initPositionY;
		}
		ThreadBotao threadBotao = null;
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		if(this.matrixTab2[k][l] != null) {
        			if(this.matrixTab2[k][l].getImageView().getY() > menor  && this.matrixTab2[k][l].getImageView().getY() < maior
                			&& this.matrixTab2[k][l].getImageView().getX() < xPlus && this.matrixTab2[k][l].getImageView().getX() > xLess
                			&& !this.getMatrixTab2()[k][l].isVazio()	) {
                			this.matrixTab2[k][l].setVazio(true);
                			this.ateX = k;
                			this.ateY = l;
                			System.out.println(k +"/"+ l);
                	}
        		}
        		if(thread.equals(this.matrixTab2[k][l])) {
        			
        			this.actX = k; this.actY = l;
        			this.matrixTab2[this.actX][this.actY].setVazio(true);
        			this.updateTab2(this.anchor2);
        			this.matrixTab2[this.actX][this.actY] = this.createClone(finalPositionX, finalPositionY);
        			
        			
        		}
            }
        }
		System.out.println("Passou");
	
		this.updateTab2(this.anchor2);
	
		
		this.checkEndGame2();
		
		this.sendMessage(this.actX , this.actY, finalPositionX, finalPositionY, this.ateX, this.ateY);
		this.updateTab2(this.anchor2);
		
		return thread;
	}
	
	public ThreadBotao createClone(double positionX, double positionY) {
		return new ThreadBotao(positionX, positionY, this);
	}
	
	public void setWithIndex(ThreadBotao thread, int k, int l) {
		this.matrixTab2[k][l] = thread;
	}
	
	public ThreadBotao getWithIndex(int k, int l) {
		return this.matrixTab2[k][l] ;
	}
	
	
	private void checkEndGame2() {
		int cont = 0;
	
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		if(this.matrixTab2[k][l] != null) {
        			if(!this.matrixTab2[k][l].isVazio()) {
        				cont++;
        			}
	            }
	        }
		}
		if(cont == 1) {
			Mensagem mensagem = new Mensagem();
	        mensagem.setRemetente(this.dialogStage.getTitle());
	        mensagem.setTexto("Ganhou!!");
	        mensagem.setAction(Mensagem.Action.SEND);
		}
	}
	
	private void sendMessage(int matrixX, int matrixY, double finalPositionX, double finalPositionY,  int matrixXAte, int matrixYAte) {
		
			Mensagem mensagem = new Mensagem();
	        
	        mensagem.setPosicoes(new PosicoesTab( matrixX, matrixY, finalPositionX, finalPositionY, matrixXAte, matrixYAte));
			if(this.dialogStage.getTitle().equals("Thread Servidor: 1")) mensagem.setPlayer1(false);
				
			else if (this.dialogStage.getTitle().equals("Thread Servidor: 2") ) mensagem.setPlayer1(true);
	        
			try {
				this.actions.updateTable(mensagem);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	}
	
	public void openDialog() {
		if(this.dialogStage.getTitle().equals("Thread Servidor: 2") && this.name.equals("Thread Servidor: 2")) {
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			
			//dialogStage.initOwner(this.dialogStage); // Define o estágio principal como proprietário do diálogo
			dialogStage.setTitle("Dialog Stage");
			
			Label label = new Label();
			label.setText("Esperando a jogada do outro jogador");
			Button closeButton = new Button("Fechar");
		    closeButton.setOnAction(e -> dialogStage.close());
		    
		    VBox dialogRoot = new VBox(10);
		    dialogRoot.setPadding(new Insets(10));
		   
		
		    dialogRoot.getChildren().add(label);
		    dialogRoot.getChildren().add(closeButton);
		   
		    dialogStage.setScene(new Scene(dialogRoot, 300, 150));
		
		    dialogStage.show();
		    
		    this.modal = dialogStage;
		    
		} 
    }
	
	public void openDialog1() {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		
		//dialogStage.initOwner(this.dialogStage); // Define o estágio principal como proprietário do diálogo
		dialogStage.setTitle("Dialog Stage");
		
		Label label = new Label();
		label.setText("Esperando a jogada do outro jogador");
		Button closeButton = new Button("Fechar");
	    closeButton.setOnAction(e -> dialogStage.close());
	    
	    VBox dialogRoot = new VBox(10);
	    dialogRoot.setPadding(new Insets(10));
	   
	
	    dialogRoot.getChildren().add(label);
	    dialogRoot.getChildren().add(closeButton);
	   
	    dialogStage.setScene(new Scene(dialogRoot, 300, 150));
	
	    dialogStage.show();
	    this.modal = dialogStage;
	}
	
	public void closeDialog() {
    	
       	if(this.modal != null && modal.isShowing()) {
       		
       		if(this.dialogStage.getTitle().equals("Thread Servidor: 2") ) {
       			this.modal.close();
    		    
    		} else if(this.dialogStage.getTitle().equals("Thread Servidor: 1") ) {
    			this.modal.close();
    		}
       	}
    }

}


