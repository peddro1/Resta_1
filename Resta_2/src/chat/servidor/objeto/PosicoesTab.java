package chat.servidor.objeto;

import java.io.Serializable;

public class PosicoesTab implements Serializable{
	
	private int matrixX, matrixY;
	private double finalPositionX, finalPositionY;
	private int matrixXAte, matrixYAte;
	
	public PosicoesTab(int matrixX, int matrixY, double finalPositionX, double finalPositionY, int matrixXAte, int matrixYAte) {
		this.matrixX = matrixX;
		this.matrixY = matrixY;
		this.finalPositionX = finalPositionX;
		this.finalPositionY = finalPositionY;
		this.matrixXAte = matrixXAte;
		this.matrixYAte = matrixYAte;
	}

	public int getMatrixY() {
		return matrixY;
	}

	public void setMatrixY(int matrixY) {
		this.matrixY = matrixY;
	}

	public int getMatrixX() {
		return matrixX;
	}

	public void setMatrixX(int matrixX) {
		this.matrixX = matrixX;
	}

	public double getFinalPositionX() {
		return finalPositionX;
	}

	public void setFinalPositionX(double finalPositionX) {
		this.finalPositionX = finalPositionX;
	}

	public double getFinalPositionY() {
		return finalPositionY;
	}

	public void setFinalPositionY(double finalPositionY) {
		this.finalPositionY = finalPositionY;
	}

	public int getMatrixYAte() {
		return matrixYAte;
	}

	public void setMatrixYAte(int matrixYAte) {
		this.matrixYAte = matrixYAte;
	}

	public int getMatrixXAte() {
		return matrixXAte;
	}

	public void setMatrixXAte(int matrixXAte) {
		this.matrixXAte = matrixXAte;
	}
}
