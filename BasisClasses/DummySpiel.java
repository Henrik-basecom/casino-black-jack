public class DummySpiel extends CasinospielBasis {

    public DummySpiel(Spieler spieler) {
        super("Dummy-Spiel", spieler);
    }

    @Override
    public String ersteNachricht() {
        return "Willkommen im Dummy-Spiel.\n" +
               "Verfügbare Befehle:\n" +
               "- 'plus X'  → fügt X Jetons hinzu\n" +
               "- 'minus X' → zieht X Jetons ab\n" +
               "- 'stand'   → zeigt aktuellen Jetonstand\n";
    }

    @Override
    public String verarbeiteEingabe(String eingabe) {
        if (eingabe.toLowerCase().startsWith("plus ")) {
            try {
                int betrag = Integer.parseInt(eingabe.substring(5).trim());
                spieler.addJetons(betrag);
                return "Jetons erhöht. Neuer Stand: " + spieler.getJetons();
            } catch (NumberFormatException e) {
                return "Ungültiger Betrag bei 'plus'.";
            }
        } else if (eingabe.toLowerCase().startsWith("minus ")) {
            try {
                int betrag = Integer.parseInt(eingabe.substring(6).trim());
                spieler.removeJetons(betrag);
                return "Jetons reduziert. Neuer Stand: " + spieler.getJetons();
            } catch (NumberFormatException e) {
                return "Ungültiger Betrag bei 'minus'.";
            }
        } else if (eingabe.equalsIgnoreCase("stand")) {
            return "Aktueller Jetonstand: " + spieler.getJetons();
        } else {
            return "Unbekannter Befehl. Nutze 'plus X', 'minus X' oder 'stand'.";
        }
    }

    /**
     * Setzt den Spielzustand zurück.
     * Wird z.B. aufgerufen, wenn der Spieler den Tab wechselt.
     * Für das Dummy-Spiel ist kein interner Zustand nötig.
     */
    @Override
    public void neuesSpiel() {
        // Kein Zustand zu speichern – Methode bleibt leer
    }
}
