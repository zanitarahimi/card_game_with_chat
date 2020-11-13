import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

	int EnterGame(String name) throws RemoteException;
    String getName(int id) throws RemoteException;
	String getCardPriority() throws RemoteException;
	Card[] giveMeCards(int id) throws RemoteException;
	int giveMyRealPoints(int id) throws RemoteException;
	boolean putCard(int id, int crdNo) throws RemoteException;
	Card[] getCards(int id) throws RemoteException;
	int whoseChanceIs() throws RemoteException;
	boolean roundOver() throws RemoteException;
	Card[] getLastPut() throws RemoteException;
	int howManyEntered() throws RemoteException;
	boolean canstartNextEvent(int u_id) throws RemoteException;
	String getPutType() throws RemoteException;
	boolean isGameOver() throws RemoteException;
	void needToLeave(int id) throws RemoteException;
	int LeavedPlayerID() throws RemoteException;
	boolean Leave() throws RemoteException;
	String getWinnerOrder() throws RemoteException;
	void sendClientMsg(String msg) throws RemoteException;
	String getChat() throws RemoteException;
}
