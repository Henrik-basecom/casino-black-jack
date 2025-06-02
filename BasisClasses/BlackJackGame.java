import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackGame extends CasinospielBasis{
    private Scanner sc = new Scanner(System.in);
    private GamePhases gamePhase = GamePhases.WelcomeAndBet;
    private String[] cards = {"2","3","4","5","6","7","8","9","10","Bube","Dame","König","ASS"};
    private String[] prefixs = {"♠","♥","♦","♣"};
    private ArrayList<String> deck = new ArrayList<String>();
    private String[] handPlayer;
    private String[] handDealer;
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

    public String[] getHandPlayer() {
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

    public int calculateHand(String hand) {
        String[] targetHand = hand.equals("player") ? handPlayer : handDealer;
        int sum = 0;
        for (String card : targetHand) {
            if (card == null || card.equals("???")) continue; // Verdeckte Karte ignorieren

            String value = card.split(" ")[1]; // Extrahieren des Kartenwerts (z.B. "♥ 10" → "10")
            switch (value) {
                case "ASS":   sum += 11; break; // ASS = 11 (später anpassbar für 1/11)
                case "Bube":
                case "Dame":
                case "König": sum += 10; break;
                default:      sum += Integer.parseInt(value); // Zahlenkarten (2-10)
            }
        }
        return sum;
    }

    public void gameResult(){

    }

    public void playerTurn(){

    }

    public void dealerTurn(){

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
            // Initialisiere die Hände (je 2 Karten für Spieler, 2 für Dealer)
            handPlayer = new String[2];
            handDealer = new String[2];

            // Spieler erhält 2 Karten (offen)
            handPlayer[0] = getRandomCard();  // Erste Karte
            handPlayer[1] = getRandomCard();  // Zweite Karte

            // Dealer erhält 1 offene und 1 verdeckte Karte
            handDealer[0] = getRandomCard();  // Offene Karte
            handDealer[1] = "???";            // Verdeckte Karte (symbolisch)

            // Ausgabe des aktuellen Spielstands
            System.out.println("\n--- Karten wurden ausgeteilt ---");
            System.out.println("Dealer zeigt: " + handDealer[0] + " + [???]");
            System.out.println("Dein Blatt:   " + handPlayer[0] + " + " + handPlayer[1]);
            System.out.println("Deine Punkte: " + calculateHand("player") + "\n");

            // Wechsle zur nächsten Phase (Spieler-Entscheidung)
            this.gamePhase = GamePhases.Decision;
            System.out.println("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?");
        }
        return "";
    }

    @Override
    public void neuesSpiel() {

    }
}
