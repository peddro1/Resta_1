package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;

public interface Actions extends Remote {
	public Tabuleiro updateTable(Mensagem mensagem) throws RemoteException;
	public void enviarMensagem(Mensagem mensagem) throws RemoteException;
	public void conectar(ClientActions client, Mensagem mensagem) throws RemoteException;
	public void desconectar(ClientActions client, Mensagem mensagem) throws RemoteException;
}
