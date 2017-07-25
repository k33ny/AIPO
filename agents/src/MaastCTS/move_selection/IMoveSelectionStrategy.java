package MaastCTS.move_selection;

import MaastCTS.test.IPrintableConfig;
import ontology.Types.ACTIONS;
import MaastCTS.model.MctNode;

public interface IMoveSelectionStrategy extends IPrintableConfig
{
	
	/** Should be implemented to select a move to play in the real game for the position in the given root */
	public ACTIONS selectMove(MctNode root);

}
