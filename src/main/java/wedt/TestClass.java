/* 
 * Created by Kamil on 13 sty 2015 01:19:13
 */
package wedt;


public class TestClass
{
    private static final String[][] candidatesLong = new String[][]
            {
                new String[]{"microsoft excel", "microsoft spreadsheet", "ms excel", "ms spreadsheet"},
                new String[]{"yours sincerely", "best wishes", "yours faithfully", "best regards"},
                new String[]{"samsung galaxy young", "samsung GT-S6310", "galaxy young", "S6310 young"}
            };
    private static final String[] resultsLong = new String[]
            {
                "ms excel",
                "yours faithfully",
                "galaxy young"
            };
    
    private static final String[][] candidates = new String[][]
        {
            new String[]{"remote","automatic","distant","savage","mean"},
            new String[]{"detest","argue","hate","discover","reveal"},
            new String[]{"gracious","pretty","clever","pleasant","present"},
            new String[]{"predict","foretell","decide","prevent","discover"},
            new String[]{"kin","exult","twist","friend","relative"},
            new String[]{"pensive","oppressed","caged","thoughtful","happy"},
            new String[]{"banish","exile","hate","fade","clean"},
            new String[]{"fraud","malcontent","argument","imposter","clown"},
            new String[]{"saccharine","leave","sweet","arid","quit"},
            new String[]{"drag","sleepy","crush","proud","pull"}
        };
    private static final String[] results = new String[]
        {
            "distant",
            "hate",
            "pleasant",
            "foretell",
            "relative",
            "thoughtful",
            "exile",
            "argument",
            "sweet",
            "pull"
        };
    public static void main(String[] args)
    {
//      List<String> queries = new ArrayList<String>();
//      for(int i=0; i<candidates.length; i++)
//      {
//          for(int j=0; j<candidates[i].length; j++)
//          {
//              queries.add(candidates[i][j]);
//          }
//      }
      
//      for(int i=0; i<candidatesLong.length; i++)
//      {
//          for(int j=0; j<candidatesLong[i].length; j++)
//          {
//              queries.add(candidatesLong[i][j]);
//          }
//      }
      
      
//      WebSearch search = new GoogleSearch();
//      Map<String, List<Link>> queriesResponse = search.getQueriesResponse();
//      
//      String phrase = candidates[0][0];
//      String[] currCandidates = Arrays.copyOfRange(candidates[0], 1, candidates[0].length);
//      
//      Synonym s = RDESFramework.discoverAndGetSynonym(phrase, currCandidates, queriesResponse);
//      if(s != null)
//          System.out.println("Main candidate for " + s.getS1().toUpperCase() + " is " + s.getS2().toUpperCase());
      
//      WebSearch search = new GoogleSearch();
//      Map<String, List<Link>> queriesResponse = search.getLinks(queries);
//      search.saveToFile();
      
//      String[] cands = candidates[0];
//      for(int i=1; i<candidates.length; i++)
//          cands = (String[]) ArrayUtils.addAll(cands, candidates[i]);
//      for(int i=0; i<candidatesLong.length; i++)
//          cands = (String[]) ArrayUtils.addAll(cands, candidatesLong[i]);
//      
//      PseudoDoc pseudoDoc = new PseudoDoc(cands, queriesResponse);
      
//      for(int i=0; i<candidates.length; i++)
//      {
//          String phrase = candidates[i][0];
//          String[] currCandidates = Arrays.copyOfRange(candidates[i], 1, candidates[i].length);
//          
//          Synonym s = RDESFramework.discoverAndGetSynonym(phrase, currCandidates, queriesResponse);
//          if(s != null)
//              System.out.println(i+1 + ": Main candidate for " + s.getS1().toUpperCase() + " is " + s.getS2().toUpperCase() + "\t " + (results[i].equals(s.getS2()) ? "OK" : "FAIL"));
//      }
      
//      for(int i=0; i<candidatesLong.length; i++)
//      {
//          String phrase = candidatesLong[i][0];
//          String[] currCandidates = Arrays.copyOfRange(candidatesLong[i], 1, candidatesLong[i].length);
//          
//          Synonym s = RDESFramework.discoverAndGetSynonym(phrase, currCandidates, queriesResponse);
//          if(s != null)
//              System.out.println(i+1 + ": Main candidate for " + s.getS1().toUpperCase() + " is " + s.getS2().toUpperCase() + "\t " + (resultsLong[i].equals(s.getS2()) ? "OK" : "FAIL"));
//      }
    }

}
