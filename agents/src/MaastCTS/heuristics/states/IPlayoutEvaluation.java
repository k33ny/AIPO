package MaastCTS.heuristics.states;

import MaastCTS.test.IPrintableConfig;
import core.game.StateObservation;

public interface IPlayoutEvaluation extends IPrintableConfig
{
	public double scorePlayout(StateObservation stateObs);
}
