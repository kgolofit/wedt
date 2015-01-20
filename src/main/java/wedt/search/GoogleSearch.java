/* 
 * Created by Kamil on 12 sty 2015 15:48:02
 */
package wedt.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wedt.search.GoogleResults.Result;

import com.google.gson.Gson;

public class GoogleSearch extends WebSearch
{
    private static final String GOOGLE_FILE = "./google_saved_results.dat";

    @Override
    /**
     * zwraca 8 pierwszych wynikow z googla (wiecej sie nie da - pierwsza strona)
     */
    protected List<Link> getLinksForQuery(String query)
    {
        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=8&start=0&q=";
        String charset = "UTF-8";
        List<Link> ret = new ArrayList<Link>();

        try
        {
            URL url = new URL(google + URLEncoder.encode(query, charset));
            Reader reader = new InputStreamReader(url.openStream(), charset);
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
            //Thread.sleep(200);
            
            if(results != null && results.getResponseData() != null && results.getResponseData().getResults() != null)
            {
                for(Result r : results.getResponseData().getResults())
                {
                    Link l = new Link();
                    l.url = r.getUrl();
                    l.desc = r.getTitle();
                    
                    ret.add(l);
                }
            }
            
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } 
        return ret;
    }

    @Override
    protected void loadFromFile()
    {
        File file = new File(GOOGLE_FILE);
        if(file.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                
                queriesResponse = (HashMap<String, List<Link>>)ois.readObject();
                
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
            queriesResponse = new HashMap<String, List<Link>>();
        }
    }

    @Override
    public void saveToFile()
    {
        try
        {
            File file = new File(GOOGLE_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(queriesResponse);
            
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

    @Override
    protected List<Link> getLinksForQuery(String query, int from, int to)
    {
        int currentCounter = from;
        
        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=8&start=";
        String googleEnd = "&q=";
        String charset = "UTF-8";
        List<Link> ret = new ArrayList<Link>();

        try
        {
            while(currentCounter < to)
            {
                URL url = new URL(google + currentCounter + googleEnd + URLEncoder.encode(query, charset));
                Reader reader = new InputStreamReader(url.openStream(), charset);
                GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
                Thread.sleep(200);
                
                if(results != null && results.getResponseData() != null && results.getResponseData().getResults() != null)
                {
                    for(Result r : results.getResponseData().getResults())
                    {
                        Link l = new Link();
                        l.url = r.getUrl();
                        l.desc = r.getTitle();
                        
                        ret.add(l);
                    }
                }
                
                currentCounter += 8; // zwracane jest 8 wynikow na strone, wiec o tyle inkrementowany jest licznik
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

}
