/* 
 * Created by Kamil on 13 sty 2015 03:42:47
 */
package wedt.similarityComputation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import wedt.search.Link;

public class PseudoDocResolver
{
    /**
     * Resolver - generuje pseudodokumenty dla dokumentow(urli) dla fraz/kandydatow
     * 
     * @return zbior pseudo-dokuentow
     */
    public static Map<String, List<String>> generatePseudoDocs(String[] candidates, Map<String, List<Link>> documents)
    {
        Map<String, List<String>> pseudoDocs = new HashMap<String, List<String>>();
        
        // lista wszystkich dokumentow ktorymi sie aktualnie zajmujemy
        List<Link> currDocuments = new ArrayList<Link>();
        for(String cPhrase : candidates)
        {
            currDocuments.addAll(documents.get(cPhrase));
        }
        
        // teraz dla kazdego dokumentu ktory nas interesuje
        for(Link doc : currDocuments)
        {
            // przechodzimy po wszystkich dokumentach dla fraz
            for (Map.Entry<String, List<Link>> entry : documents.entrySet())
            {
                List<Link> links = entry.getValue();
                for(Link l : links)
                {
                    if(doc.url.equals(l.url))
                    {
                        String phraseForLinks = entry.getKey();
                        
                        String[] tokens = phraseForLinks.split("\\s+");
                        List<String> currTokens = pseudoDocs.get(doc.url);
                        
                        if(currTokens == null)
                            currTokens = new ArrayList<String>();
                        
                        for(String t : tokens)
                        {
                            if(!currTokens.contains(t))
                            {
                                currTokens.add(t);
                            }
                        }
                        
                        pseudoDocs.put(doc.url, currTokens);
                    }
                }
            }
        }
        
        return pseudoDocs;
    }
    
    /**
     * opakowanie przeciazonej metody
     */
    public static Map<String, List<String>> generatePseudoDocs(String phrase, String[] candidates, Map<String, List<Link>> documents)
    {
        String[] phraseArr = new String[]{phrase};
        String[] allPhrases = (String[]) ArrayUtils.addAll(phraseArr, candidates);
        
        Map<String, List<String>> pseudoDocs = PseudoDocResolver.generatePseudoDocs(allPhrases, documents);
        return pseudoDocs;
    }
}
