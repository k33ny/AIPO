import java.io.InputStream;
import java.net.URL;

/**
 * Created by mpetus on 24/07/2017.
 */
public class ResourceLoader
{
    private static final ResourceLoader RL = new ResourceLoader();

    public static InputStream getResource(String resource)
    {
        return RL.getClass().getClassLoader().getResourceAsStream(resource);
    }
    public static boolean validateResource(String resource)
    {
        URL u = RL.getClass().getClassLoader().getResource(resource);
        if(u == null) return false;
        else return true;
    }
}
