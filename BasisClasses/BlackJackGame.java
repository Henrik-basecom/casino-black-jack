import java.util.ArrayList;

public class BlackJackGame extends CasinospielBasis{
    private GamePhases gamePhase = GamePhases.WelcomeAndBet;
    private String[] cards = {"2","3","4","5","6","7","8","9","10","B","D","K","A"};
    private String[] prefixs = {"♠","♥","♦","♣"};
    private ArrayList<String> deck = new ArrayList<String>();
    private ArrayList<String> handPlayer = new ArrayList<String>();
    private ArrayList<String> handDealer =  new ArrayList<String>();
    private int playerBet;
    private StringBuilder output = new StringBuilder();


    public BlackJackGame(Spieler spieler) {
        super("BlackJack", spieler);
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }


    private int getPlayerBet() {
        return playerBet;
    }

    private void setPlayerBet(int playerBet) {
          this.playerBet = playerBet;
    }

    private ArrayList<String> getHand(GamePlayers player) {
        if (player == GamePlayers.Player) {
            return this.handPlayer;
        }
        if (player == GamePlayers.Dealer) {
            return this.handDealer;
        }
        return new ArrayList<String>();
    }

    private String getRandomCard() {
        if (deck.isEmpty()) {
            resetDeck();
        }
        int randomIndex = (int) (Math.random() * deck.size());
        String card = deck.get(randomIndex);
        deck.remove(randomIndex);
        return card;
    }

    private void resetDeck(){
        deck.clear();
        for (String prefix : prefixs) {
            for (String card : cards) {
                deck.add(prefix + " " + card);
            }
        }
    }

    private void resetHand(GamePlayers player){
        if (player == GamePlayers.Player) {
            this.handPlayer.clear();
        }
        if (player == GamePlayers.Dealer) {
            this.handDealer.clear();
        }
    } 

    private int calculateHand(GamePlayers player) {
        ArrayList<String> targetHand = this.getHand(player);
        int assCount = 0;
        int tempResult = 0;

        for (String card : targetHand) {
            String cardValue = card.split(" ")[1]; // Extrahieren des Kartenwerts (z.B. "♥ 10" → "10")
            switch (cardValue) {
                case "A":   assCount++; break; // Ass 1 o. 11
                case "B":
                case "D":
                case "K": tempResult += 10; break;
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

    // Funktion gibt true zurück wenn im Interface der output ausgegeben werden soll. False wenn der Code erstmal weiter laufen kann
    private boolean playerTurn(String eingabe) {
        output.append("\n--- Spielerzug Start ---\n\n");
        output.append("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?\n");

        if (this.gamePhase != GamePhases.PlayerDecision) {
            this.gamePhase = GamePhases.PlayerDecision;
            return true;
        }
        
        if (!eingabe.equals("hit") && !eingabe.equals("stand")) {
            this.output.append("Die Vorherige eingabe war falsch!");
            return true;
        }

        if (eingabe.equals("hit")) {
            String rndCard = this.getRandomCard();
            this.output.append("\nDu ziehst:\n");
            this.output.append("\n" + this.formatHandToString(rndCard) + "\n");
            this.getHand(GamePlayers.Player).add(rndCard);
            this.output.append("Dein Blatt:\n");
            this.output.append("\n" + this.formatHandToString(GamePlayers.Player) + "\n");
            this.output.append("Deine Punkte: " + calculateHand(GamePlayers.Player) + "\n\n");
            if (this.calculateHand(GamePlayers.Player) > 21) {
                return false;
            }
            this.output.append("Möchtest du 'hit' (weitere Karte) oder 'stand' (bleiben)?\n");
            return true;
        }
        this.output.append("\n --- Spielerzug Ende ---\n");
        return false;
    }

    private void dealerTurn(){
        this.output.append("\n--- Dealerzug ---\n");
        while (this.calculateHand(GamePlayers.Dealer) < this.calculateHand(GamePlayers.Player)) {
            this.handDealer.add(getRandomCard());
        }
    }

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
            String parts[] = card.split(" ");
            String prefix = parts[0];
            String value = parts[1];
            result[0] += "┌───────┐ ";
            result[1] += String.format("│%s     │ ", String.format("%-2s", value));
            result[2] += String.format("│   %s   │ ", prefix);
            result[3] += String.format("│     %s│ ", String.format("%2s", value));;
            result[4] += "└───────┘ ";
        }

        return String.join("\n", result) + "\n";
    }

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

    @Override
    public String verarbeiteEingabe(String eingabe) {
        this.output = new StringBuilder();

        if (this.gamePhase == GamePhases.TestRestart) {
            if (!eingabe.equals("start") && !eingabe.equals("exit")) {
                    this.output.append("Bitte gib 'start' oder 'exit' ein.");;
                    return this.output.toString();
            }
            if (eingabe.equals("exit")) {
                this.output.append("Bis zum nächsten mal bei Black Jack!");
                return this.output.toString();
            }

            this.output.append("\n --- Restart Game ---\n\n");
            this.output.append("Du verfügst aktuell über " + super.spieler.getJetons() + " Jetons\n");
            this.output.append("Bitte gib an wie viele Jetons du setzen möchtest: ");
            this.gamePhase = GamePhases.WelcomeAndBet;
            return this.output.toString();
        }

        if (this.gamePhase == GamePhases.WelcomeAndBet) {
            if (super.spieler.getJetons() <= 0) {
                return "Du hast nicht mehr genug Jetons um weiter zu spielen!";
            }

            int numInput = -1;

            try {
                numInput = Integer.parseInt(eingabe);
            } catch (NumberFormatException e) {
                return "Bitte gib nur eine Zahl an!";
            }

            if (numInput > super.spieler.getJetons() || numInput <= 0) {
                this.output.append("Du musst mindestens 1 Jeton setzen!\n");
                this.output.append("Du kannst nur maximal " + super.spieler.getJetons() + " Jetons setzen!");
                return this.output.toString();
            }

            this.setPlayerBet(numInput);
            super.spieler.removeJetons(this.playerBet);
            this.output.append("Du hast " + numInput + " Jetons gesetzt.");
            this.gamePhase = GamePhases.GetCards;
        }


        if (this.gamePhase == GamePhases.GetCards) {
            this.resetDeck();
            for (GamePlayers player : GamePlayers.values()) {
                this.resetHand(player);
            }

            // Dealer und Spieler erhalten jeweils 2 Karten
            for (int i = 0; i < 2; i++) {
                this.getHand(GamePlayers.Player).add(getRandomCard());
                this.getHand(GamePlayers.Dealer).add(getRandomCard());
            }

            // Ausgabe des aktuellen Spielstands
            this.output.append("\n\n--- Karten werden ausgeteilt ---\n\n");
            this.output.append("Dealer zeigt:\n");
            this.output.append("\n" + this.formatHandToString(new String[] {this.handDealer.get(0), "? ?"}) + "\n");
            this.output.append("Dein Blatt:\n");
            this.output.append("\n" + this.formatHandToString(GamePlayers.Player) + "\n");
            this.output.append("Deine Punkte: " + this.calculateHand(GamePlayers.Player));
            this.output.append("\n\n--- Karten wurden ausgeteilt ---\n");

            this.gamePhase = GamePhases.Decision;
        }

        if (this.gamePhase == GamePhases.Decision || this.gamePhase == GamePhases.PlayerDecision) {
            boolean tempVal = this.playerTurn(eingabe);
            if (tempVal) {
                return this.output.toString();
            }

            if (calculateHand(GamePlayers.Player) <= 21) {
                this.dealerTurn();
            }
            this.gamePhase = GamePhases.Stand;
        }

        if (this.gamePhase == GamePhases.Stand) {
            this.output.append("\n--- Endstand ---\n\n");
            this.output.append("Dealer Blatt:\n");
            this.output.append("\n" + formatHandToString(GamePlayers.Dealer) + "\n");
            this.output.append("Dealer Punkte: " + calculateHand(GamePlayers.Dealer) + "\n");
            this.output.append("\nDein Blatt:\n");
            this.output.append("\n" + formatHandToString(GamePlayers.Player) + "\n");
            this.output.append("Deine Punkte: " + calculateHand(GamePlayers.Player) + "\n");
            this.output.append("\n--- Endstand ---\n");
            this.gamePhase = GamePhases.EndAndPay;
        }

        if (this.gamePhase == GamePhases.EndAndPay) {
            this.output.append("\n--- Ergebnis ---\n\n");
            GameResults gameResult = gameResult();
            
            if (gameResult == GameResults.Win) {
                this.output.append("Du hast gewonnen \\o/\n");
                this.output.append("Dir werden " + this.getPlayerBet() * 2 + " Jetons gutgeschrieben.");
                super.spieler.addJetons(this.getPlayerBet() * 2);
            }
            if (gameResult == GameResults.Lose) {
                this.output.append("Du hast leider verloren.\n");
                this.output.append("Deine gesetzten Jetons werden eingezogen");
            }
            if (gameResult == GameResults.Draw) {
                this.output.append("Ein Unentschieden.\n");
                this.output.append("Deine gesetzten Jetons werden dir zurückerstattet.");
                super.spieler.addJetons(this.getPlayerBet());
            }
            this.output.append("\n\n--- Ergebnis ---\n\n");

            if (super.spieler.getJetons() <= 0) {
                this.output.append("Du hast nicht mehr genug Jetons um weiter zu spielen.");
                this.gamePhase = GamePhases.WelcomeAndBet;
                return this.output.toString();
            }

            this.output.append("Möchtest du 'start' (neues Spiel) oder 'exit' (Black Jack verlassen)?");;
            this.gamePhase = GamePhases.TestRestart;
            return this.output.toString();
        }
        
        return "This statement should not be reached";
    }

    @Override
    public void neuesSpiel() {
        // Is not neeeded
    }
}
