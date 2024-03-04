package chat.servidor.objeto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import application.ThreadBotao;

import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;

public class Tabuleiro implements Serializable{
	
	private AnchorPane anchor;
	
	private Socket socket;
	
	private boolean isPlayer1Turn;
	
	private static Tabuleiro instancia = null; 

	private ThreadBotao[][] matrixTab;
	
	private int ateX = -1; private int ateY = -1;
	
	private int actX = -1; private int actY = -1;

	public Tabuleiro(AnchorPane anchor, Socket socket) {
		this.socket = socket;
		this.anchor = anchor;
		
		this.isPlayer1Turn = true;
		
		this.matrixTab = new ThreadBotao[7][7];
		
		for(int i = 169, k = 0; k < 7; i=i+55, k++) {
			for(int j = 44, l = 0; l < 7; j=j+48, l++ ) {
				
				if((k < 2 && l < 2) || (k > 4 && l < 2) || (k < 2 && l > 4 ) || (k > 4 && l > 4)) {    // calculo com base nas posicoes
					this.matrixTab[k][l] = new ThreadBotao(i, j);   
					this.matrixTab[k][l].setVazio(true);    // vazio = true para as casas indisponiveis
				}
				else if(k == 3 && l == 3) {
					this.matrixTab[k][l] = null;   // null para as casas vazias
				}
				else {
					this.matrixTab[k][l] = new ThreadBotao(i, j);   // para as casa com peças
				}
			}
		}
		this.updateTab(anchor);
	}
	

	
	public static Tabuleiro novoTabuleiro(AnchorPane anchor, Socket socket){
        if(instancia == null){
            instancia = new Tabuleiro(anchor, socket);
        }
        return instancia;
    }
	
	public void updateTab(AnchorPane anchor) {
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		
        		if(this.getMatrixTab()[k][l] != null && !this.getMatrixTab()[k][l].isVazio()) {
        
        			anchor.getChildren().remove(this.getMatrixTab()[k][l].getImageView());
        			anchor.getChildren().add(this.getMatrixTab()[k][l].getImageView());      // preenche tabuleiro
        		}else if(this.getMatrixTab()[k][l] != null && this.getMatrixTab()[k][l].isVazio()) {
        			anchor.getChildren().remove(this.getMatrixTab()[k][l].getImageView());
        		}
        		
            }
        }
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
	
	public void updateMatrixWithPosition(int newRow, int newColumn, int oudRow, int oudColumn, ThreadBotao botao) {
		this.matrixTab[oudRow][oudColumn] = null;
		this.matrixTab[newRow][newColumn] = botao;
		
	}
	
	public void eatPiece(int x, int y) {
		this.matrixTab[x][y] = null;
	}
	
	public ThreadBotao findBotaoByX(double initPositionX, double finalPositionX, double initPositionY, double finalPositionY,  ThreadBotao thread  ) {
		System.out.println(initPositionX +"/"+ initPositionY);
		System.out.println(finalPositionX +"/"+ finalPositionY);
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
        		if(this.matrixTab[k][l] != null) {
        			if(this.matrixTab[k][l].getImageView().getX() > menor  && this.matrixTab[k][l].getImageView().getX() < maior
                			&& this.matrixTab[k][l].getImageView().getY() < yPlus && this.matrixTab[k][l].getImageView().getY() > yLess
                			&& !this.getMatrixTab()[k][l].isVazio()	) {
                			this.matrixTab[k][l].setVazio(true);
                			this.ateX = k;
                			this.ateY = l;
                			System.out.println(k +"/"+ l);	
                	}
        		}
        		if(thread.equals(this.matrixTab[k][l])) {
        			this.matrixTab[k][l].setVazio(true);
        			this.updateTab(this.anchor);
        			this.matrixTab[k][l] = this.createClone(finalPositionX, finalPositionY);
        			this.actX = k; this.actY = l;
        		}
            }
        }
		
		this.updateTab(this.anchor);
		this.checkEndGame();
		this.sendMessage(this.actX, this.actY, finalPositionX, finalPositionY, this.ateX, this.ateY);
		
		return threadBotao;
	}
	
	public ThreadBotao findBotaoByY(double initPositionX, double finalPositionX, double initPositionY, double finalPositionY, ThreadBotao thread ) {
		System.out.println(initPositionX +"/"+ initPositionY);
		System.out.println(finalPositionX +"/"+ finalPositionY);
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
        		if(this.matrixTab[k][l] != null) {
        			if(this.matrixTab[k][l].getImageView().getY() > menor  && this.matrixTab[k][l].getImageView().getY() < maior
                			&& this.matrixTab[k][l].getImageView().getX() < xPlus && this.matrixTab[k][l].getImageView().getX() > xLess
                			&& !this.getMatrixTab()[k][l].isVazio()	) {
                			this.matrixTab[k][l].setVazio(true);
                			this.ateX = k;
                			this.ateY = l;
                			System.out.println(k +"/"+ l);
                	}
        		}
        		if(thread.equals(this.matrixTab[k][l])) {
        			this.matrixTab[k][l].setVazio(true);
        			this.updateTab(this.anchor);
        			this.matrixTab[k][l] = this.createClone(finalPositionX, finalPositionY);
        			this.actX = k; this.actY = l;
        			
        		}
            }
        }
		
		this.updateTab(this.anchor);
		this.checkEndGame();
		//this.blockTab();
		this.sendMessage(this.actX , this.actY, finalPositionX, finalPositionY, this.ateX, this.ateY);
		
		return threadBotao;
	}
	
	private ThreadBotao createClone(double positionX, double positionY) {
		return new ThreadBotao(positionX, positionY);
	}
	
	private void checkEndGame() {
		int cont = 0;
	
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		if(this.matrixTab[k][l] != null) {
        			if(!this.matrixTab[k][l].isVazio()) {
        				cont++;
        			}
	            }
	        }
		}
		if(cont == 1) {
			Label label = new Label();
			label.setText("Ganhou!!");
			this.anchor.getChildren().add(label);
		}
	}
	
	public void blockTab() {
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		
        		if(this.matrixTab[k][l] != null && !this.matrixTab[k][l].isVazio()) {
        			this.matrixTab[k][l].getImageView().setDisable(true);
        		}
        		
            }
        }
	}
	
	public void freeTab() {
		for(int k = 0; k < 7; k++ ) {
        	for(int l = 0; l < 7; l++ ) {
        		
        		if(this.matrixTab[k][l] != null && !this.matrixTab[k][l].isVazio()) {			
        			this.matrixTab[k][l].getImageView().setDisable(false);
        		}
        		
            }
        }
	}
	
	private void sendMessage(int matrixX, int matrixY, double finalPositionX, double finalPositionY,  int matrixXAte, int matrixYAte) {
		try {
			Mensagem mensagem = new Mensagem();
	        mensagem.setRemetente(null);
	        mensagem.setTexto(null);
	        mensagem.setAction(Mensagem.Action.UPDATE_TABLE);
	        mensagem.setPosicoes(new PosicoesTab( matrixX, matrixY, finalPositionX, finalPositionY, matrixXAte, matrixYAte));
			mensagem.setPlayer1(!mensagem.isPlayer1());
	        
			ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
			saida.writeObject(mensagem); //Enviando mensagem para Servidor
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
