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
	

	public ThreadBotao(double positionX, double positionY) {
		
		this.image = new ImageView();
		this.setVazio(false);
		this.image.setImage(new Image(getClass().getResource("/image/botao.png").toExternalForm()));
		this.image.setFitHeight(47);
		this.image.setFitWidth(50);
		this.image.setX(positionX);   //280
		this.image.setY(positionY);   //60
		
		this.initPositionX = positionX;
		this.initPositionY = positionY;
		
		this.image.setOnMousePressed(e -> {
			this.tabuleiro = Tabuleiro.getInstancia();
			this.fimDaJogada = false;

			this.start();
			
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
	
	@Override
	public void run() {

		this.image.setOnMouseDragged(e -> {
			this.image.setX(e.getX());
			this.image.setY(e.getY());
		});
		while(!fimDaJogada) {			
			this.endAction();
		}
	}
	
	private void endAction() {
		this.image.setOnMouseReleased(e ->{
			e.getX();   // posicao final 
			e.getY();   // posicao final
			
			
			if(Math.abs(e.getX() - this.initPositionX)  > 50 && Math.abs(e.getY() - this.initPositionY) < 50) {    // mudou no eixo X
				tabuleiro.findBotaoByX(initPositionX, e.getX(), initPositionY, e.getY(), this);
				
			}else if(Math.abs(e.getX() - this.initPositionX)  < 50 && Math.abs(e.getY() - this.initPositionY) > 50) {
				tabuleiro.findBotaoByY(initPositionX, e.getX(), initPositionY, e.getY(), this);
			}   // mudou no eixo y
			this.initPositionX = e.getX();
			this.initPositionY = e.getY();
			fimDaJogada = true;	
		});
	}
	
}
