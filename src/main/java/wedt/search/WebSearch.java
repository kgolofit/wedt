/* 
 * Created by Kamil on 12 sty 2015 15:41:47
 */
package wedt.search;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wedt.util.Synonym;
import wedt.util.Utils;

public abstract class WebSearch
{
    private static final int MIN_DOCUMENTS = 16; // 40
    protected Map<String, List<Link>> queriesResponse;
    
    public WebSearch()
    {
        loadFromFile();
    }
    

    /**
     * opakowanie przeciazonej metody
     */
    public Map<String, List<Link>> getLinks(String[] queries)
    {
        List<String> str = new ArrayList<String>(Arrays.asList(queries));
        return getLinks(str);
    }
    
    /**
     * Zbiera dokumenty (urle) do zapytan z wykorzystaniem wyszukiwarki internetowej
     * 
     * @param queries - zapytania
     * @return mapa <zapytanie, lista urli(dokumentow)>
     */
    public Map<String, List<Link>> getLinks(List<String> query)
    {
        System.out.println("========================= WEBSEARCH =========================");
        Map<String, List<Link>> map = new HashMap<String, List<Link>>();
        int i=1;
        
        for(String q : query)
        {
            boolean memory = true;
            boolean additional = false;
            List<Link> links = queriesResponse.get(q);
            
            // jesli wyniki tego zapytania nie sa w cache, to pytamy google o rezultaty, 
            // w przeciwnym wypadku jego wyniki sa w cache - tylko je zwracamy (bez szukania w google)
            if(links == null || links.size() == 0)
            {
                // tego jeszcze nie bylo w cache - lecimy z zapytaniem do google
                memory = false;
                links = getLinksForQuery(q);
                
                // dodajemy do zapamietanych
                queriesResponse.put(q, links);
            }
            
            // sprawdzamy tez liczbe urli(dokumentow) dla zapytania - jesli jest za mala to doszukujemy w google
            if(links.size() < MIN_DOCUMENTS)
            {
                additional = true;
                List<Link> additionalLinks = getLinksForQuery(q, links.size(), MIN_DOCUMENTS);
                
                // dodajemy do cache i do wynikow
                List<Link> newLinks = links;
                newLinks.addAll(additionalLinks);
                
                queriesResponse.put(q, newLinks);
            }
            
            map.put(q, links);
            
            System.out.println("Query " + i + " of " + query.size() + ": "+q.toUpperCase()+"; Response from: " +  (memory ? "MEMORY" : "GOOGLE") + (additional ? " + ADDITIONAL DOWNLOAD" : ""));
            i++;
        }
        
        return map;
    }
    
    /**
     * ladowanie cache z pliku (deserializacja)
     */
    protected abstract void loadFromFile();
    
    /**
     * zapis cache do pliku (serializacja)
     */
    public abstract void saveToFile();
    
    /**
     * Metoda pobierajaca wyniki zapytania z wyszukiwarki internetowej (juz bez uwzgledniania cache)
     * @param query - zapytanie
     * @return lista urli(dokumentow)
     */
    protected abstract List<Link> getLinksForQuery(String query);
    
    /**
     * Metoda uzywana do 'dobierania' wynikow z wyszukiwarki, jesli w cache jest ich za malo 
     */
    protected abstract List<Link> getLinksForQuery(String query, int from, int to);

    /**
     * getter
     */
    public Map<String, List<Link>> getQueriesResponse()
    {
        return queriesResponse;
    }
    
    public void printAllDocumentsToFile()
    {
        String outFileName = Utils.getFileNameToDumpOut("documents", "txt");
        PrintWriter pw = null;
        
        try
        {
            pw = new PrintWriter(outFileName);
            pw.println("=====                         Zbior dokumentow                         =====");
            pw.println("============================================================================");
            
            for (Map.Entry<String, List<Link>> entry : queriesResponse.entrySet())
            {
                pw.println(entry.getKey() + " : " + entry.getValue().size() + " el.");
                for(Link l : entry.getValue())
                {
                    pw.println("\t" + l.url);
                }
                pw.println("----------------------------------------------------------------------------");
            }
            
            pw.println("============================================================================");
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
