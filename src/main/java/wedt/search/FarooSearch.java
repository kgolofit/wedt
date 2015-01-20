/* 
 * Created by Kamil on 12 sty 2015 15:47:46
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

import com.google.gson.Gson;

public class FarooSearch extends WebSearch
{
    private static final String FAROO_FILE = "./faroo_saved_results.dat";
    private static final String FAROO_KEY = "&key=uQvB2mREvyV5sN0Op@zkH54wR-4_";
    
    
    @Override
    protected List<Link> getLinksForQuery(String query)
    {
        return getLinksForQuery(query, 1);
    }
    
    protected List<Link> getLinksForQuery(String query, int start)
    {
        String google = "http://www.faroo.com/api?start="+start+"&length=10&l=en&src=web&i=false&f=json";
        String q = "&q=";
        String charset = "UTF-8";
        List<Link> ret = new ArrayList<Link>();

        try
        {
            URL url;
            url = new URL(google + FAROO_KEY + q + URLEncoder.encode(query, charset));
            Reader reader = new InputStreamReader(url.openStream(), charset);
            ResultJ results = new Gson().fromJson(reader, ResultJ.class);
            Thread.sleep(3000);
            for (EntinyJ item : results.results)
            {
                Link l = new Link();
                l.url = item.domain;
                l.desc = item.kwic;
                ret.add(l);
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

    @Override
    protected void loadFromFile()
    {
        File file = new File(FAROO_FILE);
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
            File file = new File(FAROO_FILE);
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
        List<Link> ret = new ArrayList<Link>();
        
        while(currentCounter < to)
        {
            List<Link> add = getLinksForQuery(query, (currentCounter%10)+1);
            
            ret.addAll(add);
            currentCounter += 10;
        }
        
        return ret;
    }

}
