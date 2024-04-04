package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;

public interface ClientActions extends Remote {

	public void updateTable(Mensagem mensagem) throws RemoteException;
	public void conectar( Mensagem mensagem) throws RemoteException;
	public void desconectar(Mensagem mensagem) throws RemoteException;
	public void receberMensagem(Mensagem mensagem) throws RemoteException;
}
