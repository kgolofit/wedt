/* 
 * Created by Kamil on 13 sty 2015 00:21:50
 */
package wedt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wedt.exception.PseudoDocNotInitializedException;
import wedt.search.Link;
import wedt.similarityComputation.ContextSimilarityComputation;
import wedt.similarityComputation.DocumentSimilarityComputation;
import wedt.similarityComputation.PseudoDoc;
import wedt.util.Synonym;

public class RDESFramework
{
    /**
     * paruje (fraza, kandydat) i wylicza funkcje jakosci podobienstwa
     * 
     * @return lista sparowanych (fraza, kandydat) wraz z wyliczonymi funkcjami jakosci podobienstwa
     */
    public static List<Synonym> discoverSynonyms(String phrase, String[] candidates, Map<String, List<Link>> documents)
    {
        List<Synonym> synonyms = new ArrayList<Synonym>();
        
        Map<String, List<String>> pseudoDocs;
        try
        {
            pseudoDocs = PseudoDoc.getPseudoDocs();
        } 
        catch (PseudoDocNotInitializedException e)
        {
            e.printStackTrace();
            return null;
        }
        
        for(String candidate : candidates)
        {
            double eTocDocSim = DocumentSimilarityComputation.computeDocSim(phrase, candidate, documents.get(candidate), pseudoDocs);
            double cToeDocSim = DocumentSimilarityComputation.computeDocSim(candidate, phrase, documents.get(phrase), pseudoDocs);
            double contextSim = ContextSimilarityComputation.computeContextSim(phrase, documents.get(phrase), candidate, documents.get(candidate));
            
            Synonym s = new Synonym(phrase, candidate, eTocDocSim, cToeDocSim, contextSim);
            synonyms.add(s);
        }
        
        return synonyms;
    }
    
    /**
     * opakowanie do przeciazonej metory 
     */
    public static Synonym discoverAndGetSynonym(String phrase, String[] candidates, Map<String, List<Link>> documents)
    {
        return RDESFramework.discoverAndGetSynonym(phrase, candidates, documents, 0d);
    }
    
    /**
     * Odkrywa synonimy - dla kazdej z par (fraza, kandydat) wyliczane sa funkcje jakosci prawdopodobienstwa
     * na podstawie ktorych wybierany jest najlepszy kandydat na synonim. 
     * Wartosci trzech funkcji jakosci podobienstwa sa sumowane i sprawdzany jest warunek czy suma jest wieksza niz wartosc progowa (odcinanie 'slabych' kandydatow).
     * Z pozostalych kandydatow zwracany jest najlepszy jako synonim.
     * 
     * W razie braku kandydatow spelniajacych warunek odciecia zwracany jest null.
     * 
     * @param phrase - fraza do wyszukania synonimu
     * @param candidates - kandydaci na synonimy
     * @param documents - dokumenty (zebrane z wyszukiwarki internetowej)
     * @param treshold - parametr odciecia
     * @return najlepszy kandydat na synonim lub null
     */
    public static Synonym discoverAndGetSynonym(String phrase, String[] candidates, Map<String, List<Link>> documents, double treshold)
    {
        System.out.println("=================================================================================");
        List<Synonym> synonyms = discoverSynonyms(phrase, candidates, documents);
        
        Synonym mainCandidate = null;
        double maxSummarySim = 0d;
        
        for(Synonym s : synonyms)
        {
            double sum = s.getE_to_c_Sim() + s.getC_to_e_Sim() + s.getContextSim();
            
            System.out.println("\tPhrase: " + phrase.toUpperCase() + ", potential_synonym: " + s.getS2() + "\t\tsumSim=" + sum);
            
            // jesli funkcja jakosci podobienstwa jest wieksza niz wartosc progowa (jesli bierzemy to w ogole pod uwage)
            if(sum >= treshold)
            {
                if(sum > maxSummarySim)
                {
                    maxSummarySim = sum;
                    mainCandidate = s;
                }
            }
        }
        
        if(mainCandidate != null)
        {
            System.out.println("=================================================================================");
            System.out.println("Main synonym for " + phrase.toUpperCase() + " is " + mainCandidate.getS2().toUpperCase() + " with similarity " + maxSummarySim);
        }
        
        return mainCandidate;
    }
}
