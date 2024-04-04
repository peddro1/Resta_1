package application;



import chat.servidor.objeto.Tabuleiro;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThreadBotao extends Thread {
	
	private Tabuleiro tabuleiro;
	
	private boolean vazio;
	
	private double initPositionX;
	
	private double initPositionY;
	
	private ImageView image;
	
	private boolean fimDaJogada = false;
	
	private ThreadBotao response = null;
	

	public ThreadBotao(double positionX, double positionY, Tabuleiro tabu) {
		this.response = null;
		this.setName( positionX + "" + positionY);
		this.tabuleiro = tabu;
		this.image = new ImageView();
		this.setVazio(false);
		this.image.setImage(new Image(getClass().getResource("/image/botao.png").toExternalForm()));
		this.image.setFitHeight(47);
		this.image.setFitWidth(50);
		this.image.setX(positionX);   //280
		this.image.setY(positionY);   //60
		
		this.initPositionX = positionX;
		this.initPositionY = positionY;
		this.fimDaJogada = false;
		
		this.image.setOnMousePressed(e -> {

			this.start();
			System.out.println("Comecou!!");
			
		});
		this.image.setOnMouseDragged(e -> {
			this.image.setX(e.getX());
			this.image.setY(e.getY());
		});
		
	}
	
	
	public ImageView getImageView() {
		return image;
	}

	public void setImage(ImageView imageView) {
		this.image = imageView;
	}


	public boolean isVazio() {
		return vazio;
	}


	public void setVazio(boolean vazio) {
		this.vazio = vazio;
	}
	
	public void setPositionX(double x) {
		this.initPositionX = x; 
	}
	
	public void setPositionY(double y) {
		this.initPositionY = y; 
	}
	
	public double getPositionX() {
		return this.initPositionX; 
	}
	
	public double getPositionY() {
		return this.initPositionY; 
	}
	
	
	@Override
	public void run() {

		//boolean fimDaJogada = false;
		while(!fimDaJogada) {			
			this.image.setOnMouseReleased(e ->{
				e.getX();   // posicao final 
				e.getY();   // posicao final
				System.out.println(initPositionX +"/"+ initPositionY);
				System.out.println(e.getX() +"/"+ e.getY());
				if(Math.abs(e.getX() - this.initPositionX)  > 50 && Math.abs(e.getY() - this.initPositionY) < 50) {    // mudou no eixo X
					this.response = tabuleiro.findBotaoByX2(initPositionX, e.getX(), initPositionY, e.getY(), this);
					
				} if(Math.abs(e.getX() - this.initPositionX)  < 50 && Math.abs(e.getY() - this.initPositionY) > 50) {
					this.response = this.tabuleiro.findBotaoByY2(initPositionX, e.getX(), initPositionY, e.getY(), this);
					//System.out.println("passou no if y");
				}   // mudou no eixo y
				
				
				this.initPositionX = e.getX();
				this.initPositionY = e.getY();
				System.out.println("terminou!!");
				fimDaJogada = true;	
				
			});
		}
	}
	
	private void endAction() {
		
	}
	
}
