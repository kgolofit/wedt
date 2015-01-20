/* 
 * Created by Kamil on 12 sty 2015 15:43:06
 */
package wedt.search;

import java.io.Serializable;
import java.util.List;

/**
 * Opakowanie dla linku zwracanego przez wyszukiwarke internetowa
 * @author Kamil
 *
 */
public class Link implements Serializable
{
    private static final long serialVersionUID = 104537549700788988L;
    
    public String url;
    public String desc;
    
    @Override
    public String toString()
    {
        return "[URL: "+url+"; DESC: " + desc + "]";
    }
}

class EntinyJ
{
    public String domain;
    public String kwic;
}

class ResultJ
{
    public List<EntinyJ> results;
}

class GoogleResults{

    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }

    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }

    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +", title:" + title + "]"; }
    }
}
