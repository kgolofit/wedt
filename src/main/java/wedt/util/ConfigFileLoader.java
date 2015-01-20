/* 
 * Created by Kamil on 13 sty 2015 16:15:48
 */
package wedt.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loader konfiguracji z pliku.
 * @author Kamil
 *
 */
public class ConfigFileLoader
{
    private static final String FILE_CONFIG = "./config.conf";
    private static final double TRESHOLD_DEFAULT = 1.0d;
    
    /**
     * Wczytuje plik linia po linii, parsuje go
     * 
     * @return konfiguracja
     */
    public static ConfigFile loadConfigFile()
    {
        ConfigFile configFile = new ConfigFile();
        
        try
        {
            double treshold = TRESHOLD_DEFAULT;
            
            FileReader fileReader = new FileReader(FILE_CONFIG);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            List<String[]> lines = new ArrayList<String[]>();
            String line = null;
            
            while((line = bufferedReader.readLine())!= null)
            {
                if(line.contains("//") || line.length() <= 2) // komentarz lub pusta linia
                    continue;
                else
                    break;
            }
            
            // pierwsza linia - parametr treshold
            if(line != null)
                treshold = Double.parseDouble(line);
                
            // reszte linii to zapisne przyklady do sprawdzenia
            while((line = bufferedReader.readLine())!= null)
            {
                if(line.contains("//") || line.length() <= 2) // komentarz lub pusta linia
                    continue;
                
                String [] list = line.split(";");
                lines.add(list);
            }
            
            // koniec pliku - zamykamy strumien
            bufferedReader.close();
            
            // w lines mamy kolejne linie zawierajace stringi w formie listy tablicy stringow
            String[] phrases = new String[lines.size()];
            String[][] candidates = new String[lines.size()][];
            String[] results = new String[lines.size()];
            
            for(int i=0; i<lines.size(); i++)
            {
                String[] singleLine = lines.get(i);
                
                phrases[i] = singleLine[0];
                candidates[i] = Arrays.copyOfRange(singleLine, 1, singleLine.length-1);
                results[i] = singleLine[singleLine.length-1];
            }

            configFile.setSimTreshold(treshold);
            configFile.setPhrases(phrases);
            configFile.setCandidates(candidates);
            configFile.setResults(results);
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            configFile = null;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return configFile;
    }
}
