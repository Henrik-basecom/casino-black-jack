import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackGame extends CasinospielBasis{
    private Scanner sc = new Scanner(System.in);
    private GamePhases gamePhase = GamePhases.WelcomeAndBet;
    private String[] cards = {"2","3","4","5","6","7","8","9","10","Bube","Dame","König","ASS"};
    private String[] prefixs = {"♠","♥","♦","♣"};
    private ArrayList<String> deck = new ArrayList<String>();
    private ArrayList<String> handPlayer = new ArrayList<String>();
    private ArrayList<String> handDealer =  new ArrayList<String>();
    private int playerBet;


    public BlackJackGame(Spieler spieler) {
        super("BlackJack", spieler);
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }


    public int getPlayerBet() {
        return playerBet;
    }

    public void setPlayerBet(int playerBet) {
          this.playerBet = playerBet;
    }

    public ArrayList<String> getHandPlayer() {
        return handPlayer;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public String getRandomCard() {
        if (deck.isEmpty()) {
            resetDeck();
        }
        int randomIndex = (int) (Math.random() * deck.size());
        String card = deck.get(randomIndex);
        deck.remove(randomIndex);
        return card;
    }

    public void removeDeckCard(String card){

    }

    public void addCardToHand(String card, String hand){

    }

    public void resetDeck(){
        deck.clear();
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }

    public void resetHand(){

    }

    public String formatHandToString(String hand) {
        ArrayList<String> targetHand = hand.equals("player") ? handPlayer : handDealer;
        String result = "";
        for (String card : targetHand) {
            result += "[" + card + "]" + " + ";
        }
        return result.replaceAll(" \\+ $", "");
    }

    public int calculateHand(String hand) {
        ArrayList<String> targetHand = hand.equals("player") ? handPlayer : handDealer;
        int assCount = 0;
        int tempResult = 0;

        for (String card : targetHand) {
            String cardValue = card.split(" ")[1]; // Extrahieren des Kartenwerts (z.B. "♥ 10" → "10")
            switch (cardValue) {
                case "ASS":   assCount++; break; // ASS = 11 (später anpassbar für 1/11)
                case "Bube":
                case "Dame":
                case "König": tempResult += 10; break;
                default:      tempResult += Integer.parseInt(cardValue); // Zahlenkarten (2-10)
            }
        }

        // Speichere das Ergebnis vorläufig in dem jedes Ass als 1 gewertet wird
        int result = tempResult + assCount;

        // Checke wie viele Asse zu einer 11 werden können ohne über 21 Punkte zu kommen & update das Ergebnis
        if (result < 21) {
            for (int i = 1; i <= assCount; i++) {
                int tempValue = tempResult + assCount - i + i * 11;
                if (tempValue == 21) {
                    result = tempValue;
                    break;
                }
                if (tempValue < 21) {
                    result = tempValue;
                    continue;
                }
                break;
            }
        }

        return result;
    }

    public GameResults gameResult() {
        int dealerResult = calculateHand("dealer");
        int playerResult = calculateHand("player");

        if (playerResult > 21 || (dealerResult > playerResult && dealerResult <= 21)) {
            return GameResults.Lose;
        }
        if (dealerResult > 21 || dealerResult < playerResult) {
            return GameResults.Win;
        }

        return GameResults.Draw;
    }

    public void playerTurn() {
        System.out.println("\n --- Entscheide dich ---");
            System.out.println("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?");

            boolean runLoop = true;
            while (runLoop) {
                String input = sc.nextLine();
                
                if (!input.equals("hit") && !input.equals("stand")) {
                    System.out.println("Bitte gib 'hit' oder 'stand' ein.");
                    continue;
                }

                if (input.equals("hit")) {
                    String rndCard = getRandomCard();
                    System.out.println("Du ziehst: " + rndCard);
                    handPlayer.add(rndCard);
                    System.out.println("Dein Blatt:   " + formatHandToString("player"));
                    System.out.println("Deine Punkte: " + calculateHand("player"));
                    if (calculateHand("player") > 21) {
                        runLoop = false;
                        break;
                    }
                    System.out.println("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?");
                    continue;
                }

                runLoop = false;
            }
    }

    public void dealerTurn(){
        System.out.println("\n--- Dealer macht seinen Zug. ---");
        while (calculateHand("dealer") < calculateHand("player")) {
            this.handDealer.add(getRandomCard());
        }
    }

    public void createDeck(){

    }


    @Override
    public String ersteNachricht() {
        return super.spieler.getName() + ", Willkommen bei \n" +
                "┌───────┐   ┌───────┐\n" +
                "│10     │   │A      │\n" +
                "│   ♠   │   │  ♥    │\n" +
                "│       │   │       │\n" +
                "└───────┘   └───────┘\n" +
                " B L A C K J A C K\n" +
                "\nDu verfügst aktuell über " + super.spieler.getJetons() + " Jetons\n" +
                "Bitte gib an wie viele Jetons du setzen möchtest: ";
    }

    @Override
    public String verarbeiteEingabe(String eingabe) {
        if (this.gamePhase == GamePhases.WelcomeAndBet) {
            Boolean checkInput = true;
            int numInput = -1;
            while (checkInput) {
                try {
                    numInput = Integer.parseInt(eingabe);
                } catch (NumberFormatException ex) {
                    System.err.println("Bitte gib nur eine Zahl ein!");
                    System.out.println("Neue Eingabe: ");
                    eingabe = sc.nextLine();
                    continue;
                }
                if (numInput > super.spieler.getJetons() || numInput < 0) {
                    System.err.println("Du musst mindestens 1 Jeton setzen!");
                    System.err.println("Du kannst nur maximal " + super.spieler.getJetons() + " Jetons setzen!");
                    System.out.println("Neue Eingabe: ");
                    eingabe = sc.nextLine();
                    continue;
                }
                checkInput = false;
            }
            this.playerBet = numInput;
            System.out.println("Du hast " + numInput + " Jetons gesetzt.");
            this.gamePhase = GamePhases.GetCards;
        }


        if (this.gamePhase == GamePhases.GetCards) {
            // Spieler erhält 2 Karten
            handPlayer.add(getRandomCard());
            handPlayer.add(getRandomCard());

            // Dealer erhält 2 Karten
            handDealer.add(getRandomCard());
            handDealer.add(getRandomCard());

            // Ausgabe des aktuellen Spielstands
            System.out.println("\n--- Karten wurden ausgeteilt ---");
            System.out.println("Dealer zeigt: [" + handDealer.get(0) + "]" + " +  [???]");
            System.out.println("Dein Blatt:   " + formatHandToString("player"));
            System.out.println("Deine Punkte: " + calculateHand("player"));

            this.gamePhase = GamePhases.Decision;
        }

        if (this.gamePhase == GamePhases.Decision) {
            this.playerTurn();
            if (calculateHand("player") <= 21) {
                this.dealerTurn();
            }
            this.gamePhase = GamePhases.Stand;
        }

        if (this.gamePhase == GamePhases.Stand) {
            System.out.println("\n--- Endstand ---");
            System.out.println("Dealer Blatt:   " + formatHandToString("dealer"));
            System.out.println("Dealer Punkte: " + calculateHand("dealer"));
            System.out.println("Dein Blatt:   " + formatHandToString("player"));
            System.out.println("Deine Punkte: " + calculateHand("player"));
            this.gamePhase = GamePhases.EndAndPay;
        }

        if (this.gamePhase == GamePhases.EndAndPay) {
            GameResults gameResult = gameResult();

            if (gameResult == GameResults.Win) {
                System.out.println("Win");
            }
            if (gameResult == GameResults.Lose) {
                System.out.println("Lose");
            }
            if (gameResult == GameResults.Draw) {
                System.out.println("Draw");
            }
        }

        return "";
    }

    @Override
    public void neuesSpiel() {

    }
}
