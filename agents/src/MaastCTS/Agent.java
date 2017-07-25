package MaastCTS;

import java.awt.Graphics2D;

import MaastCTS.controller.IController;
import MaastCTS.controller.MctsController;
import MaastCTS.heuristics.states.IPlayoutEvaluation;
import MaastCTS.move_selection.IMoveSelectionStrategy;
import MaastCTS.playout.IPlayoutStrategy;
import MaastCTS.playout.NstPlayout;
import MaastCTS.heuristics.states.GvgAiEvaluation;
import MaastCTS.move_selection.MaxAvgScore;
import MaastCTS.selection.ISelectionStrategy;
import MaastCTS.selection.ol.ProgressiveHistory;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {
	public static IController controller;

	/**
	 * constructor for competition
	 * 
	 * @param so
	 * @param elapsedTimer
	 */
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
		MctsController.TIME_BUFFER_MILLISEC = 8;	// shorter time buffer because better hardware on official competition server
		controller = new MctsController(new ProgressiveHistory(0.6, 1.0), new NstPlayout(10, 0.5, 7.0, 3),
				new MaxAvgScore(), new GvgAiEvaluation(), true, true, true, true, true, 0.6, 3, true, false);
		controller.init(so, elapsedTimer);
	}

	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer,
                 ISelectionStrategy selectionStrategy, IPlayoutStrategy playoutStrategy,
                 IMoveSelectionStrategy moveSelectionStrategy, IPlayoutEvaluation playoutEval,
                 boolean initBreadthFirst, boolean noveltyBasedPruning, boolean exploreLosses,
                 boolean knowledgeBasedEval, boolean detectDeterministicGames, boolean treeReuse,
                 double treeReuseGamma, int maxNumSafetyChecks, boolean alwaysKB, boolean noTreeReuseBFTI) {
		controller = new MctsController(selectionStrategy, playoutStrategy, moveSelectionStrategy, 
										playoutEval, initBreadthFirst, noveltyBasedPruning, exploreLosses,
										knowledgeBasedEval, treeReuse, treeReuseGamma, maxNumSafetyChecks, alwaysKB, noTreeReuseBFTI);
		controller.init(so, elapsedTimer);
	}
	
	public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer, IController controller){
		Agent.controller = controller;
		controller.init(so,  elapsedTimer);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		Globals.knowledgeBase.update(stateObs);
		return controller.chooseAction(stateObs, elapsedTimer);
	}
	
	@Override
	public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer){
		controller.result(stateObservation, elapsedCpuTimer);
    }
	
	@Override
	public void draw(Graphics2D g){
		if(Globals.DEBUG_DRAW){
			Globals.knowledgeBase.draw(g);
		}
	}

}
