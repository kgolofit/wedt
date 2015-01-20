/* 
 * Created by Kamil on 13 sty 2015 16:13:52
 */
package wedt.util;

import org.apache.commons.lang.ArrayUtils;

/**
 * Opakowanie do konfiguracji wczytywanej przy uruchomieniu programu z pliku 
 * @author Kamil
 *
 */
public class ConfigFile
{
    private double simTreshold;
    
    private String[] phrases;
    private String[][] candidates;
    private String[] results;
    
    public ConfigFile()
    {
        
    }
    
    public ConfigFile(double simTreshold, String[] phrases,
            String[][] candidates, String[] results)
    {
        super();
        this.simTreshold = simTreshold;
        this.phrases = phrases;
        this.candidates = candidates;
        this.results = results;
    }
    
    public String[] getAllPhrases()
    {
        String[] allPhrases = phrases;
        
        for(String[] c : candidates)
        {
            allPhrases = (String[]) ArrayUtils.addAll(allPhrases, c);
        }
     
        return allPhrases;
    }

    public double getSimTreshold()
    {
        return simTreshold;
    }

    public void setSimTreshold(double simTreshold)
    {
        this.simTreshold = simTreshold;
    }

    public String[] getPhrases()
    {
        return phrases;
    }

    public void setPhrases(String[] phrases)
    {
        this.phrases = phrases;
    }

    public String[][] getCandidates()
    {
        return candidates;
    }

    public void setCandidates(String[][] candidates)
    {
        this.candidates = candidates;
    }

    public String[] getResults()
    {
        return results;
    }

    public void setResults(String[] results)
    {
        this.results = results;
    }
}
