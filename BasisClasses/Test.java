import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Spieler henrik = new Spieler("henrik", 100);
        BlackJackGame blackjackgame = new BlackJackGame(henrik);
        System.out.println(blackjackgame.ersteNachricht());
        String input = sc.nextLine();
        blackjackgame.verarbeiteEingabe(input);
    }
}