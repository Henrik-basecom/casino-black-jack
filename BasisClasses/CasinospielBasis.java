/**
 * Basisklasse für alle Casinospiele.
 * Stellt gemeinsame Eigenschaften und Methoden bereit.
 */
public abstract class CasinospielBasis {

    // Referenz auf den Spieler, der das Spiel spielt
    protected Spieler spieler;

    // Name des Spiels (z. B. "Roulette")
    private final String name;

    /**
     * Erstellt ein neues Casinospiel mit Name und Spieler.
     *
     * @param name    Spielname
     * @param spieler Spielerobjekt
     */
    public CasinospielBasis(String name, Spieler spieler) {
        this.name = name;
        this.spieler = spieler;
    }

    /**
     * Gibt den Namen des Spiels zurück.
     *
     * @return Spielname
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die erste Nachricht des Spiels zurück (z. B. Begrüßung oder Anleitung).
     *
     * @return Einstiegstext
     */
    public abstract String ersteNachricht();

    /**
     * Verarbeitet die Eingabe des Spielers und gibt eine Antwort zurück.
     *
     * @param eingabe Nutzereingabe
     * @return Textausgabe des Spiels
     */
    public abstract String verarbeiteEingabe(String eingabe);

    /**
     * Setzt das Spiel vollständig zurück.
     */
    public abstract void neuesSpiel();
}