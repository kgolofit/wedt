/* 
 * Created by Kamil on 13 sty 2015 00:06:47
 */
package wedt.similarityComputation;

import java.util.List;

import wedt.search.Link;

public class ContextSimilarityComputation
{
    /**
     * Wylicza podobienstwo dwoch fraz w sensie podobienstwa kontekstowego
     * 
     * @param s1 - fraza pierwsza
     * @param s1Documents - zbior dokumentow dla frazy pierwszej
     * @param s2 - fraza druga
     * @param s2Documents - zbior dokumentow dla frazy drugiej
     * @return podobienstwo z zakresu [0;0.5]
     */
    public static double computeContextSim(String s1, List<Link> s1Documents, String s2, List<Link> s2Documents)
    {
        /*
         * Ze wzoru [4] z artykulu 'A Framework for Robust Discovery of Entity Synonyms'
         * sim = (liczba pokrywajacych sie dokumentow z s1 i s2) / (suma dokumentow dla s1 i s2)
         */
        
        int intersection = 0;
        int union = s1Documents.size() + s2Documents.size();
        
        for(Link l : s1Documents)
        {
            for(Link l2 : s2Documents)
            {
                if(l.url.equals(l2.url))
                    intersection++;
            }
        }
        
        return (intersection / (double)union);
    }
}
