import java.util.ArrayList;
import java.util.Scanner;

/**
 * BlackJackGame implementiert ein vollständiges Black Jack Spiel,
 * das von der Basisklasse CasinospielBasis erbt.
 */
public class BlackJackGame extends CasinospielBasis {

    // Eingabescanner für Benutzereingaben
    private Scanner sc = new Scanner(System.in);

    // Aktuelle Spielphase
    private GamePhases gamePhase = GamePhases.WelcomeAndBet;

    // Kartenwerte (2-10, Bube, Dame, König, Ass)
    private String[] cards = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "B", "D", "K", "A"};

    // Kartensymbole für die vier Farben
    private String[] prefixs = {"♠", "♥", "♦", "♣"};

    // Kartendeck
    private ArrayList<String> deck = new ArrayList<>();

    // Karten auf der Hand des Spielers
    private ArrayList<String> handPlayer = new ArrayList<>();

    // Karten auf der Hand des Dealers
    private ArrayList<String> handDealer = new ArrayList<>();

    // Aktueller Einsatz des Spielers
    private int playerBet;

    // Konstruktor initialisiert das Spiel und erstellt das Kartendeck
    public BlackJackGame(Spieler spieler) {
        super("BlackJack", spieler);
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }

    // Getter und Setter für den Einsatz
    private int getPlayerBet() {
        return playerBet;
    }

    private void setPlayerBet(int playerBet) {
        this.playerBet = playerBet;
    }

    // Gibt die Hand des angegebenen Spielers zurück
    private ArrayList<String> getHand(GamePlayers player) {
        if (player == GamePlayers.Player) return this.handPlayer;
        if (player == GamePlayers.Dealer) return this.handDealer;
        return new ArrayList<>();
    }

    // Gibt eine zufällige Karte vom Deck zurück und entfernt sie daraus
    private String getRandomCard() {
        if (deck.isEmpty()) resetDeck();
        int randomIndex = (int) (Math.random() * deck.size());
        String card = deck.get(randomIndex);
        deck.remove(randomIndex);
        return card;
    }

    // Setzt das Deck auf den Ursprungszustand zurück
    private void resetDeck() {
        deck.clear();
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }

    // Leert die Hand eines Spielers
    private void resetHand(GamePlayers player) {
        if (player == GamePlayers.Player) this.handPlayer.clear();
        if (player == GamePlayers.Dealer) this.handDealer.clear();
    }

    // Berechnet die Punktzahl der Hand eines Spielers unter Berücksichtigung der Ass-Regel
    private int calculateHand(GamePlayers player) {
        ArrayList<String> targetHand = this.getHand(player);
        int assCount = 0;
        int tempResult = 0;

        for (String card : targetHand) {
            String cardValue = card.split(" ")[1];
            switch (cardValue) {
                case "A": assCount++; break;
                case "B":
                case "D":
                case "K": tempResult += 10; break;
                default: tempResult += Integer.parseInt(cardValue);
            }
        }

        int result = tempResult + assCount;
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

    // Ermittelt den Spielausgang anhand der Punktzahlen
    private GameResults gameResult() {
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

    // Ablauf des Spielerzugs: Eingabe von "hit" oder "stand"
    private void playerTurn() {
        this.delayAfterPrintln("\n --- Spielerzug Start ---\n");
        this.delayAfterPrintln("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?");

        boolean runLoop = true;
        while (runLoop) {
            String input = this.sc.nextLine();

            if (!input.equals("hit") && !input.equals("stand")) {
                this.delayAfterPrintln("Bitte gib 'hit' oder 'stand' ein.");
                continue;
            }

            if (input.equals("hit")) {
                String rndCard = this.getRandomCard();
                this.delayAfterPrintln("Du ziehst:");
                System.out.println(this.formatHandToString(rndCard));
                this.getHand(GamePlayers.Player).add(rndCard);
                this.delayAfterPrintln("Dein Blatt:");
                System.out.println(this.formatHandToString(GamePlayers.Player));
                this.delayAfterPrintln("Deine Punkte: " + calculateHand(GamePlayers.Player));
                if (this.calculateHand(GamePlayers.Player) > 21) {
                    runLoop = false;
                    break;
                }
                this.delayAfterPrintln("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?");
                continue;
            }

            runLoop = false;
        }
        this.delayAfterPrintln("\n --- Spielerzug Ende ---\n");
    }

    // Dealer zieht Karten solange seine Punktzahl niedriger ist als die des Spielers
    private void dealerTurn() {
        this.delayAfterPrintln("\n--- Dealerzug ---\n");
        while (this.calculateHand(GamePlayers.Dealer) < this.calculateHand(GamePlayers.Player)) {
            this.handDealer.add(getRandomCard());
        }
    }

    // Prüft und validiert die Jeton-Eingabe des Spielers
    private int checkJetonInput(String input) {
        boolean checkInput = true;
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
            if (numInput > super.spieler.getJetons() || numInput <= 0) {
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

    // Hilfsmethoden für Pausen zwischen Ausgaben
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void delayAfterPrintln(String message) {
        this.delayAfterPrintln(message, 500);
    }

    private void delayAfterPrintln(String message, int millisec) {
        System.out.println(message);
        this.delay(millisec);
    }

    // Formatierte Darstellung einzelner oder mehrerer Karten
    private String formatHandToString(String input) {
        String[] tempVal = {input};
        return this.formatHandToString(tempVal);
    }

    private String formatHandToString(GamePlayers player) {
        return this.formatHandToString(this.getHand(player).toArray(new String[0]));
    }

    private String formatHandToString(String[] input) {
        String[] result = {"", "", "", "", ""};

        for (String card : input) {
            String[] parts = card.split(" ");
            String prefix = parts[0];
            String value = parts[1];
            result[0] += "┌───────┐ ";
            result[1] += String.format("│%s     │ ", String.format("%-2s", value));
            result[2] += String.format("│   %s   │ ", prefix);
            result[3] += String.format("│     %s│ ", String.format("%2s", value));
            result[4] += "└───────┘ ";
        }

        return String.join("\n", result) + "\n";
    }

    // Begrüßungsnachricht beim Spielstart
    @Override
    public String ersteNachricht() {
        return super.spieler.getName() + ", Willkommen bei \n" +
                "┌───────┐   ┌───────┐\n" +
                "│10     │   │A      │\n" +
                "│   ♠   │   │  ♥    │\n" +
                "│     10│   │      A│\n" +
                "└───────┘   └───────┘\n" +
                " B L A C K J A C K\n" +
                "\nDu verfügst aktuell über " + super.spieler.getJetons() + " Jetons\n" +
                "Bitte gib an wie viele Jetons du setzen möchtest: ";
    }

    // Zentrale Methode zur Eingabeverarbeitung und Ablaufsteuerung des Spiels
    @Override
    public String verarbeiteEingabe(String eingabe) {
        boolean mainGameLoop = true;

        while (mainGameLoop) {
            if (this.gamePhase == GamePhases.WelcomeAndBet) {
                int numInput = this.checkJetonInput(eingabe);
                this.setPlayerBet(numInput);
                super.spieler.removeJetons(this.playerBet);
                this.delayAfterPrintln("Du hast " + numInput + " Jetons gesetzt.");
                this.gamePhase = GamePhases.GetCards;
            }

            if (this.gamePhase == GamePhases.GetCards) {
                for (int i = 0; i < 2; i++) {
                    this.getHand(GamePlayers.Player).add(getRandomCard());
                    this.getHand(GamePlayers.Dealer).add(getRandomCard());
                }

                this.delayAfterPrintln("\n--- Karten werden ausgeteilt ---\n");
                this.delayAfterPrintln("Dealer zeigt:");
                System.out.println(this.formatHandToString(new String[] {this.handDealer.get(0), "? ?"}));
                this.delayAfterPrintln("Dein Blatt:");
                System.out.println(this.formatHandToString(GamePlayers.Player));
                this.delayAfterPrintln("Deine Punkte: " + this.calculateHand(GamePlayers.Player));
                this.delayAfterPrintln("\n--- Karten wurden ausgeteilt ---\n");

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
                this.delayAfterPrintln("\n--- Endstand ---\n");
                this.delayAfterPrintln("Dealer Blatt:");
                System.out.println(formatHandToString(GamePlayers.Dealer));
                this.delayAfterPrintln("Dealer Punkte: " + calculateHand(GamePlayers.Dealer));
                this.delayAfterPrintln("\nDein Blatt:");
                System.out.println(formatHandToString(GamePlayers.Player));
                this.delayAfterPrintln("Deine Punkte: " + calculateHand(GamePlayers.Player));
                this.delayAfterPrintln("\n--- Endstand ---\n");
                this.gamePhase = GamePhases.EndAndPay;
            }

            if (this.gamePhase == GamePhases.EndAndPay) {
                this.delayAfterPrintln("\n--- Ergebnis ---\n");
                GameResults gameResult = gameResult();

                if (gameResult == GameResults.Win) {
                    this.delayAfterPrintln("Du hast gewonnen \\o/");
                    this.delayAfterPrintln("Dir werden " + this.getPlayerBet() * 2 + " Jetons gutgeschrieben.");
                    super.spieler.addJetons(this.getPlayerBet() * 2);
                }
                if (gameResult == GameResults.Lose) {
                    this.delayAfterPrintln("Du hast leider verloren.");
                }
                if (gameResult == GameResults.Draw) {
                    this.delayAfterPrintln("Ein Unentschieden.");
                    this.delayAfterPrintln("Deine gesetzten Jetons werden dir zurückerstattet.");
                    super.spieler.addJetons(this.getPlayerBet());
                }

                this.delayAfterPrintln("\n--- Ergebnis ---\n");

                if (super.spieler.getJetons() <= 0) {
                    this.delayAfterPrintln("Du hast nicht mehr genug Jetons um weiter zu spielen.");
                    return "Not enough jetons";
                }

                this.delayAfterPrintln("Möchtest du 'start' (neues Spiel) oder 'exit' (Black Jack verlassen)?");
                String input = null;
                boolean runLoop = true;
                while (runLoop) {
                    input = this.sc.nextLine();
                    if (!input.equals("start") && !input.equals("exit")) {
                        this.delayAfterPrintln("Bitte gib 'start' oder 'exit' ein.");
                        continue;
                    }
                    runLoop = false;
                }

                if (input != null && input.equals("start")) {
                    this.resetDeck();
                    for (GamePlayers player : GamePlayers.values()) {
                        this.resetHand(player);
                    }

                    this.delayAfterPrintln("\n --- Restart Game ---");
                    this.delayAfterPrintln("Du verfügst aktuell über " + super.spieler.getJetons() + " Jetons");
                    this.delayAfterPrintln("Bitte gib an wie viele Jetons du setzen möchtest: ");
                    eingabe = this.sc.nextLine();
                    this.gamePhase = GamePhases.WelcomeAndBet;
                    continue;
                }

                this.delayAfterPrintln("Bis zum nächsten mal bei Black Jack!");
                return "End Game";
            }
        }

        return "This statement should not be reached";
    }

    @Override
    public void neuesSpiel() {
        // Wird nicht benötigt, da Restart im Ablauf eingebaut ist
    }
}
