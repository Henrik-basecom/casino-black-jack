import java.util.Scanner;

public class BlackJackGame extends CasinospielBasis{
    private Scanner sc = new Scanner(System.in);
    private GamePhases gamePhase = GamePhases.WelcomeAndBet;
    private String[] cards;
    private String[] prefix;
    private String[] deck;
    private String[] handPlayer;
    private String[] handDealer;
    private int playerBet;


    public BlackJackGame(Spieler spieler) {
        super("BlackJack", spieler);
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

    public String[] getDeck() {
        return deck;
    }

    public String getRandomCard() {
        return null;
    }

    public void removeDeckCard(String card){

    }

    public void addCardToHand(String card, String hand){

    }

    public void resetDeck(){

    }

    public void resetHand(){

    }

    public int calculateHand(String hand){
        return 1;
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
        return "Willkommen bei BlackJack " + super.spieler.getName() + "\n"
        + "Du verfügst aktuell über " + super.spieler.getJetons() + " Jetons\n"
        + "Bitte gib an wie viele Jetons du setzen möchtest: ";
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
                if (numInput > super.spieler.getJetons()) {
                    System.err.println("Du kannst nur maximal " + super.spieler.getJetons() + " Jetons setzen!");
                    System.out.println("Neue Eingabe: ");
                    eingabe = sc.nextLine();
                    continue;
                }
                checkInput = false;
            }
            System.out.println("Erfolgreich " + numInput);
        }
        return "";
    }

    @Override
    public void neuesSpiel() {

    }
}
