import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by mpetus on 18/07/2017.
 */
public class Evolution
{
    private final DecimalFormat dFormat = new DecimalFormat("#.00000");
    private final EvolutionProfile PROFILE;
    private final LogWriter log;
    private final long startMillis;

    private long avgMillisPerGame = 0;
    private int gameRunCount = 0;
    private long accumulatedMillis = 0;

    public Evolution(EvolutionProfile profile)
    {
        this.PROFILE = profile;
        log = new LogWriter(Integer.toString(PROFILE.SEED));
        startMillis = System.currentTimeMillis();
    }

//region Main Evolution
private void Log(String str) {log.log(str);}
    private void Logln(String str) {log.logln(str);}
    private void startLog()
    {
        System.out.println("Starting log file...");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        Logln("Start Date: " + dateFormat.format(date));
        Logln("AIPO version: " + TestEvolution.VERSION);
        Logln("\n********** Evolution Parameters **********");
        Logln("Trials: " + PROFILE.trials);
        Logln("Population Size: " + PROFILE.populationSize);
        Logln("Survival Ratio: " + PROFILE.survivalRatio);
        Logln("Crossover Ratio: " + PROFILE.crossoverRatio);
        Logln("Mutation Rate: " + PROFILE.mutationRate);
        Logln("Evolution Goal: " + PROFILE.goal());
        Logln("\n********** Simulation Parameters **********");
        Logln("Game: " + PROFILE.GAME);
        Logln("Game Seed: " + PROFILE.SEED);
        Logln("Agent Count: " + PROFILE.getAgents().size());
        Logln("Agent Evaluation Trials: " + PROFILE.agentEvaluationTrials);
        Logln("Individual Fitting Trials: " + PROFILE.individualFittingTrials);
        Logln("Iterating through levels: " + PROFILE.iterateThroughLevels);
        Logln("\n********** Game Run Counts **********");
        int aEvaluationRunCount = PROFILE.agentEvaluationTrials*PROFILE.getAgents().size();
        int evolutionRunCount = PROFILE.getAgents().size()*PROFILE.individualFittingTrials*PROFILE.populationSize*PROFILE.trials;
        int totalRunCount = aEvaluationRunCount + evolutionRunCount;
        PROFILE.estimatedTotalRuns = totalRunCount;
        Logln("Runs for Agent(s) Evaluation: " + aEvaluationRunCount);
        Logln("Runs for Evolution: " + evolutionRunCount);
        Logln("Total Runs: " + totalRunCount);
    }
    private void evaluateAgents()
    {
        System.out.println("Evaluating agents...");
        List<AgentWrap> agents = PROFILE.getAgents();
        int trials = PROFILE.agentEvaluationTrials;

        double topStr = 0;
        for (int a = 0; a < agents.size(); a++)
        {
            AgentWrap agent = agents.get(a);
            double performances[] = new double[trials];
            for (int t = 0; t < trials; t++)
            {
                Individual ind = new Individual();
                performances[t] = PROFILE.fit(runSimulation(ind.GENES, ind.validateLevel(t), agent));
                double proggress = 100*(a)/agents.size();
                double delta = 100*(a+1)/agents.size() - 100*(a)/agents.size();
            }

            for(double p : performances) agent.strength += p;
            agent.strength = agent.strength/performances.length;
            if(agent.strength > topStr) topStr = agent.strength;
        }

        for(AgentWrap agent : agents)
        {
            agent.strength = agent.strength/topStr;
            if(agent.strength < 0) agent.strength = 0;
            agent.strength = Double.valueOf(dFormat.format(agent.strength));
        }
        agents.sort((a, b) -> {if(a.strength > b.strength) return -1; else return 1;});

        StringBuilder report = new StringBuilder();
        report.append("\n********** Agent Evaluation Report **********\n");
        for(AgentWrap a : agents)
            report.append("Agent: \"" + a.path + "\";\tStrength: " + a.strength + ";\n");
        Log(report.toString());

        System.out.println("\nAgent evaluation complete.");
    }
    private Population evolve()
    {
        System.out.println("\nEvolving...");
        StringBuilder report = new StringBuilder();
        report.append("\n********** Evolution **********\n");
        int generation = 0;
        Population population = new Population();
        List<Individual> survivors = new ArrayList<>();
        Individual elite;

        do
        {
            //Fitness evaluation
            generation++;
            population.fit();
            population.sort();
            double avgFitness = 0;
            for (int i = 0; i < population.size(); i++)
                avgFitness += population.getIndividual(i).getFitness();
            avgFitness = Double.valueOf(dFormat.format(avgFitness/population.size()));
            report.append("Generation " + generation + ": " + population + "\n");
            report.append("Average fitness: " + avgFitness + "\n\n");
            elite = population.getIndividual(0).clone();
            survivors.add(elite);

            while(survivors.size() < PROFILE.survivalRatio*PROFILE.populationSize)
            {
                //Tournament selection
                List<Individual> contestants = new ArrayList<>();
                while (contestants.size() < PROFILE.populationSize * 0.25) while (true)
                {
                    boolean unique = true;
                    Individual entrant = population.getIndividual(new Random().nextInt(population.size()));
                    for (Individual i : contestants) if (i.equals(entrant)) unique = false;
                    if (unique)
                    {
                        contestants.add(entrant);
                        break;
                    }
                }

                //Tournament
                contestants.sort((a, b) ->
                {
                    if (a.getFitness() > b.getFitness()) return -1;
                    else return 1;
                });

                //Breeding
                Individual father = contestants.get(0);
                contestants = contestants.subList(1, contestants.size());
                Individual mother = contestants.get(new Random().nextInt(contestants.size()));
                survivors.add(mother.breed(father));
            }
            population = new Population(survivors);
            survivors.clear();
        } while(generation < PROFILE.trials);

        report.append("\n********** Evolution Outcome **********\n");
        report.append("Top Individual Fitness: " + elite.getFitness() + "\n");
        report.append("Individual's genes: " + elite);
        Logln(report.toString());
        return population;
    }
    public int[] startEvolution()
    {
        System.out.println("Beginning evolution...");
        startLog();
        evaluateAgents();
        evolve();

        Logln("Time Elapsed: " + getFullTimeString((System.currentTimeMillis() - startMillis)/1000));
        log.writeLogFile();
        System.out.println("Evolution complete.");
        return new int[PROFILE.designMachine.getNumDimensions()];
    }
//endregion
    private double[] runSimulation(int[] parameterSet, AgentWrap agent)
    {
        return runSimulation(parameterSet, 0, agent);
    }
    private double[] runSimulation(int[] parameterSet, int level, AgentWrap agent)
    {
        long startT = System.currentTimeMillis();
        double[] result = PROFILE.designMachine.runOneGame(parameterSet, PROFILE.getGameFile(), PROFILE.getLevelFile(level), PROFILE.showVisuals, agent.path, null, PROFILE.SEED, 0);
        gameRunCount++;
        accumulatedMillis += (System.currentTimeMillis() - startT);
        avgMillisPerGame = accumulatedMillis/gameRunCount;
        String progress = new DecimalFormat("#00.0#").format(gameRunCount*100/PROFILE.estimatedTotalRuns);
        System.out.println("ETA: " + getFullTimeString(getRemainingTimeInSeconds()) + "\t\t| (" + progress + "%)");

        return result;
    }
    private long getRemainingTime()
    {
        long ETA = (PROFILE.estimatedTotalRuns - gameRunCount) * avgMillisPerGame;
        return ETA;
    }
    private long getRemainingTimeInSeconds()
    {
        return getRemainingTime()/1000;
    }
    public String getTimeString(long Seconds)
    {
        long hours = Seconds / 60 / 60 % 24;
        long minutes = Seconds / 60 % 60;
        long seconds = Seconds % 60;
        return (hours + "h " + minutes + "m " + seconds + "s");
    }
    public String getFullTimeString(long Seconds)
    {
        long days = Seconds / 60 / 60 / 24;
        long hours = Seconds / 60 / 60 % 24;
        long minutes = Seconds / 60 % 60;
        long seconds = Seconds % 60;
        return (days + "d " + hours + "h " + minutes + "m " + seconds + "s");
    }
//region Private Classes
    private class Population
    {
        protected final List<Individual> INDIVIDUALS = new ArrayList<>();

        protected Population(List<Individual> individuals)
        {
            this.INDIVIDUALS.addAll(individuals);
            while(size() < PROFILE.populationSize)
                INDIVIDUALS.add(new Individual());
        }
        protected Population()
        {
            for (int i = 0; i < PROFILE.populationSize; i++)
                this.INDIVIDUALS.add(new Individual());
        }

        protected void fit()
        {
            for(Individual i : INDIVIDUALS) i.fit();
        }
        protected Individual getIndividual(int index)
        {
            return INDIVIDUALS.get(index);
        }

        protected void sort(boolean isAscending)
        {
            int flag = isAscending ? 1 : -1;
            INDIVIDUALS.sort((a, b) -> {if(a.getFitness() > b.getFitness()) return flag; else return -flag;});
        }
        protected Population clone()
        {
            List<Individual> inds = new ArrayList<>();
            for (Individual i : INDIVIDUALS)
            {
                inds.add(i.clone());
            }
            return new Population(inds);
        }
        protected void sort()
        {
            sort(false);
        }
        protected int size()
        {
            return INDIVIDUALS.size();
        }
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(Individual ind : INDIVIDUALS) sb.append(ind.getFitness() + ", ");
            sb.delete(sb.length()-2, sb.length());
            sb.append("]");
            return sb.toString();
        }
    }
    private class Individual
    {
        protected final int[] GENES;

        private Double fitness = null;

        protected Individual(int[] genes)
        {
            if(genes.length == PROFILE.designMachine.getNumDimensions()) this.GENES = genes;
            else throw new InvalidParameterException("Invalid gene count.");
        }
        protected Individual()
        {
            int length = PROFILE.designMachine.getNumDimensions();
            GENES = new int[length];
            for (int i = 0; i < length; i++)
            {
                int dimSize = PROFILE.designMachine.getDimSize(i);
                GENES[i] = new Random().nextInt(dimSize);
            }
        }

        protected Individual breed(Individual father)
        {
            int[] fGenes = father.GENES;
            int[] cGenes = GENES.clone();
            if(cGenes.length != fGenes.length) throw new IllegalArgumentException("Parents' genes count does not match.");

            for (int i = 0; i < cGenes.length; i++)
                if (new Random().nextDouble() <= PROFILE.crossoverRatio)
                    cGenes[i] = fGenes[i];

            Individual child = new Individual(cGenes);
            if(new Random().nextDouble() <= PROFILE.mutationRate) child.mutate();
            return child;
        }
        private Individual mutate()
        {
            int[] genes = GENES.clone();
            for (int i = 0; i < genes.length; i++)
                if (new Random().nextDouble() <= 1/genes.length)
                {
                    int lastGene;
                    do
                    {
                        lastGene = genes[i];
                        int dimSize = PROFILE.designMachine.getDimSize(i);
                        genes[i] = new Random().nextInt(dimSize);
                    } while (genes[i] == lastGene);
                }
            return new Individual(genes);
        }
        protected double fit()
        {
            double results[][] = new double[PROFILE.getAgents().size()][3];

            for (int a = 0; a < PROFILE.getAgents().size(); a++)
            {
                double result[] = new double[3];
                for (int r = 0; r < result.length; r++) result[r] = 0;
                for (int s = 0; s < PROFILE.individualFittingTrials; s++)
                {
                    double[] tRez = runSimulation(GENES, validateLevel(s), PROFILE.getAgent(a));
                    for (int r = 0; r < result.length; r++) result[r] += tRez[r];
                }
                for (int r = 0; r < result.length; r++) result[r] = result[r]/PROFILE.individualFittingTrials;
                results[a] = result;
            }
            setFitness(PROFILE.fit(results));
            return fitness;
        }
        public int validateLevel(int simulationIndex)
        {
            int lvl = 0;
            if(PROFILE.iterateThroughLevels)
            {
                int lvlCount = simulationIndex + 1;
                while (!ResourceLoader.validateResource(PROFILE.GAME_DIRECTORY + PROFILE.GAME + "_lvl" + (lvlCount-1) + ".txt"))
                    lvlCount--;
                int targetLvl = simulationIndex;
                while(!ResourceLoader.validateResource(PROFILE.GAME_DIRECTORY + PROFILE.GAME + "_lvl" + (targetLvl) + ".txt"))
                    targetLvl = simulationIndex % lvlCount;
                lvl = targetLvl;

            }
            return lvl;
        }


        protected double getFitness()
        {
            return this.fitness;
        }
        protected void setFitness(double fitness)
        {
            this.fitness = Double.valueOf(dFormat.format(fitness));
        }
        protected Individual clone()
        {
            Individual clone = new Individual(GENES.clone());
            clone.setFitness(getFitness());
            return clone;
        }
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int gene : GENES) sb.append(gene + ", ");
            sb.delete(sb.length()-2, sb.length());
            sb.append("]");
            return sb.toString();
        }
    }
//endregion
}
