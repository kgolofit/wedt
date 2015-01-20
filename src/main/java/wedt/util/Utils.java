/* 
 * Created by Kamil on 13 sty 2015 18:08:23
 */
package wedt.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Klasa udostepniajaca przydatne statyczne metody
 * 
 * @author Kamil
 *
 */
public class Utils
{
    private static void checkIfDirectoryExists(String dir)
    {
        File outDir = new File("./" + dir + "/");
        if (!outDir.exists())
        {
            System.out.println("Tworze katalog:" + "./" + dir + "/");
            outDir.mkdir();
        }
    }
    
    public static String getFileNameToDumpOut(String prefix, String extension)
    {
        // najpierw sprawdzmy czy w biezacym katalogu jest katalog out/
        // jesli nie to go tworzymy
        checkIfDirectoryExists("out");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd---HH-mm-ss");
        Calendar cal = Calendar.getInstance();

        return "./out/" + prefix + "-" + dateFormat.format(cal.getTime()) + "."
                + extension;
    }

    public static String getFileNameToDumpOut(String extension)
    {
        return Utils.getFileNameToDumpOut("out", extension);
    }
}
