/**
 * Created by mpetus on 24/07/2017.
 */
public class AgentWrap
{
    public static final int MAX_WINS = 250;

    public final String path;
    public final String name;
    public final int points;
    public final int wins;
    public final int disqualifications;

    public double strength = 0;

    public AgentWrap(String name, String path)
    {
        this(name, path, 0, 0, 0);
    }
    public AgentWrap(String name, String path, int points, int wins, int disqualifications)
    {
        this.path = path;
        this.name = name;
        this.points = points;
        this.wins = wins;
        this.disqualifications = disqualifications;
    }

    public String toString()
    {
        return ("\"" + name + "\"\tStrength: " + strength);
    }
}
