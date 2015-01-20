/* 
 * Created by Kamil on 12 sty 2015 23:53:13
 */
package wedt.similarityComputation;

import java.util.List;
import java.util.Map;

import wedt.search.Link;

public class DocumentSimilarityComputation
{
    /**
     * UWAGA! Dziala jednostronnie -> computeDocSim(s1, s2) != computeDocSim(s2, s1) !!!
     * Wylicza podobienstwo dwoch fraz w sensie podobienstwa pseudo-dokumentowego
     * 
     * @param s1 - fraza pierwsza
     * @param s1Documents - zbior dokumentow dla frazy pierwszej
     * @param s2 - fraza druga
     * @param s2Documents - zbior dokumentow dla frazy drugiej
     * @return podobienstwo z zakresu [0;1]
     */
    public static double computeDocSim(String s1, String s2, List<Link> s2Documents, Map<String, List<String>> pseudoDocs)
    {
        /*
         * Ze wzoru [2] i [3] z artykulu 'A Framework for Robust Discovery of Entity Synonyms'
         * sim = (liczba takich dokumentow dla s2, w ktorych pseudodokumentach zawieraja sie tokeny frazy s1) / (liczba dokumentow dla s2)
         */
        
        String[] s1Tokens = s1.split("\\s+");
        
        int pseuDocS1inDocS2 = 0;
        int docS2 = s2Documents.size();
        
        for(Link s2Doc : s2Documents)
        {
            List<String> suppTokens = pseudoDocs.get(s2Doc.url);
            boolean ok = true;
            
            for(String s1Token : s1Tokens)
            {
                if(!suppTokens.contains(s1Token))
                {
                    ok = false;
                }
            }
            
            if(ok)
            {
                pseuDocS1inDocS2++;
            }
        }
        
        return (pseuDocS1inDocS2 / (double)docS2);
    }
}
