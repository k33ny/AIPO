/**
 * Created by Martynas Petuska on 18/07/2017.
 */
public interface Fittable
{
    /**
     * Fits a single simulation.
     * @param result the outcome of the simulation. result{isWin[0:1], score, ticks}.
     * @return calculated fitness.
     */
    double fit(double[] result);

    /**
     * Fits multiple simulations, possibly run with different agents.
     * @param result the outcomes of the simulations. result[path]{isWin[0:1], score, ticks}
     * @return
     */
    double fit(double[][] result);

    /**
     * Fits multiple simulations, possibly run with different agents.
     * @param result the outcomes of the simulations. result[path]{isWin[0:1], score, ticks}
     * @return
     */
    double fit(double[][][] result);
    String goal();
}