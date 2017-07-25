/**
 * Created by mpetus on 24/07/2017.
 */
public class AgentWrap
{
    public static final int MAX_WINS = 250;

    public final String name;
    public final String path;
    public final int rankGECCO2016;
    public final int points;
    public final int wins;
    public final int disqualifications;

    public double strength = 0;

    public AgentWrap(String name, String path)
    {
        this(name, path, 0, 0, 0, 0);
    }
    public AgentWrap(String name, String path, int rankGECCO2016, int points, int wins, int disqualifications)
    {
        this.name = name;
        this.path = path;
        this.rankGECCO2016 = rankGECCO2016;
        this.points = points;
        this.wins = wins;
        this.disqualifications = disqualifications;
    }

    public String toString()
    {
        return ("\"" + name + "\"\tStrength: " + strength);
    }
}
