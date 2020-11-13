import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
	public static void main(String[] args) {
		try {
			RemoteObject obj = new RemoteObject();
			Naming.rebind("obj", obj);
			System.out.println("Server is running. Have fun playing my game.");
		}
		catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}
		catch (RemoteException e) {
			System.err.println(e.getMessage());
		}
	}

}
