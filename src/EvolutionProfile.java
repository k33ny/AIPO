import tracks.DesignMachine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Martynas Petuska on 18/07/2017.
 */
public abstract class EvolutionProfile implements Fittable
{
    public DesignMachine designMachine;
    public final String GAME;
    public final String GAME_DIRECTORY;
    public final int SEED;

    public int trials = 1000;
    public int populationSize = 20;
    public double survivalRatio = 0.8;  //what percentage of the population survives
    public double crossoverRatio = 0.5; //how many genes to crossover
    public double mutationRate = 0.02;   //chance to mutate

    public int agentEvaluationTrials = 200;
    public int individualFittingTrials = 3;
    public boolean iterateThroughLevels = true;
    public boolean showVisuals = false;
    private List<AgentWrap> agents = new ArrayList<>(Arrays.asList(new AgentWrap("sampleMCTS", "tracks.singlePlayer.advanced.sampleMCTS.Agent")));
    private boolean defaultAgents = true;
    public int estimatedTotalRuns = 0;

    public EvolutionProfile(String game)
    {
        if(game.endsWith(".txt")) game = game.substring(0, game.length()-4);
        this.GAME = game;
        this.GAME_DIRECTORY = "games/" + game + "/";
        SEED = new Random().nextInt();

        designMachine = new DesignMachine(getGameFile());
    }

    public void addAgent(AgentWrap agent)
    {
        if(defaultAgents)
        {
            defaultAgents = false;
            agents.clear();
        }
        agents.add(agent);
    }
    public AgentWrap getAgent(int index)
    {
        return agents.get(index);
    }
    public List<AgentWrap> getAgents()
    {
        return this.agents;
    }
    public InputStream getLevelFile(int index)
    {
        return ResourceLoader.getResource(GAME_DIRECTORY + GAME + "_lvl" + index + ".txt");
    }
    public InputStream getGameFile()
    {
        return ResourceLoader.getResource(GAME_DIRECTORY + GAME + ".txt");
    }
}
