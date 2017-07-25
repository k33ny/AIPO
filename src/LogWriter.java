import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mpetus on 18/07/2017.
 */
public class LogWriter
{
    private static boolean printToSystem = true;
    private final StringBuilder logBuilder;
    public final String fileName;
    public final String directory = "evolutionLogs/";

    public LogWriter(String fileName)
    {
        logBuilder = new StringBuilder();
        this.fileName = fileName;
    }

    public static void setPrintToSystem(boolean flag)
    {
        printToSystem = flag;
    }

    public void log(String string)
    {
        logBuilder.append(string);
        if(printToSystem) System.out.print(string);
    }
    public void logln(String string)
    {
        log(string + "\n");
    }
    public void writeLogFile()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        logBuilder.append("Completion Date: " + dateFormat.format(date) + "\n");
        try
        {
            new File(directory).mkdirs();
            FileWriter fw = new FileWriter(new File(directory + fileName));
            fw.write(logBuilder.toString());
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
