import java.rmi.RemoteException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements ActionListener {

    private int numberOfCards = 8;
    private int hasEnterCount = 0;
    private boolean usernamePutCard;
    private ServerInterface rem;
    private int id;
    private String name;
    private int whoseChanceIs;
    private String whatTypeToPut;
    private boolean GameOver = false;
    private boolean exitThread;
    private JPanel ChatPanel = null;
    private ReadThread t;
    private JPanel MyPanel = null;
    private CardLabel Card1 = null;
    private CardLabel Card2 = null;
    private CardLabel Card3 = null;
    private CardLabel Card4 = null;
    private CardLabel Card5 = null;
    private CardLabel Card6 = null;
    private CardLabel Card7 = null;
    private CardLabel Card8 = null;

    private CardLabel Other1 = null;
    private CardLabel Other2 = null;
    private CardLabel Other3 = null;
    private CardLabel Other4 = null;


    private JLabel lbl_status = null;
    private JLabel lbl_score = null;
    private JLabel lbl_chance = null;
    private JLabel lbl_LeavedUses = null;
    
    private JLabel User1 = null;
    private JLabel User2 = null;
    private JLabel User3 = null;
    private JLabel User4 = null;
    
    private CardLabel UsersPutCards[] = new CardLabel[4];
    private CardLabel CardArray[] = new CardLabel[numberOfCards];
    private JLabel UsersName[] = new JLabel[5];
    
    private int[][] OtherUserPutCardPosition = { { 0, 0 }, { 199, 293 },
            { 91, 190 }, { 199, 92 }, { 301, 190 } };
    private int[][] OtherUserNamePosition = { { 0, 0 }, { 64, 422 }, { 6, 94 },
            { 350, 29 }, { 445, 380 } };
    
    private JLabel cardPicture1 = null;
    private JLabel cardPicture3 = null;
    private JLabel cardPicture2 = null;
    private JLabel CardPrio = null;
    private JLabel pr1 = null;
    private JLabel pr2 = null;
    private JLabel pr3 = null;
    private JLabel pr4 = null;
    private JPanel BottomPanel = null;
    private JLabel Winner1 = null;
    private JLabel Winner2 = null;
    private JLabel Winner3 = null;
    private JLabel Winner4 = null;
    private JLabel WinnerLabel = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;

    private JTextArea jta = null;
    private JScrollPane jsp = null;
    private JTextArea sendText = null;
    private JPanel SenderPanel = null;
    private JButton send = null;
    
    public GUI(ServerInterface r, String p_host, String p_name, int id) {
        super();
        initialize();
        try {
            this.rem = r;
            this.id = id;
            System.out.println("Your id is : " + id);
            if (id == 0) {
                System.out.println("There are enough players.");
            } 
            else {
                this.name = rem.getName(id);
                this.setTitle("Hello " + name + "! Welcome to my game.");
                GiveCards();
            }

        } 
        catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
        }
    }

    private void initialize() {
        this.setSize(900, 654);
        this.setContentPane(getMyPanel());
        this.setResizable(false);
        this.setContentPane(getMyPanel());
        this.setTitle("Card Game");

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    rem.needToLeave(id);
                }
                catch (RemoteException e1) {
                    e1.printStackTrace();
                } 
                finally {
                    exitThread = true;
                }
            }
        });
    }

    public void GiveCards() throws RemoteException {
        Card[] arr;
        if (id > 0 & id <= 4) {
            arr = rem.giveMeCards(id);

            /*
             * this piece of code makes the card labels able to see
             */
            for (int i = 0; i < numberOfCards; i++) {
                CardArray[i].setIcon(new ImageIcon("pic\\" + arr[i].getType()
                        + " " + arr[i].getValue() + ".png"));
                CardArray[i].cardId = i + 1;
                CardArray[i].card = arr[i];
                CardArray[i].almostPut = false;
            }
        }
        t = new ReadThread("ID" + id, rem, id);
        t.start();
    }

    private void putCard(CardLabel lbl) {
        int putCard = lbl.cardId;
        if (putCard > 0 & putCard <= numberOfCards) {
            try {
                if (rem.putCard(id, putCard - 1)) {
                    lbl.setBounds(lbl.getBounds());
                    lbl.setIcon(null);
                    lbl.almostPut = true;
                    lbl.card.setCardReset();
                    lbl_status.setText("Card is put");
                }
            } 
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class ReadThread extends Thread {
        ServerInterface rem;
        int u_id;
        private int sleepTime = 0;
        private int chance;

        public ReadThread(String name, ServerInterface p_rem, int p_id) {
            super(name);
            sleepTime = (int)(Math.random() * 3000);
            this.rem = p_rem;
            this.u_id = p_id;
        }

        public void run() {
            while (true) {
                if (exitThread) {
                    break;
                }
                try {
                    jta.setText(rem.getChat());
                    hasEnterCount = rem.howManyEntered();
                    if (hasEnterCount >= 4) {
                        if (rem.Leave()) {
                            lbl_LeavedUses.setText(rem.getName(rem.LeavedPlayerID()) + " has left the game!");
                            lbl_LeavedUses.setForeground(Color.WHITE);
                            lbl_status.setText("Game Over");
                            lbl_status.setForeground(Color.WHITE);
                            GameOver = true;
                            break;
                        }
                        if (!usernamePutCard) {
                            int theID = id;
                            for (int i = 1; i <= 4; i++) {
                                UsersName[theID].setLocation(OtherUserNamePosition[i][0], OtherUserNamePosition[i][1]);
                                UsersPutCards[theID - 1].setLocation(OtherUserPutCardPosition[i][0],
                                        OtherUserPutCardPosition[i][1]);
                                theID++;
                                if (theID > 4) theID = 1;
                            }
                            UsersName[1].setText(rem.getName(1));
                            UsersName[1].setForeground(Color.WHITE);
                            UsersName[2].setText(rem.getName(2));
                            UsersName[2].setForeground(Color.WHITE);
                            UsersName[3].setText(rem.getName(3));
                            UsersName[3].setForeground(Color.WHITE);
                            UsersName[4].setText(rem.getName(4));
                            UsersName[id].setForeground(Color.WHITE);
                            lbl_status.setText("");
                            usernamePutCard = true;
                            setCardStrength();
                        }
                        
                        if (rem.roundOver()) {
                            for (int i = 0; i < 4; i++) {
                                UsersPutCards[i].setIcon(new ImageIcon(""));
                            }
                        }
                        // if all cards gone start next round
                        if (rem.canstartNextEvent(u_id)) {
                            if (rem.roundOver()) {
                                GiveCards();
                                setResizeCard();
                                break;
                            }
                        }
                        // return cards put by users
                        Card[] arrUserCards = rem.getLastPut();
                        for (int i = 0; i < 4; i++) {
                            if (arrUserCards[i].getType() != "N") {
                                UsersPutCards[i].setIcon(new ImageIcon("pic\\" + arrUserCards[i].getType() + " "
                                        + arrUserCards[i].getValue() + ".png"));
                            }
                        }

                        chance = rem.whoseChanceIs();
                        whoseChanceIs = chance;
                        whatTypeToPut = rem.getPutType();

                        if (u_id == chance) {
                            lbl_chance.setText("It's your turn");
                            lbl_chance.setForeground(Color.WHITE);

                        } 
                        else {
                            lbl_chance.setText("It's " + rem.getName(chance) + "\'s turn");
                            lbl_chance.setForeground(Color.WHITE);

                        }
                        lbl_score.setText("Score = " + Integer.toString(rem.giveMyRealPoints(u_id)));
                        lbl_score.setForeground(Color.WHITE);

                        if (rem.isGameOver()) {
                            GameOver = true;
                            lbl_status.setText("Game over!");
                            lbl_status.setForeground(Color.WHITE);
                            lbl_chance.setText("");

                            String winnerOrder = rem.getWinnerOrder();

                            String[] winnerArray = winnerOrder.split(",");
                            WinnerLabel.setForeground(Color.WHITE);
                            WinnerLabel.setText("Order!");
                            Winner4.setText("4 - " + winnerArray[0]);
                            Winner3.setText("3 - " + winnerArray[1]);
                            Winner2.setText("2 - " + winnerArray[2]);
                            Winner1.setText("1 - " + winnerArray[3]);

                            break;
                        }
                    } else {
                        lbl_status.setForeground(new Color(153, 206, 212));
                        lbl_status.setText("Players = " + hasEnterCount);
                        lbl_status.setFont(new Font("Calibri", Font.PLAIN, 16));
                    }
                    Thread.sleep(sleepTime);
                }

                catch (InterruptedException interruptedException) {
                    System.err.println("InterruptedException" + interruptedException.getMessage());
                } 
                catch (RemoteException e) {
                    System.err.println("RemoteException" + e.getMessage());
                } 
                catch (Exception e) {
                    System.err.println("Exception" + e.getMessage());
                }
            }
        }
    }

    private void setCardStrength() {
        String CardPriority;

        try {
            CardPriority = rem.getCardPriority();
            String[] splitArray = CardPriority.split(",");

            pr1.setText(splitArray[3] + "   >");
            pr2.setText(splitArray[2] + "   >");
            pr3.setText(splitArray[1] + "   >");
            pr4.setText(splitArray[0]);
        }

        catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
        }
    }


    private class MouseCard extends MouseAdapter {
        private CardLabel thisCard;
        private int paintOrder;
        int x, y;

        public MouseCard(CardLabel card, int order, int x, int y) {
            this.thisCard = card;
            paintOrder = order;
            this.x = x;
            this.y = y;
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
            MyPanel.setComponentZOrder(thisCard, paintOrder);
            thisCard.setLocation(new Point(x, y));
            MyPanel.updateUI();
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
            MyPanel.setComponentZOrder(thisCard, 0);
            thisCard.setLocation(new Point(x, y-10));
            MyPanel.updateUI();
        }

        public void mousePressed(java.awt.event.MouseEvent e) {
            if (GameOver | thisCard.almostPut | hasEnterCount != 4)
                return;
            if (!putTypeOk(thisCard.card)) {
                lbl_status.setText("Try another card");
                return;
            }
            if (whoseChanceIs != id) {
                lbl_status.setForeground(Color.WHITE);
                lbl_status.setText("It's not your turn!");
                return;
            }
            putCard(thisCard);
            thisCard.setSize(new Dimension(2, 135));
        }
    }

    private JPanel getMyPanel() {
        if (MyPanel == null) {

            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(235, 267, 132, 16));
            jLabel1.setForeground(Color.WHITE);
            jLabel1.setText("Enjoy!");
            jLabel1.setFont(new Font("Calibri", Font.PLAIN, 16));
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(225, 247, 116, 17));
            jLabel.setForeground(Color.WHITE);
            jLabel.setText("Game time");
            jLabel.setFont(new Font("Calibri", Font.PLAIN, 16));

            WinnerLabel = new JLabel();
            WinnerLabel.setBounds(new Rectangle(17, 470, 62, 24));
            WinnerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            WinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            WinnerLabel.setText("");
            Winner4 = new JLabel();
            Winner4.setText("");
            Winner4.setSize(new Dimension(90, 16));
            Winner4.setLocation(new Point(8, 560));
            Winner3 = new JLabel();
            Winner3.setText("");
            Winner3.setSize(new Dimension(90, 16));
            Winner3.setLocation(new Point(8, 540));
            Winner2 = new JLabel();
            Winner2.setText("");
            Winner2.setSize(new Dimension(90, 16));
            Winner2.setLocation(new Point(8, 520));
            Winner1 = new JLabel();
            Winner1.setText("");
            Winner1.setSize(new Dimension(90, 16));
            Winner1.setLocation(new Point(8, 500));
            pr4 = new JLabel();
            pr4.setText("");
            pr4.setSize(new Dimension(76, 21));
            pr4.setLocation(new Point(410, 5));
            pr4.setForeground(new Color(25, 34, 49));
            pr3 = new JLabel();
            pr3.setText("");
            pr3.setSize(new Dimension(76, 21));
            pr3.setLocation(new Point(310, 5));
            pr3.setForeground(new Color(25, 34, 49));
            pr2 = new JLabel();
            pr2.setText("");
            pr2.setSize(new Dimension(76, 21));
            pr2.setLocation(new Point(210, 5));
            pr2.setForeground(new Color(25, 34, 49));
            pr1 = new JLabel();
            pr1.setText("");
            pr1.setSize(new Dimension(76, 21));
            pr1.setLocation(new Point(110, 5));
            pr1.setForeground(new Color(25, 34, 49));

            CardPrio = new JLabel();
            CardPrio.setForeground(new Color(25, 34, 49));
            CardPrio.setFont(new Font("Dialog", Font.BOLD, 12));
            CardPrio.setSize(new Dimension(87, 22));
            CardPrio.setLocation(new Point(7, 5));
            CardPrio.setText("Priority:");
            cardPicture2 = new JLabel();
            cardPicture2.setBounds(new Rectangle(125, 8, 281, 90));
            cardPicture2.setIcon(new ImageIcon(getClass().getResource("/pic/Cards2.png")));
            cardPicture2.setText("");
            cardPicture3 = new JLabel();
            cardPicture3.setBounds(new Rectangle(430, 93, 84, 364));
            cardPicture3.setText("");
            cardPicture3.setIcon(new ImageIcon(getClass().getResource("/pic/Cards.png")));
            cardPicture1 = new JLabel();
            cardPicture1.setBounds(new Rectangle(13, 93, 84, 364));
            cardPicture1.setIcon(new ImageIcon(getClass().getResource("/pic/Cards.png")));
            cardPicture1.setText("");
            User4 = new JLabel();
            User4.setBounds(new Rectangle(5, 530, 50,70));
            User4.setHorizontalTextPosition(SwingConstants.CENTER);
            User4.setText("");
            User4.setHorizontalAlignment(SwingConstants.CENTER);
            User3 = new JLabel();
            User3.setBounds(new Rectangle(45, 500, 40, 90));
            User3.setHorizontalTextPosition(SwingConstants.CENTER);
            User3.setText("");
            User3.setHorizontalAlignment(SwingConstants.CENTER);
            User2 = new JLabel();
            User2.setBounds(new Rectangle(330, 40, 50, 15));
            User2.setHorizontalTextPosition(SwingConstants.CENTER);
            User2.setText("");
            User2.setToolTipText("");
            User2.setHorizontalAlignment(SwingConstants.CENTER);
            User1 = new JLabel();
            User1.setBounds(new Rectangle(5, 130, 70, 25));
            User1.setHorizontalTextPosition(SwingConstants.CENTER);
            User1.setHorizontalAlignment(SwingConstants.CENTER);
            User1.setText("");
            lbl_LeavedUses = new JLabel();
            lbl_LeavedUses.setText("");
            lbl_LeavedUses.setSize(new Dimension(135, 17));
            lbl_LeavedUses.setLocation(new Point(380, 550));
            lbl_chance = new JLabel();
            lbl_chance.setText("");
            lbl_chance.setSize(new Dimension(135, 17));
            lbl_chance.setLocation(new Point(380, 520));
            lbl_score = new JLabel();
            lbl_score.setText("");
            lbl_score.setSize(new Dimension(135, 17));
            lbl_score.setLocation(new Point(380, 490));
            lbl_status = new JLabel();
            lbl_status.setText("");
            lbl_status.setSize(new Dimension(135, 17));
            lbl_status.setLocation(new Point(380, 460));
            Other4 = new CardLabel();
            Other4.setText("");
            Other4.setSize(new Dimension(90, 135));
            Other4.setLocation(new Point(215, 305));
            Other3 = new CardLabel();
            Other3.setText("");
            Other3.setSize(new Dimension(90, 135));
            Other3.setLocation(new Point(340, 195));
            Other2 = new CardLabel();
            Other2.setText("");
            Other1 = new CardLabel();
            Other2.setSize(new Dimension(90, 135));
            Other2.setLocation(new Point(218, 84));
            Other1.setText("");
            Other1.setSize(new Dimension(90, 135));
            Other1.setLocation(new Point(93, 195));

            Card1 = new CardLabel();
            Card1.setText("");
            Card1.setSize(new Dimension(90, 135));
            Card1.setToolTipText("");
            Card1.setLocation(new Point(105, 450));
            Card1.addMouseListener(new MouseCard(Card1, 7, 105, 450));

            Card2 = new CardLabel();
            Card2.setText("");
            Card2.setSize(new Dimension(90, 135));
            Card2.setToolTipText("");
            Card2.setLocation(new Point(130, 450));
            Card2.addMouseListener(new MouseCard(Card2, 6, 130, 450));

            Card3 = new CardLabel();
            Card3.setText("");
            Card3.setSize(new Dimension(90, 135));
            Card3.setToolTipText("");
            Card3.setLocation(new Point(155, 450));
            Card3.addMouseListener(new MouseCard(Card3, 5, 155, 450));

            Card4 = new CardLabel();
            Card4.setText("");
            Card4.setSize(new Dimension(90, 135));
            Card4.setToolTipText("");
            Card4.setLocation(new Point(180, 450));
            Card4.addMouseListener(new MouseCard(Card4, 4, 180, 450));

            Card5 = new CardLabel();
            Card5.setText("");
            Card5.setSize(new Dimension(90, 135));
            Card5.setToolTipText("");
            Card5.setLocation(new Point(205, 450));
            Card5.addMouseListener(new MouseCard(Card5, 3, 205, 450));

            jta = new JTextArea(27, 27);
            jta.setEditable(false);
            jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            send = new JButton("send");
            send.addActionListener(this);
            sendText = new JTextArea(4, 14);

            Card6 = new CardLabel();
            Card6.setText("");
            Card6.setSize(new Dimension(90, 135));
            Card6.setToolTipText("");
            Card6.setLocation(new Point(230, 450));
            Card6.addMouseListener(new MouseCard(Card6, 2, 230, 450));

            Card7 = new CardLabel();
            Card7.setText("");
            Card7.setSize(new Dimension(90, 135));
            Card7.setToolTipText("");
            Card7.setLocation(new Point(255, 450));
            Card7.addMouseListener(new MouseCard(Card7, 1, 255, 450));

            Card8 = new CardLabel();
            Card8.setText("");
            Card8.setSize(new Dimension(90, 135));
            Card8.setToolTipText("");
            Card8.setLocation(new Point(280, 450));
            Card8.addMouseListener(new MouseCard(Card8, 0, 280, 450));

            CardArray[0] = Card1;
            CardArray[1] = Card2;
            CardArray[2] = Card3;
            CardArray[3] = Card4;
            CardArray[4] = Card5;
            CardArray[5] = Card6;
            CardArray[6] = Card7;
            CardArray[7] = Card8;

            UsersPutCards[0] = Other1;
            UsersPutCards[1] = Other2;
            UsersPutCards[2] = Other3;
            UsersPutCards[3] = Other4;

            UsersName[1] = User1;
            UsersName[2] = User2;
            UsersName[3] = User3;
            UsersName[4] = User4;

            MyPanel = new JPanel();
            MyPanel.setLayout(null);
            MyPanel.setBackground(new Color(25, 34, 49));
            MyPanel.add(Other1, null);
            MyPanel.add(Other2, null);
            MyPanel.add(Other3, null);
            MyPanel.add(Other4, null);
            MyPanel.add(lbl_status, null);
            MyPanel.add(lbl_score, null);
            MyPanel.add(lbl_chance, null);
            MyPanel.add(lbl_LeavedUses, null);
            MyPanel.add(User1, null);
            MyPanel.add(User2, null);
            MyPanel.add(User3, null);
            MyPanel.add(User4, null);
            MyPanel.add(cardPicture1, null);
            MyPanel.add(cardPicture3, null);
            MyPanel.add(cardPicture2, null);
            MyPanel.add(Card8, 0);
            MyPanel.add(Card7, 1);
            MyPanel.add(Card6, 2);
            MyPanel.add(Card5, 3);
            MyPanel.add(Card4, 4);
            MyPanel.add(Card3, 5);
            MyPanel.add(Card2, 6);
            MyPanel.add(Card1, 7);
            MyPanel.add(getBottomPanel(), null);
            MyPanel.add(getChatPanel(), null);
            MyPanel.add(Winner1, null);
            MyPanel.add(Winner2, null);
            MyPanel.add(Winner3, null);
            MyPanel.add(Winner4, null);
            MyPanel.add(WinnerLabel, null);
            MyPanel.add(jLabel, null);
            MyPanel.add(jLabel1, null);
            MyPanel.updateUI();
        }
        return MyPanel;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getSource() == send){
                rem.sendClientMsg(rem.getName(id) + ": "+sendText.getText());
                sendText.setText("");
            }
        } catch (RemoteException re) {
            re.printStackTrace();
        }
    }

    private void setResizeCard() {
        for (int i = 0; i < numberOfCards; i++) {
            CardArray[i].setSize(new Dimension(90, 135));
        }
    }

    private boolean putTypeOk(Card card) {
        if ((whatTypeToPut.equals(card.getType()) | whatTypeToPut.equals("") | notHaveCard())
                & !GameOver)
            return true;
        else
            return false;
    }

    private boolean notHaveCard() {
        boolean cardNotHave = true;
        for (int i = 0; i < CardArray.length; i++) {
            if (whatTypeToPut.equals(CardArray[i].card.getType()))
                cardNotHave = false;
        }
        return cardNotHave;
    }

    private JPanel getBottomPanel() {
        if (BottomPanel == null) {
            BottomPanel = new JPanel();
            BottomPanel.setLayout(null);
            BottomPanel.setBounds(new Rectangle(-1, 592, 900, 30));
            BottomPanel.setBorder(BorderFactory.createLineBorder(new Color(25, 34, 49), 1));
            BottomPanel.add(CardPrio, null);
            BottomPanel.add(pr1, null);
            BottomPanel.add(pr2, null);
            BottomPanel.add(pr3, null);
            BottomPanel.add(pr4, null);
        }
        return BottomPanel;
    }

    private JPanel getChatPanel(){
        if(ChatPanel == null){
            ChatPanel = new JPanel();
            ChatPanel.setLayout(new FlowLayout());
            ChatPanel.setBounds(new Rectangle(540, 20, 320, 550));
            ChatPanel.setBorder(BorderFactory.createLineBorder(new Color(25, 34, 49), 1));
            ChatPanel.add(jsp, null);
            ChatPanel.add(getSenderPanel(), null);

        }
        return ChatPanel;
    }

    private JPanel getSenderPanel(){
        if(SenderPanel == null){
            SenderPanel = new JPanel();
            SenderPanel.setLayout(new GridLayout(1, 2));
            SenderPanel.setBounds(new Rectangle(530, 580, 40, 30));
            SenderPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            SenderPanel.add(sendText);
            SenderPanel.add(send);
            System.out.println(SenderPanel.getWidth());
            System.out.println(SenderPanel.getHeight());
        }
        return SenderPanel;
    }
}