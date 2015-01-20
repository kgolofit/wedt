/* 
 * Created by Kamil on 13 sty 2015 14:29:53
 */
package wedt.similarityComputation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wedt.exception.PseudoDocNotInitializedException;
import wedt.search.Link;
import wedt.util.Utils;

/**
 * Opakowanie pseudo-dokumentow, pozwalajaca na serializacje i deserializacje
 * 
 * @author Kamil
 *
 */
public class PseudoDoc
{
    private static final String PSEUDO_DOC_FILE = "./pseudodocuments.dat";
    private static Map<String, List<String>> pseudoDocs;
    
    public PseudoDoc(String[] candidates, Map<String, List<Link>> documents, boolean rebuild)
    {
        this(candidates, documents);
        if(rebuild)
        {
            // przebudowujemy baze pseudodokumentow
            System.out.println("Rebuilding Pseudo Documents...");
            pseudoDocs = PseudoDocResolver.generatePseudoDocs(candidates, documents);
            System.out.println("Rebuild Pseudo Documents done. Serializing...");
            save();
            System.out.println("Pseudo Documents serialized.");
        }
    }
    
    public PseudoDoc(String[] candidates, Map<String, List<Link>> documents)
    {
        load();
        boolean needRebuild = allNeededPseudoDocCheck(candidates, documents);
        if(needRebuild)
        {
            // przebudowujemy baze pseudodokumentow
            System.out.println("Rebuilding Pseudo Documents...");
            pseudoDocs = PseudoDocResolver.generatePseudoDocs(candidates, documents);
            System.out.println("Rebuild Pseudo Documents done. Serializing...");
            save();
            System.out.println("Pseudo Documents serialized.");
        }
    }
    
    /**
     * Sprawdza czy przebudowa zbioru pseudodokumentow jest potrzebna (czy zbior zawiera pseudodokumenty dla wszystkich dokumentow dla kandydatow)
     * 
     * @param candidates - kandydaci, bedacy porownywani w ramach algorytmu
     * @param documents - zebrane dokumenty z wyszukiwarki internetowej
     * 
     * @return true jesli nie trzeba przebudowywac
     */
    private boolean allNeededPseudoDocCheck(String[] candidates, Map<String, List<Link>> documents)
    {
        // lista wszystkich dokumentow ktorymi sie aktualnie zajmujemy
        List<Link> currDocuments = new ArrayList<Link>();
        for(String cPhrase : candidates)
        {
            currDocuments.addAll(documents.get(cPhrase));
        }
        
        // teraz dla wszystkich tych linkow powinnismy miec pseudodokument z co najmniej jednym tokenem
        for(Link doc : currDocuments)
        {
            List<String> tokens = pseudoDocs.get(doc.url);
            
            if(tokens == null || tokens.size()==0)
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     * getter
     *  
     * @throws PseudoDocNotInitializedException - kiedy brak inicjalizacji (nie bylo deserializacji)
     */
    public static Map<String, List<String>> getPseudoDocs() throws PseudoDocNotInitializedException
    {
        if(pseudoDocs == null)
            throw new PseudoDocNotInitializedException("PseudoDoc nie zainicjalizowany!\nPrzed uzyciem nalezy utworzyc instancje klasy PseudoDoc!");
        return pseudoDocs;
    }
    
    /**
     * ladowanie zbioru pseudodokumentow z pliku (deserializacja)
     */
    private void load()
    {
        File file = new File(PSEUDO_DOC_FILE);
        if(file.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                
                pseudoDocs = (HashMap<String, List<String>>)ois.readObject();
                
                ois.close();
                fis.close();
                
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            pseudoDocs = new HashMap<String, List<String>>();
        }
    }
    
    /**
     * zapis zbioru pseudodokumentow do pliku (serializacja)
     */
    private void save()
    {
        try
        {
            File file = new File(PSEUDO_DOC_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pseudoDocs);
            
            oos.close();
            fos.close();
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void printAllPseudoDocumentsToFile()
    {
        String outFileName = Utils.getFileNameToDumpOut("pseudo_documents", "txt");
        PrintWriter pw = null;
        
        try
        {
            pw = new PrintWriter(outFileName);
            pw.println("=====                  Zbior pseudo-dokumentow                         =====");
            pw.println("============================================================================");
            pw.println("===== w postaci: pierwsza linia - doument : liczba elementow,          =====");
            pw.println("===== reszta linii z tabulacja - pseudodokumenty danego dokumentu.     =====");
            pw.println("============================================================================");
            
            for (Map.Entry<String, List<String>> entry : pseudoDocs.entrySet())
            {
                pw.println(entry.getKey() + " : " + entry.getValue().size() + " el.");
                for(String s : entry.getValue())
                {
                    pw.println("\t" + s);
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
