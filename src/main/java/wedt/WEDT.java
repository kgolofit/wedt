/* 
 * Created by Kamil on 11 sty 2015 12:02:09
 */
package wedt;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wedt.search.GoogleSearch;
import wedt.search.Link;
import wedt.search.WebSearch;
import wedt.similarityComputation.PseudoDoc;
import wedt.util.ConfigFile;
import wedt.util.ConfigFileLoader;
import wedt.util.Synonym;
import wedt.util.Utils;

public class WEDT
{
    public static void main(String[] args)
    {
        doTheMagic();
    }
    
    private static void doTheMagic()
    {
        /* 
         * 1. Nie generujemy kandydatow bo sa one przez nas podane - zadajemy fraze oraz pare potencjalnych synonimow
         * 2. Generujemy pseudodokumenty dla analizowanych fraz
         * 3. Na podstawie rownan [2] i [3] liczymy podobienstwo E-to-C i C-to-E (podobieństwo pseudo-dokumentowe)
         * 4. Na podstawie rownania [4] liczymy podobienstwo kontekstowe
         * 5. Filtrujemy kandydatow na synonimy
         * 
         * Podczas pracy programu wyniki zapisywane sa do pliku wyjsciowego (w katalogu ./out/).
         * Dodatkowe informacje podawane sa podczas pracy programu do konsoli.
         */
        
        // zaladowanie parametrow z pliku wejsciowego (konfiguracja)
        ConfigFile cf = ConfigFileLoader.loadConfigFile();
        double simTreshold = cf.getSimTreshold();
        String[] allPhrases = cf.getAllPhrases();
        
        // inicjalizacja wyszukiwarki - pobranie wynikow szukania odpowiednich fraz z google
        // jako ze google search api ma limit dzienny 200 requests dla kazdego zapytania wyniki (urle - dokumenty) sa przetrzymywane w pamieci i serializowane do pliku
        WebSearch search = new GoogleSearch();
        Map<String, List<Link>> queriesResponse = search.getLinks(allPhrases);
        search.saveToFile(); // zapisujemy dokumenty z pamieci do pliku (ew nowe pobrane wyniki zapytan sa serializowane)
        
        search.printAllDocumentsToFile(); // TODO to tylko jednorazowo

        // podobnie jak z dokumentami, pseudo-dokumenty sa przechowywane w pamieci i serializowane do pliku. 
        // W razie braku pseudo-dokumentu dla zadanego dokumentu (urla) nastepuje ponowne wygenerowanie pseudo-dokumentow.
        @SuppressWarnings("unused") // inicjalizacja pseudo-dokumentow
        PseudoDoc pseudoDoc = new PseudoDoc(allPhrases, queriesResponse);
        
        pseudoDoc.printAllPseudoDocumentsToFile(); // TODO to tylko jednorazowo
        
        int correct = 0;
        int total = cf.getPhrases().length;
        List<Synonym> synonyms = new ArrayList<Synonym>();
        
        // dla wszystkich fraz wczytanych z pliku, nastepuje proces odkrywania synonimow sposrod podanych kandydatow
        for(int i=0; i<total; i++)
        {
            String phrase = cf.getPhrases()[i];
            String[] currCandidates = cf.getCandidates()[i];
            String retPattern = cf.getResults()[i];
            
            Synonym s = RDESFramework.discoverAndGetSynonym(phrase, currCandidates, queriesResponse, simTreshold);
            if(s != null)
            {
                System.out.println(i+1 + ": Main candidate for " + s.getS1().toUpperCase() + " is " + s.getS2().toUpperCase() + "\t " + (retPattern.equals(s.getS2()) ? "OK" : "FAIL"));
                synonyms.add(s);
                if(retPattern.equals(s.getS2()))
                    correct++;
            }
        }
        
        // wyliczana ogolna jakosc
        double quality = (correct*100 / (double)total);
        System.out.println("========================================");
        System.out.println("Synonim discover quality for requested examples is " + quality + "%");
        
        // zapis wynikow do outa
        printResultsToOutFile(cf.getPhrases(), synonyms, simTreshold, quality);
    }

    private static void printResultsToOutFile(String[] phrases, List<Synonym> synonyms, double simTreshold, double quality)
    {
        String outFileName = Utils.getFileNameToDumpOut("txt");
        PrintWriter pw = null;
        
        try
        {
            pw = new PrintWriter(outFileName);
            pw.println("Parametr treshold=" + simTreshold + "; fcja jakosci pnstwa zawiera sie w zbiorze [0;2.5]");
            pw.println("=============================================================================");
            
            for(String phrase : phrases)
            {
                boolean ok = false;
                
                for(Synonym s : synonyms)
                {
                    if(phrase.equals(s.getS1()))
                    {
                        ok = true;
                        pw.println("Fraza: " + s.getS1().toUpperCase() + ", znaleziony synonim: " + s.getS2().toUpperCase() + ", fcja jako�ci podobie�stwa[EtoC+CtoE+contextSim] = " + s.getE_to_c_Sim() + "+" + s.getC_to_e_Sim() + "+" + s.getContextSim() + " = " + (s.getE_to_c_Sim() + s.getC_to_e_Sim() + s.getContextSim()));
                    }
                }
                
                if(!ok)
                    pw.println("Fraza: " + phrase.toUpperCase() + ", nie znaleziono synonimu .");
            }
            
            pw.println("=============================================================================");
            pw.println("Ogolna jakosc = " + quality + "%");
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(pw != null)
                pw.close();
        }
    }
}
