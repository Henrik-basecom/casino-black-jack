public class BlackJackGame extends CasinospielBasis{
    String[] cards;
    String[] prefix;
    String[] deck;
    String[] handPlayer;
    String[] handDealer;
    int playerBet;


    public BlackJackGame(String name, Spieler spieler) {
        super(name, spieler);
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
        return "";
    }

    @Override
    public String verarbeiteEingabe(String eingabe) {
        return "";
    }

    @Override
    public void neuesSpiel() {

    }
}
