import java.io.Console;

public class Test {
    public static void main(String[] args) {
        Spieler henrik = new Spieler("henrik", 100);
        BlackJackGame casinospiel = new BlackJackGame("BlackJack",henrik);
        System.out.println(GamePhases.Decision);
    }
}