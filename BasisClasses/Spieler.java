/**
 * Repräsentiert einen Spieler mit Namen und Jetonkonto.
 * Wird vom Nutzerinterface erstellt und an die Spiele übergeben.
 */
public class Spieler {
    private final String name;
    private int jetons;

    /**
     * Erstellt einen neuen Spieler mit Name und Startjetons.
     *
     * @param name Name des Spielers
     * @param startJetons Startwert der Jetons
     */
    public Spieler(String name, int startJetons) {
        this.name = name;
        this.jetons = startJetons;
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return Spielername
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die aktuelle Jetonanzahl zurück.
     *
     * @return Anzahl der Jetons
     */
    public int getJetons() {
        return jetons;
    }

    /**
     * Erhöht den Jetonstand um den angegebenen Betrag.
     *
     * @param betrag Jetons, die hinzugefügt werden sollen
     */
    public void addJetons(int betrag) {
        this.jetons += betrag;
    }

    /**
     * Verringert den Jetonstand um den angegebenen Betrag.
     * Der Stand wird dabei nicht unter 0 gesenkt.
     *
     * @param betrag Jetons, die abgezogen werden sollen. Schulden sind möglich.
     */
    public void removeJetons(int betrag) {
        this.jetons -= betrag;
    }
}