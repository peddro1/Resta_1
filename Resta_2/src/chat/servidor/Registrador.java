package chat.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registrador {

	public static void main(String args[]) {
		try {
			
			Servidor obj = new Servidor();
			
			Registry registry = LocateRegistry.createRegistry(1099);
			
			registry.rebind("actions", obj);
			
			System.out.println("Servidor Registrado!");
			
		} catch (Exception e) { System.out.println("Erro criando o servidor");
			e.printStackTrace();
		} 
	}
}
