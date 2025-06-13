import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Spieler henrik = new Spieler("henrik", 100);
        BlackJackGame blackjackgame = new BlackJackGame(henrik);
        System.out.println(blackjackgame.ersteNachricht());
        
        for (int i = 0; i < 100; i++) {
            String input = sc.nextLine();
            String tempOut = blackjackgame.verarbeiteEingabe(input);
            System.out.println(tempOut);
        }
    }
}