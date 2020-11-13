import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObject extends UnicastRemoteObject implements ServerInterface {

	private int count = 0; 
	private boolean noOneEntered = true;
	private int cardsHavings = 0;
	private Player user0, user1, user2, user3, user4;
	protected Player Arr_User[] = { user0, user1, user2, user3, user4 };

	private static String PriorityString;
	private final static int PointLimitToGameOver = 1000;
	private int cardGivePerUser = CardDeck.cardGivePerUser;
	private CardDeck deck = new CardDeck();
	private Card[] cardPack = deck.getCards();
	
	private int CountPutCards = 0;
	private int maxXCardVal = 0;
	private int maxCardOwnerID = 0;
	private int totalPoints = 0;

	private int currentPayable = 1;
	private String typeToPut = "";

	private boolean roundIsOver;

	private boolean somOneLeave = false;
	private int leaveID = 0;

	protected RemoteObject() throws RemoteException {
		super();
	}

	public static void setCardPriority(String priority) {
		PriorityString = priority;
	}

	@Override
	public String getCardPriority() {
		return PriorityString;
	}

	public int EnterGame(String name){
		if (noOneEntered) {
			deck.setOriginalCardPoints();
			noOneEntered = false;
			deck.mixCards();
			deck.mixCards();
		}
		count++;
		if (count > 4) {
			return 0;
		}
		else if (count == 1) {
			Arr_User[count] = new Player(count, name);
			printEnterDetl(name, count);
			return Arr_User[count].getID();

		}
		else if (count == 2) {
			Arr_User[count] = new Player(count, name);
			printEnterDetl(name, count);
			return Arr_User[count].getID();
		}
		else if (count == 3) {
			Arr_User[count] = new Player(count, name);
			printEnterDetl(name, count);
			return Arr_User[count].getID();
		}
		else if (count == 4) {
			Arr_User[count] = new Player(count, name);
			printEnterDetl(name, count);
			return Arr_User[count].getID();
		}
		else return 0;
	}

	public void printEnterDetl(String name, int id) {
		System.out.println("Player " + id + " with name : " + name + " Entered!");
	}

	public String getName(int id) {
		if (id > 4 | id < 1)
			return "Illegal user";
		else
			return Arr_User[id].U_NAME;
	}

	public Card[] giveMeCards(int id) {
		cardsHavings++;
		Card[] arr = new Card[cardGivePerUser];
		for (int i = 0; i < cardGivePerUser; i++) {
			arr[i] = new Card(
					cardPack[(id - 1) * cardGivePerUser + i].getValue(),
					cardPack[(id - 1) * cardGivePerUser + i].getType(),
					cardPack[(id - 1) * cardGivePerUser + i].getPoints());
		}

		Arr_User[id].setUserCards(arr);
		Arr_User[id].Event++;

		if (cardsHavings == 4) {
			cardsHavings = 0;
			roundIsOver = false;
			deck.mixCards();
			deck.mixCards();
			deck.mixCards();
			deck.mixCards();
		}
		return arr;
	}

	public Card[] getLastPut() {
		Card LastPutArray[] = new Card[4];
		LastPutArray[0] = Arr_User[1].getLastPut();
		LastPutArray[1] = Arr_User[2].getLastPut();// System.out.println(Arr_User[2].getLastPut().getValue());
		LastPutArray[2] = Arr_User[3].getLastPut();// System.out.println(Arr_User[3].getLastPut().getValue());
		LastPutArray[3] = Arr_User[4].getLastPut();// System.out.println(Arr_User[4].getLastPut().getValue());
		return LastPutArray;
	}

	@Override
	public int giveMyRealPoints(int id) {
		return Arr_User[id].getPointsByOthers();
	}

	@Override
	public String getPutType() {
		return typeToPut;
	}

	@Override
	public boolean putCard(int id, int crdNo) {
		if (currentPayable != id | Arr_User[id].getCard(crdNo).getPoints() == 0)
			return false;
		CountPutCards++;

		if (CountPutCards == 1)
			typeToPut = Arr_User[id].getCard(crdNo).getType();
		int recentGivenVal = Arr_User[id].getCard(crdNo).getPoints();

		totalPoints = totalPoints + recentGivenVal;
		Arr_User[id].setPutCard2Zero(crdNo);

		if (maxXCardVal < recentGivenVal) {
			maxXCardVal = recentGivenVal;
			maxCardOwnerID = id;
		}
		nextPlayable();

		if (CountPutCards > 3) {
			System.out.println("Before the deal");
			System.out.println("Player with maximum score: " + maxCardOwnerID);
			System.out.println("Total score: " + totalPoints);
			System.out.println("-----------------------------------------");
			AddCardPoints(totalPoints, id);
			printSummaray();
			
			roundIsOver = true;
			Arr_User[1].setPutCardtoZero();
			Arr_User[2].setPutCardtoZero();
			Arr_User[3].setPutCardtoZero();
			Arr_User[4].setPutCardtoZero();

			return true;
		}
		else {
			printSummaray();
			return true;
		}
	}

	private void printSummaray() {
		System.out.println("======================================================");
		System.out.println("Player 0's score: " + Arr_User[1].getPointsByOthers());
		System.out.println("Player 1's score: " + Arr_User[2].getPointsByOthers());
		System.out.println("Player 2's score: " + Arr_User[3].getPointsByOthers());
		System.out.println("Player 3's score: " + Arr_User[4].getPointsByOthers());
		System.out.println("Next player: = " + currentPayable);
		System.out.println("=====================================================");
	}

	public void AddCardPoints(int totPoints, int id) {
		Arr_User[maxCardOwnerID].setUserPoints(totalPoints);
		currentPayable = maxCardOwnerID;
		CountPutCards = 0;
		maxXCardVal = 0;
		maxCardOwnerID = 0;
		totalPoints = 0;
		typeToPut = "";
	}

	private void nextPlayable() {
		currentPayable++;
		if (currentPayable > 4) currentPayable = 1;
	}

	@Override
	public Card[] getCards(int id) {
		return Arr_User[id].getCards();
	}

	@Override
	public int whoseChanceIs() {
		return currentPayable;
	}

	@Override
	public boolean roundOver() {
		return roundIsOver;
	}

	@Override
	public int howManyEntered() {
		return count;
	}
	
	private boolean noAccess = false;

	public synchronized boolean canstartNextEvent(int p_id) {
		while (noAccess) {
			try {
				wait();
			}
			catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}

		noAccess = true;
		int p = Arr_User[p_id].getUserCardPoints();
		if (p == 0) {
			noAccess = false;
			return true;
		}

		else {
			noAccess = false;
			return false;
		}
	}

	@Override
	public boolean isGameOver() {
		boolean isOver = false;
		for (int i = 1; i < Arr_User.length; i++)
			if (Arr_User[i].getPointsByOthers() >= PointLimitToGameOver) {
				isOver = true;
			}
		return isOver;
	}

	@Override
	public void needToLeave(int id) {
		leaveID = id;
		somOneLeave = true;
	}

	@Override
	public int LeavedPlayerID() {
		return leaveID;
	}

	@Override
	public boolean Leave() {
		return somOneLeave;
	}

	public String getWinnerOrder() {
		Winner win1 = new Winner(Arr_User[1].U_NAME, Arr_User[1].getPointsByOthers());
		Winner win2 = new Winner(Arr_User[2].U_NAME, Arr_User[2].getPointsByOthers());
		Winner win3 = new Winner(Arr_User[3].U_NAME, Arr_User[3].getPointsByOthers());
		Winner win4 = new Winner(Arr_User[4].U_NAME, Arr_User[4].getPointsByOthers());

		Winner[] winnerArray = { win1, win2, win3, win4 };
		for (int a = 0; a < 4; a++) {
			for (int b = 3; b > a; b--)
				if (winnerArray[b].points < winnerArray[b - 1].points) {
					Winner temp = winnerArray[b];
					winnerArray[b] = winnerArray[b - 1];
					winnerArray[b - 1] = temp;
				}
		}

		String pointString = winnerArray[0].name + "," + winnerArray[1].name + "," + winnerArray[2].name + "," +
				winnerArray[3].name;
		return pointString;
	}

   String chat = "";
   @Override
   public void sendClientMsg(String msg) {
      chat += msg + "\n";     
   }
   
   @Override
   public String getChat() {
   		return chat;
   }
}