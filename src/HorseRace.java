import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class HorseRace {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ottieni la lunghezza del percorso dalla console
        System.out.print("Inserisci la lunghezza del percorso in metri: ");
        int lunghezzaPercorso = scanner.nextInt();
        scanner.nextLine();  // Consuma il newline

        // Ottieni il numero di cavalli dalla console
        System.out.print("Inserisci il numero di cavalli: ");
        int numeroCavalli = scanner.nextInt();
        scanner.nextLine();  // Consuma il newline

        // Crea i cavalli con velocità specificata dall'utente
        List<Cavallo> cavalli = new ArrayList<>();
        for (int i = 0; i < numeroCavalli; i++) {
            System.out.print("Inserisci il nome del cavallo " + (i + 1) + ": ");
            String nome = scanner.nextLine();
            System.out.print("Inserisci la velocità del cavallo (metri al secondo): ");
            int velocita = scanner.nextInt();
            scanner.nextLine();  // Consuma il newline
            cavalli.add(new Cavallo(nome, velocita, lunghezzaPercorso));
        }

        // Avvia la gara
        for (Cavallo cavallo : cavalli) {
            cavallo.start();
        }

        boolean garaFinita = false;
        while (!garaFinita) {
            garaFinita = true;
            for (Cavallo cavallo : cavalli) {
                if (cavallo.isRunning()) {
                    garaFinita = false;
                    break;
                }
            }

            // Mescola casualmente l'ordine dei cavalli
            Collections.shuffle(cavalli);

            // Stampa la posizione dei cavalli
            System.out.println("Posizione attuale:");
            for (Cavallo cavallo : cavalli) {
                System.out.println(cavallo.getNome() + ": " + cavallo.getMetriPercorsi() + " metri");
            }

            try {
                Thread.sleep(1000); // Pausa di 1 secondo tra gli aggiornamenti
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Ordina i cavalli per metri percorsi e mostra i primi 3 classificati
        cavalli.sort(Comparator.comparingInt(Cavallo::getMetriPercorsi).reversed());
        System.out.println("Classifica dei primi 3 cavalli:");
        for (int i = 0; i < Math.min(3, cavalli.size()); i++) {
            Cavallo cavallo = cavalli.get(i);
            System.out.println((i + 1) + "°: " + cavallo.getNome() + " con " + cavallo.getMetriPercorsi() + " metri percorsi");
        }

        // Salva i risultati in un file
        System.out.print("Inserisci il nome del file dove salvare i risultati: ");
        String fileName = scanner.nextLine();

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Classifica gara:\n");
            for (int i = 0; i < Math.min(3, cavalli.size()); i++) {
                Cavallo cavallo = cavalli.get(i);
                writer.write((i + 1) + "°: " + cavallo.getNome() + " con " + cavallo.getMetriPercorsi() + " metri percorsi\n");
            }
            writer.write("\n");
            System.out.println("Risultati salvati nel file " + fileName);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio del file: " + e.getMessage());
        }

        System.out.println("Gara terminata!");
    }
}

class Cavallo extends Thread {
    private String nome;
    private int velocita; // Velocità in metri al secondo
    private int lunghezzaPercorso;
    private int metriPercorsi = 0;
    private boolean running = true;
    private boolean infortunato = false;

    public Cavallo(String nome, int velocita, int lunghezzaPercorso) {
        this.nome = nome;
        this.velocita = velocita;
        this.lunghezzaPercorso = lunghezzaPercorso;
    }

    public String getNome() {
        return nome;
    }

    public int getMetriPercorsi() {
        return metriPercorsi;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while (metriPercorsi < lunghezzaPercorso && !infortunato) {
            if (Math.random() < 0.05) { // 5% di probabilità di infortunio
                infortunato = true;
                running = false;
                System.out.println(nome + " si è infortunato ed è fuori dalla gara!");
                break;
            }

            // Avanza in base alla velocità
            metriPercorsi += velocita;

            try {
                Thread.sleep(1000); // Pausa di 1 secondo per simulare il tempo che passa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (metriPercorsi >= lunghezzaPercorso) {
                running = false;
                System.out.println(nome + " ha terminato la gara!");
            }
        }
    }
}
