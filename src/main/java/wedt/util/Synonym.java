/* 
 * Created by Kamil on 13 sty 2015 00:22:20
 */
package wedt.util;

/**
 * Opakowanie dla pary (fraza, synonim/kandydat)
 * 
 * @author Kamil
 *
 */
public class Synonym
{
    private String s1;
    private String s2;

    private double e_to_c_Sim;
    private double c_to_e_Sim;
    private double contextSim;

    public Synonym(String s1, String s2)
    {
        super();
        this.s1 = s1;
        this.s2 = s2;
    }

    public Synonym(String s1, String s2, double e_to_c_Sim, double c_to_e_Sim)
    {
        super();
        this.s1 = s1;
        this.s2 = s2;
        this.e_to_c_Sim = e_to_c_Sim;
        this.c_to_e_Sim = c_to_e_Sim;
    }

    public Synonym(String s1, String s2, double e_to_c_Sim, double c_to_e_Sim,
            double contextSim)
    {
        super();
        this.s1 = s1;
        this.s2 = s2;
        this.e_to_c_Sim = e_to_c_Sim;
        this.c_to_e_Sim = c_to_e_Sim;
        this.contextSim = contextSim;
    }

    public void setContextSim(double contextSim)
    {
        this.contextSim = contextSim;
    }

    public String getS1()
    {
        return s1;
    }

    public String getS2()
    {
        return s2;
    }

    public double getE_to_c_Sim()
    {
        return e_to_c_Sim;
    }

    public double getC_to_e_Sim()
    {
        return c_to_e_Sim;
    }

    public double getContextSim()
    {
        return contextSim;
    }
}
