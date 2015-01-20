/* 
 * Created by Kamil on 13 sty 2015 01:16:43
 */
package wedt.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OQueryRequest;

public class BingSearch extends WebSearch
{
    private static final String BING_FILE = "./bing_saved_results.dat";
    private static final String BING_KEY = "/yp0zhT6LfaSa+WMUdKkBy6M9nVNXc3sSoDN04pihiA";

    @Override
    protected void loadFromFile()
    {
        File file = new File(BING_FILE);
        if (file.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                queriesResponse = (HashMap<String, List<Link>>) ois
                        .readObject();

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
        } else
        {
            queriesResponse = new HashMap<String, List<Link>>();
        }
    }

    @Override
    public void saveToFile()
    {
        try
        {
            File file = new File(BING_FILE);
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
    protected List<Link> getLinksForQuery(String query)
    {
        ODataConsumer c = ODataConsumers.newBuilder("https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/")
                .setClientBehaviors(OClientBehaviors.basicAuth("accountKey",BING_KEY)).build();

        OQueryRequest<OEntity> oRequest = c.getEntities("Web").custom("Query", "%27" + query + "%27");

        Enumerable<OEntity> entities = oRequest.execute();
        
        int i=0;
        i+=2;
        System.out.println(i);

        return null;
    }

    @Override
    protected List<Link> getLinksForQuery(String query, int from, int to)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
