public class CardDeck {

	private Card D_card6 = new Card("7", "D", 1);
	private Card D_card7 = new Card("8", "D", 2);
	private Card D_card8 = new Card("9", "D", 3);
	private Card D_card9 = new Card("10", "D", 4);
	private Card D_card10 = new Card("J", "D", 5);
	private Card D_card11 = new Card("Q", "D", 6);
	private Card D_card12 = new Card("K", "D", 7);
	private Card D_card13 = new Card("A", "D", 8);

	private Card H_card6 = new Card("7", "H", 1);
	private Card H_card7 = new Card("8", "H", 2);
	private Card H_card8 = new Card("9", "H", 3);
	private Card H_card9 = new Card("10", "H", 4);
	private Card H_card10 = new Card("J", "H", 5);
	private Card H_card11 = new Card("Q", "H", 6);
	private Card H_card12 = new Card("K", "H", 7);
	private Card H_card13 = new Card("A", "H", 8);

	private Card C_card6 = new Card("7", "C", 1);
	private Card C_card7 = new Card("8", "C", 2);
	private Card C_card8 = new Card("9", "C", 3);
	private Card C_card9 = new Card("10", "C", 4);
	private Card C_card10 = new Card("J", "C", 5);
	private Card C_card11 = new Card("Q", "C", 6);
	private Card C_card12 = new Card("K", "C", 7);
	private Card C_card13 = new Card("A", "C", 8);

	private Card S_card6 = new Card("7", "S", 1);
	private Card S_card7 = new Card("8", "S", 2);
	private Card S_card8 = new Card("9", "S", 3);
	private Card S_card9 = new Card("10", "S", 4);
	private Card S_card10 = new Card("J", "S", 5);
	private Card S_card11 = new Card("Q", "S", 6);
	private Card S_card12 = new Card("K", "S", 7);
	private Card S_card13 = new Card("A", "S", 8);

	private Card[] cardPack = { D_card6, D_card7, D_card8, D_card9, D_card10,
			D_card11, D_card12, D_card13, H_card6, H_card7, H_card8, H_card9,
			H_card10, H_card11, H_card12, H_card13, C_card6, C_card7, C_card8,
			C_card9, C_card10, C_card11, C_card12, C_card13, S_card6, S_card7,
			S_card8, S_card9, S_card10, S_card11, S_card12, S_card13 };

	public final static int cardGivePerUser = 8;

	public Card[] getCards() {
		return cardPack;
	}

	public void mixCards() {
		int i1 = (int) (Math.random() * 10);
		int i2 = (int) (Math.random() * 10);
		int i3 = (int) (Math.random() * 10);
		int i4 = (int) (Math.random() * 10);

		System.out.println(i1 + " " + i2);
		for (; i1 < cardGivePerUser * 4 - 1; i1++) {
			Card prev = cardPack[i1];
			cardPack[i1] = cardPack[i1 + 1];
			cardPack[i1 + 1] = prev;
		}
		for (; i2 < cardGivePerUser * 4 - 1; i2++) {
			if (i2 + 3 > cardGivePerUser * 4 - 1)
					break;
			Card prev = cardPack[i2];
			cardPack[i2] = cardPack[i2 + 3];
			cardPack[i2 + 3] = prev;
		}
		for (; i3 < cardGivePerUser * 4 - 1; i3++) {
			if (i3 + 4 > cardGivePerUser * 4 - 1)
				break;
			Card prev = cardPack[i3];
			cardPack[i3] = cardPack[i3 + 4];
			cardPack[i3 + 4] = prev;
		}
		for (; i4 < cardGivePerUser * 4 - 1; i4++) {
			if (i4 + 5 > cardGivePerUser * 4 - 1)
					break;
			Card prev = cardPack[i4];
			cardPack[i4] = cardPack[i4 + 5];
			cardPack[i4 + 5] = prev;
		}
	}

	public void setOriginalCardPoints() {
		int x1, x2, x3, x4;
		x1 = (int) (Math.random()*50);
		x2 = (int) (Math.random()*50);
		x3 = (int) (Math.random()*50);
		x4 = (int) (Math.random()*50);

		CardPoint P1, P2, P3, P4;
		int DP = x1, HP = x2, CP = x3, SP = x4;
		P1 = new CardPoint(DP,"D", "Diamond");
		P2 = new CardPoint(HP,"H", "Heart");
		P3 = new CardPoint(CP,"C" , "Clubs");
		P4 = new CardPoint(SP,"S" , "Spades");
		System.out.println(DP + " " + HP + " " + " " + CP + " " + SP);
			
		for (int b = 0; b < cardGivePerUser * 4; b++) {
			if (cardPack[b].getType() == "D")
				cardPack[b].setPoint(cardPack[b].getPoints() + DP);
			else if (cardPack[b].getType() == "H")
				cardPack[b].setPoint(cardPack[b].getPoints() + HP);
			else if (cardPack[b].getType() == "C")
				cardPack[b].setPoint(cardPack[b].getPoints() + CP);
			else if (cardPack[b].getType() == "S")
				cardPack[b].setPoint(cardPack[b].getPoints() + SP);
		}
		CardPoint[] priorityArray = { P1, P2, P3, P4 };
		for (int a = 0; a < 4; a++) {
			for (int b = 3; b > a; b--)
				if (priorityArray[b].getPoint() < priorityArray[b - 1].getPoint()) {
					CardPoint temp = priorityArray[b];
					priorityArray[b] = priorityArray[b - 1];
					priorityArray[b - 1] = temp;
				}
			}

			String priority = priorityArray[0].getRealName() + "," + priorityArray[1].getRealName() + ","
					+ priorityArray[2].getRealName() + "," + priorityArray[3].getRealName();
			System.out.println("String = " + priority);
			RemoteObject.setCardPriority(priority);
		}
}