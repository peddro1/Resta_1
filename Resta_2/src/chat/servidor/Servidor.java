package chat.servidor;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import application.Actions;
import application.ClientActions;
import chat.servidor.objeto.Mensagem;
import chat.servidor.objeto.Tabuleiro;

public class Servidor extends UnicastRemoteObject implements Actions{
	
	private List<ClientActions> clients = null;
	
	public Servidor() throws RemoteException {
		super();
		System.out.println("Servidor criado!");

		this.clients = new ArrayList<ClientActions>();
	}

	@Override
	public void conectar(ClientActions client, Mensagem mensagem) throws RemoteException {
	
		this.clients.add(client);
		mensagem.setNameThreadServer("Thread Servidor: " + String.valueOf(this.clients.size()));
		client.conectar(mensagem);
		enviarMensagem(mensagem);
		
	}

	@Override
	public void desconectar(ClientActions client, Mensagem mensagem) throws RemoteException {
		
		this.clients.remove(client);
		enviarMensagem(mensagem);
		
	}
	
	@Override
	public void enviarMensagem(Mensagem mensagem) throws RemoteException {
		
		for(ClientActions client : clients) {
			try {
				client.receberMensagem(mensagem);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public Tabuleiro updateTable(Mensagem mensagem) throws RemoteException {
		for(ClientActions client : clients) {
			client.updateTable(mensagem);
		}
		return null;
	}

}
