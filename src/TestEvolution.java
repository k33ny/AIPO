import core.competition.CompetitionParameters;
import tracks.DesignMachine;

import java.util.Random;

/**
 * Created by Martynas Petuska on 18/07/2017.
 */
public class TestEvolution
{
    public static final String VERSION = "2.3.1";

    public static Integer trials = null;
    public static Integer populationSize = null;
    public static Integer individualFittingTrials = null;
    public static Integer agentEvaluationTrials = null;
    public static String game = "aliens";
    public static Boolean iterateThroughLevels = null;
    public static Boolean showVisuals = null;

    public static Boolean positiveEvolution = true;

    public static void main(String[] args)
    {
        System.out.println("Running AIPO v" + VERSION);
        parseArguments(args);
        EvolutionProfile profilePositive = new EvolutionProfile(game)
        {
            @Override
            public String goal()
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Positive skill reflection.\nFitness functions:\n" +
                        "            @Override\n" +
                        "            public double fit(double[] result) //result[outcome]\n" +
                        "            {\n" +
                        "                double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];\n" +
                        "                return performance;\n" +
                        "            }\n\n" +
                        "            @Override\n" +
                        "            public double fit(double[][] results) //results[simulation][outcome]\n" +
                        "            {\n" +
                        "                double averagePerformance = 0;\n" +
                        "                for (int sim = 0; sim < results.length; sim++)\n" +
                        "                    averagePerformance += fit(results[sim]);\n" +
                        "                averagePerformance = averagePerformance/results.length;\n" +
                        "                return averagePerformance;\n" +
                        "            }\n\n" +
                        "            @Override\n" +
                        "            public double fit(double[][][] results) //results[agent][simulation][outcome]\n" +
                        "            {\n" +
                        "                double averageAgentPerformances[] = new double[results.length];\n" +
                        "                for (int agent = 0; agent < results.length; agent++)\n" +
                        "                    averageAgentPerformances[agent] = fit(results[agent]);\n" +
                        "\n" +
                        "                double[] agentError = new double[results.length];\n" +
                        "                double fitness = 0;\n" +
                        "                for (int agent = 0; agent < results.length; agent++)\n" +
                        "                {\n" +
                        "                    double performance = averageAgentPerformances[agent];\n" +
                        "                    double targetPerformance = averageAgentPerformances[0] * getAgent(agent).strength;\n" +
                        "                    agentError[agent] = Math.abs(targetPerformance - performance);\n" +
                        "                    fitness -= agentError[agent];\n" +
                        "                }\n" +
                        "                return fitness;\n" +
                        "            }");
                return sb.toString();
            }
            @Override
            public double fit(double[] result) //result[outcome]
            {
                double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];
                return performance;
            }
            @Override
            public double fit(double[][] results) //results[simulation][outcome]
            {
                double averagePerformance = 0;
                for (int sim = 0; sim < results.length; sim++)
                    averagePerformance += fit(results[sim]);
                averagePerformance = averagePerformance/results.length;
                return averagePerformance;
            }
            @Override
            public double fit(double[][][] results) //results[agent][simulation][outcome]
            {
                double averageAgentPerformances[] = new double[results.length];
                for (int agent = 0; agent < results.length; agent++)
                    averageAgentPerformances[agent] = fit(results[agent]);

                double[] agentError = new double[results.length];
                double fitness = 0;
                for (int agent = 0; agent < results.length; agent++)
                {
                    double performance = averageAgentPerformances[agent];
                    double targetPerformance = averageAgentPerformances[0] * getAgent(agent).strength;
                    agentError[agent] = Math.abs(targetPerformance - performance);
                    fitness -= agentError[agent];
                }
                return fitness;
            }
        };
        EvolutionProfile profileNegative = new EvolutionProfile(game)
        {
            @Override
            public String goal()
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Negative skill reflection.\nFitness functions:\n" +
                        "            @Override\n" +
                        "            public double fit(double[] result) //result[outcome]\n" +
                        "            {\n" +
                        "                double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];\n" +
                        "                return performance;\n" +
                        "            }\n\n" +
                        "            @Override\n" +
                        "            public double fit(double[][] results) //results[simulation][outcome]\n" +
                        "            {\n" +
                        "                double averagePerformance = 0;\n" +
                        "                for (int sim = 0; sim < results.length; sim++)\n" +
                        "                    averagePerformance += fit(results[sim]);\n" +
                        "                averagePerformance = averagePerformance/results.length;\n" +
                        "                return averagePerformance;\n" +
                        "            }\n\n" +
                        "            @Override\n" +
                        "            public double fit(double[][][] results) //results[agent][simulation][outcome]\n" +
                        "            {\n" +
                        "                double averageAgentPerformances[] = new double[results.length];\n" +
                        "                for (int agent = 0; agent < results.length; agent++)\n" +
                        "                    averageAgentPerformances[agent] = fit(results[agent]);\n" +
                        "\n" +
                        "                double[] agentError = new double[results.length];\n" +
                        "                double fitness = 0;\n" +
                        "                for (int agent = 0; agent < results.length; agent++)\n" +
                        "                {\n" +
                        "                    double performance = averageAgentPerformances[agent];\n" +
                        "                    double targetPerformance = averageAgentPerformances[0] * (1 - getAgent(agent).strength);\n" +
                        "                    agentError[agent] = Math.abs(targetPerformance - performance);\n" +
                        "                    fitness -= agentError[agent];\n" +
                        "                }\n" +
                        "                return fitness;\n" +
                        "            }");
                return sb.toString();
            }
            @Override
            public double fit(double[] result) //result[outcome]
            {
                double performance = result[1] + (CompetitionParameters.MAX_TIMESTEPS - result[2])*result[0];
                return performance;
            }
            @Override
            public double fit(double[][] results) //results[simulation][outcome]
            {
                double averagePerformance = 0;
                for (int sim = 0; sim < results.length; sim++)
                    averagePerformance += fit(results[sim]);
                averagePerformance = averagePerformance/results.length;
                return averagePerformance;
            }
            @Override
            public double fit(double[][][] results) //results[agent][simulation][outcome]
            {
                double averageAgentPerformances[] = new double[results.length];
                for (int agent = 0; agent < results.length; agent++)
                    averageAgentPerformances[agent] = fit(results[agent]);

                double[] agentError = new double[results.length];
                double fitness = 0;
                for (int agent = 0; agent < results.length; agent++)
                {
                    double performance = averageAgentPerformances[agent];
                    double targetPerformance = averageAgentPerformances[0] * (1 - getAgent(agent).strength);
                    agentError[agent] = Math.abs(targetPerformance - performance);
                    fitness -= agentError[agent];
                }
                return fitness;
            }
        };
        EvolutionProfile profile;
        if(positiveEvolution) profile = profilePositive;
        else profile = profileNegative;
        applyParameters(profile);
        addAgents(profile);

        Evolution evolution = new Evolution(profile);
        evolution.startEvolution();
    }

    private static void addAgents(EvolutionProfile profile)
    {
        if(game.equals("aliens")) profile.addAgent(AgentManager.adrienctx);
        if(game.equals("seaquest")) profile.addAgent(AgentManager.Number27);
        profile.addAgent(AgentManager.muzzle);
        profile.addAgent(AgentManager.thorbjrn);
        System.out.println("Selected Agents:");
        for(AgentWrap agent : profile.getAgents()) System.out.println(agent);
        System.out.println();
    }
    private static void applyParameters(EvolutionProfile profile)
    {
        if(trials != null) profile.trials = trials;
        if(populationSize != null) profile.populationSize = populationSize;
        if(individualFittingTrials != null) profile.individualFittingTrials = individualFittingTrials;
        if(agentEvaluationTrials != null) profile.agentEvaluationTrials = agentEvaluationTrials;
        if(iterateThroughLevels != null) profile.iterateThroughLevels = iterateThroughLevels;
        if(showVisuals != null) profile.showVisuals = showVisuals;
    }
    private static void parseArguments(String[] args)
    {
        String defaultValueCmd = "d";
        char cmdSymbol = '-';
        //args{game[0], trials[1], populationSize[2] individualFittingTrials[3], agentEvaluationTrials[4], iterateThroughLevels[5], showVisuals[6]}
        if((args.length > 0) && (!args[0].toLowerCase().equals(defaultValueCmd)) && (args[0].charAt(0) != cmdSymbol)) game = args[0];
        if((args.length > 1) && (!args[1].toLowerCase().equals(defaultValueCmd)) && (args[1].charAt(0) != cmdSymbol)) trials = Integer.parseInt(args[1]);
        if((args.length > 2) && (!args[2].toLowerCase().equals(defaultValueCmd)) && (args[2].charAt(0) != cmdSymbol)) populationSize = Integer.parseInt(args[2]);
        if((args.length > 3) && (!args[3].toLowerCase().equals(defaultValueCmd)) && (args[3].charAt(0) != cmdSymbol)) individualFittingTrials = Integer.parseInt(args[3]);
        if((args.length > 4) && (!args[4].toLowerCase().equals(defaultValueCmd)) && (args[4].charAt(0) != cmdSymbol)) agentEvaluationTrials = Integer.parseInt(args[4]);
        if((args.length > 5) && (!args[5].toLowerCase().equals(defaultValueCmd)) && (args[5].charAt(0) != cmdSymbol)) iterateThroughLevels = Boolean.parseBoolean(args[5]);
        if((args.length > 6) && (!args[6].toLowerCase().equals(defaultValueCmd)) && (args[6].charAt(0) != cmdSymbol)) showVisuals = Boolean.parseBoolean(args[6]);
        for(String arg : args) if(arg.charAt(0) == cmdSymbol)
        {
            if(arg.equals(cmdSymbol + "positive")) positiveEvolution = true;
            if(arg.equals(cmdSymbol + "negative")) positiveEvolution = false;
        }
    }
}
