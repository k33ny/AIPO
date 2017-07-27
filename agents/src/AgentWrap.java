/**
 * Created by mpetus on 24/07/2017.
 */
public class AgentWrap
{
    public static final int MAX_WINS = 250;

    public final String name;
    public final String path;
    public final Integer rank_CIG2016;
    public final Integer points;
    public final Integer wins;
    public final Integer disqualifications;

    public double strength = 0;

    public AgentWrap(String name, String path)
    {
        this(name, path, null, null, null, null);
    }
    public AgentWrap(String name, String path, Integer rank_CIG2016, Integer points, Integer wins, Integer disqualifications)
    {
        this.name = name;
        this.path = path;
        this.rank_CIG2016 = rank_CIG2016;
        this.points = points;
        this.wins = wins;
        this.disqualifications = disqualifications;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Agent name: " + name);
        if(strength > 0) sb.append("; Strength: " + strength);
        if(rank_CIG2016 != null) sb.append("; CIG2016 Rank: " + rank_CIG2016);
        if(points != null) sb.append("; Points: " + points);
        if(wins != null) sb.append("; Wins: " + wins + "/" + MAX_WINS);
        if(disqualifications != null) sb.append("; Disqualifications: " + disqualifications);
        return sb.toString();
    }
}
