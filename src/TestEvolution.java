import MaastCTS.Agent;
import core.competition.CompetitionParameters;

/**
 * Created by mpetus on 18/07/2017.
 */
public class TestEvolution
{
    public static final String VERSION = "2.2.1";

    public static Integer trials = null;
    public static Integer populationSize = null;
    public static Integer individualFittingTrials = null;
    public static Integer agentEvaluationTrials = null;
    public static String game = "aliens";
    public static Boolean iterateThroughLevels = null;
    public static Boolean showVisuals = null;

    public static void main(String[] args)
    {
        System.out.println("Running AIPO v" + VERSION);
        parseArguments(args);
        EvolutionProfile profile = new EvolutionProfile(game)
        {
            @Override
            public String goal()
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Positive player skill reflection.\n" +
                          "    Evolution tries to minimise the error between the measured performance and target performance.\n" +
                          "    Target performance is estimated using predetermined path strength modifiers.\n" +
                          "    Agent Strength = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];\n" +
                          "    Individual Fitness function:\n" +
                          "        double topPerf = getAgent(0).strength;\n" +
                          "        double[] agentError = new double[results.length];\n" +
                          "        for (int i = 0; i < results.length; i++)\n" +
                          "        {\n" +
                          "           double[] result = results[i];\n" +
                          "           double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];\n" +
                          "            double targetPerformance = topPerf*getAgent(i).strength;\n" +
                          "            agentError[i] = Math.abs(targetPerformance - performance);\n" +
                          "        }\n" +
                          "        double fitness = 0;\n" +
                          "        for(double error : agentError) fitness -= error;\n" +
                          "        return fitness;");
                return sb.toString();
            }
            @Override
            public double fit(double[] result)
            {
                return result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];
            }
            @Override
            public double fit(double[][] results)
            {
                double topPerf = getAgent(0).strength;
                double[] agentError = new double[results.length];
                for (int i = 0; i < results.length; i++)
                {
                    double[] result = results[i];
                    double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];
                    double targetPerformance = topPerf*getAgent(i).strength;
                    agentError[i] = Math.abs(targetPerformance - performance);
                }
                double fitness = 0;
                for(double error : agentError) fitness -= error;
                return fitness;
            }
        };
        applyParameters(profile);
        addAgents(profile);

        Evolution evolution = new Evolution(profile);
        evolution.startEvolution();
    }

    private static void addAgents(EvolutionProfile profile)
    {
//region Available agents
    //region Sample agents
        AgentWrap sampleRandomController = new AgentWrap("sampleRandom", "tracks.singlePlayer.simple.sampleRandom.Agent", 0, 0, 15, 0);
        AgentWrap doNothingController = new AgentWrap("doNothing", "tracks.singlePlayer.simple.doNothing.Agent");
        AgentWrap sampleOneStepController = new AgentWrap("sampleonesteplookahead", "tracks.singlePlayer.simple.sampleonesteplookahead.Agent", 21, 4, 21, 0);
        AgentWrap sampleMCTSController = new AgentWrap("sampleMCTS", "tracks.singlePlayer.deprecated.sampleMCTS.Agent", 16, 25, 51, 0);
        AgentWrap sampleFlatMCTSController = new AgentWrap("greedyTreeSearch", "tracks.singlePlayer.simple.greedyTreeSearch.Agent");
        AgentWrap sampleOLMCTSController = new AgentWrap("sampleMCTS", "tracks.singlePlayer.advanced.sampleMCTS.Agent", 14, 28, 51, 0);
        AgentWrap sampleGAController = new AgentWrap("sampleGA", "tracks.singlePlayer.deprecated.sampleGA.Agent", 17, 21, 59, 0);
        AgentWrap sampleOLETSController = new AgentWrap("olets", "tracks.singlePlayer.advanced.olets.Agent");
        AgentWrap repeatOLETS = new AgentWrap("repeatOLETS", "tracks.singlePlayer.tools.repeatOLETS.Agent");
    //endregion
    //GECCO 2016 agents
        AgentWrap MaastCTS = new AgentWrap("MaastCTS", "MaastCTS.Agent", 1, 127, 109, 2);
    //endregion

        profile.addAgent(MaastCTS);
        profile.addAgent(sampleOLMCTSController);
        profile.addAgent(sampleGAController);
    }
    private static void applyParameters(EvolutionProfile profile)
    {
        if(trials != null) profile.trials = trials;
        if(populationSize != null) profile.populationSize = populationSize;
        if(individualFittingTrials != null) profile.individualFittingTrials = individualFittingTrials;
        if(agentEvaluationTrials != null) profile.agentEvaluationTrials = agentEvaluationTrials;
        if(showVisuals != null) profile.showVisuals = showVisuals;
    }
    private static void parseArguments(String[] args)
    {
        String defaultValueCmd = "d";
        //args{trials[0], populationSize[1] individualFittingTrials[2], agentEvaluationTrials[3], game[4], showVisuals[5]}
        if((args.length > 0) && (!args[0].equals(defaultValueCmd))) trials = Integer.parseInt(args[0]);
        if((args.length > 1) && (!args[1].equals(defaultValueCmd))) populationSize = Integer.parseInt(args[1]);
        if((args.length > 2) && (!args[2].equals(defaultValueCmd))) individualFittingTrials = Integer.parseInt(args[2]);
        if((args.length > 3) && (!args[3].equals(defaultValueCmd))) agentEvaluationTrials = Integer.parseInt(args[3]);
        if((args.length > 4) && (!args[4].equals(defaultValueCmd))) game = args[4];
        if((args.length > 5) && (!args[5].equals(defaultValueCmd))) iterateThroughLevels = Boolean.parseBoolean(args[5]);
        if((args.length > 6) && (!args[6].equals(defaultValueCmd))) showVisuals = Boolean.parseBoolean(args[6]);
    }
}
