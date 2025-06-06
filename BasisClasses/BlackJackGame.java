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

    public ArrayList<String> getHand(GamePlayers player) {
        if (player == GamePlayers.Player) {
            return this.handPlayer;
        }
        if (player == GamePlayers.Dealer) {
            return this.handDealer;
        }
        return new ArrayList<String>();
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

    public void resetDeck(){
        deck.clear();
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }

    public void resetHand(GamePlayers player){
        if (player == GamePlayers.Player) {
            this.handPlayer.clear();
        }
        if (player == GamePlayers.Dealer) {
            this.handDealer.clear();
        }
    }

    public String formatHandToString(GamePlayers player) {
        ArrayList<String> targetHand = this.getHand(player);
        String result = "";
        for (String card : targetHand) {
            result += "[" + card + "]" + " + ";
        }
        return result.replaceAll(" \\+ $", "");
    }

    public int calculateHand(GamePlayers player) {
        ArrayList<String> targetHand = this.getHand(player);
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
        int dealerResult = this.calculateHand(GamePlayers.Dealer);
        int playerResult = this.calculateHand(GamePlayers.Player);

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
                String input = this.sc.nextLine();
                
                if (!input.equals("hit") && !input.equals("stand")) {
                    System.out.println("Bitte gib 'hit' oder 'stand' ein.");
                    continue;
                }

                if (input.equals("hit")) {
                    String rndCard = this.getRandomCard();
                    System.out.println("Du ziehst: " + rndCard);
                    this.getHand(GamePlayers.Player).add(rndCard);
                    System.out.println("Dein Blatt:   " + formatHandToString(GamePlayers.Player));
                    System.out.println("Deine Punkte: " + calculateHand(GamePlayers.Player));
                    if (this.calculateHand(GamePlayers.Player) > 21) {
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
        while (this.calculateHand(GamePlayers.Dealer) < this.calculateHand(GamePlayers.Player)) {
            this.handDealer.add(getRandomCard());
        }
    }

    public int checkJetonInput(String input) {
        Boolean checkInput = true;
        int numInput = -1;
        while (checkInput) {
            try {
                numInput = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.err.println("Bitte gib nur eine Zahl ein!");
                System.out.println("Neue Eingabe: ");
                input = this.sc.nextLine();
                continue;
            }
            if (numInput > super.spieler.getJetons() || numInput < 0) {
                System.err.println("Du musst mindestens 1 Jeton setzen!");
                System.err.println("Du kannst nur maximal " + super.spieler.getJetons() + " Jetons setzen!");
                System.out.println("Neue Eingabe: ");
                input = this.sc.nextLine();
                continue;
            }
            checkInput = false;
        }
        return numInput;
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
        boolean mainGameLoop = true;
        while (mainGameLoop) {
            if (this.gamePhase == GamePhases.WelcomeAndBet) {
                int numInput = this.checkJetonInput(eingabe);
                this.setPlayerBet(numInput);
                super.spieler.removeJetons(this.playerBet);
                System.out.println("Du hast " + numInput + " Jetons gesetzt.");
                this.gamePhase = GamePhases.GetCards;
            }


            if (this.gamePhase == GamePhases.GetCards) {
                // Dealer und Spieler erhalten jeweils 2 Karten
                for (int i = 0; i < 2; i++) {
                    this.getHand(GamePlayers.Player).add(getRandomCard());
                    this.getHand(GamePlayers.Dealer).add(getRandomCard());
                }

                // Ausgabe des aktuellen Spielstands
                System.out.println("\n--- Karten wurden ausgeteilt ---");
                System.out.println("Dealer zeigt: [" + handDealer.get(0) + "]" + " +  [???]");
                System.out.println("Dein Blatt:   " + formatHandToString(GamePlayers.Player));
                System.out.println("Deine Punkte: " + calculateHand(GamePlayers.Player));

                this.gamePhase = GamePhases.Decision;
            }

            if (this.gamePhase == GamePhases.Decision) {
                this.playerTurn();
                if (calculateHand(GamePlayers.Player) <= 21) {
                    this.dealerTurn();
                }
                this.gamePhase = GamePhases.Stand;
            }

            if (this.gamePhase == GamePhases.Stand) {
                System.out.println("\n--- Endstand ---");
                System.out.println("Dealer Blatt:   " + formatHandToString(GamePlayers.Dealer));
                System.out.println("Dealer Punkte: " + calculateHand(GamePlayers.Dealer));
                System.out.println("Dein Blatt:   " + formatHandToString(GamePlayers.Player));
                System.out.println("Deine Punkte: " + calculateHand(GamePlayers.Player));
                this.gamePhase = GamePhases.EndAndPay;
            }

            if (this.gamePhase == GamePhases.EndAndPay) {
                System.out.println("\n--- Ergebnis ---");
                GameResults gameResult = gameResult();
                
                if (gameResult == GameResults.Win) {
                    System.out.println("Du hast gewonnen \\o/");
                    System.out.println("Dir werden " + this.getPlayerBet() * 2 + " Jetons gutgeschrieben.");
                    super.spieler.addJetons(this.getPlayerBet() * 2);
                }
                if (gameResult == GameResults.Lose) {
                    System.out.println("Du hast leider verloren.");
                    System.out.println("Deine gesetzten Jetons werden eingezogen");
                }
                if (gameResult == GameResults.Draw) {
                    System.out.println("Ein Unentschieden.");
                    System.out.println("Deine gesetzten Jetons werden dir zurückerstattet.");
                    super.spieler.addJetons(this.getPlayerBet());
                }

                if (super.spieler.getJetons() <= 0) {
                    System.out.println("Du hast nicht mehr genug Jetons um weiter zu spielen.");
                    return "Not enough jetons";
                }

                System.out.println("Möchtest du 'start' (neues Spiel) oder 'exit' (Black Jack verlassen)?");
                String input = null;
                boolean runLoop = true;
                while (runLoop) {
                    input = this.sc.nextLine();
                    if (!input.equals("start") && !input.equals("exit")) {
                        System.out.println("Bitte gib 'start' oder 'exit' ein.");
                        continue;
                    }
                    runLoop = false;
                }

                if (input != null && input.equals("start")) {
                    this.resetDeck();
                    for (GamePlayers player : GamePlayers.values()) {
                        this.resetHand(player);
                    }

                    System.out.println("\n --- Restart Game ---");
                    System.out.println("Du verfügst aktuell über " + super.spieler.getJetons() + " Jetons");
                    System.out.println("Bitte gib an wie viele Jetons du setzen möchtest: ");
                    eingabe = this.sc.nextLine();
                    this.gamePhase = GamePhases.WelcomeAndBet;
                    continue;
                }

                System.out.println("Bis zum nächsten mal bei Black Jack!");
                return "End Game";
            }
        }
        
        return "This statement should not be reached";
    }

    @Override
    public void neuesSpiel() {
        // Is not neeeded
    }
}
