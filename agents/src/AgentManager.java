/**
 * Created by Martynas Petuska on 26/07/2017.
 */
public class AgentManager
{
//region CIG 2016 Agents
    public static final AgentWrap YOLOBOT = new AgentWrap("YOLOBOT", "YOLOBOT.Agent", 1, 96, 104, 0); //Very competent but fails to initialise most of the time
    public static final AgentWrap adrienctx = new AgentWrap("adrienctx", "adrienctx.Agent", 3, 75, 61, 0);
    public static final AgentWrap MaastCTS2 = new AgentWrap("MaastCTS", "MaastCTS2.Agent", 4, 71, 70, 9); //Initialisation issues.
    public static final AgentWrap Number27 = new AgentWrap("Number27", "Number27.Agent", 5, 71, 84, 0);
    public static final AgentWrap muzzle = new AgentWrap("muzzle", "muzzle.Agent", 6, 65, 51, 7);
    public static final AgentWrap NovelTS = new AgentWrap("NovelTS", "NovelTS.Agent", 7, 56, 50, 0);
    public static final AgentWrap sampleOneStepLookahead = new AgentWrap("sampleOneStepLookahead", "sampleOneStepLookahead.sampleonesteplookahead.Agent", 8, 44, 35, 5);
    public static final AgentWrap thorbjrn = new AgentWrap("thorbjrn", "thorbjrn.Agent", 10, 38, 42, 0);
    //Return42 removed due to it's incompatibility to the new version of the GVGAI framework.
    //NovTea worked, but was removed due to occasional crashes discovered. Possibly due to the new GVGAI framework.
//endregion
//region Sample Agents
    public static final AgentWrap sampleRandomController = new AgentWrap("sampleRandom", "tracks.singlePlayer.simple.sampleRandom.Agent");
    public static final AgentWrap doNothingController = new AgentWrap("doNothing", "tracks.singlePlayer.simple.doNothing.Agent");
    public static final AgentWrap sampleOneStepController = new AgentWrap("sampleOneStepLookAhead", "tracks.singlePlayer.simple.sampleonesteplookahead.Agent");
    public static final AgentWrap sampleMCTSController = new AgentWrap("sampleMCTS(depreciated)", "tracks.singlePlayer.deprecated.sampleMCTS.Agent");
    public static final AgentWrap sampleFlatMCTSController = new AgentWrap("sampleFlatMCTS", "tracks.singlePlayer.simple.greedyTreeSearch.Agent");
    public static final AgentWrap sampleOLMCTSController = new AgentWrap("sampleMCTS(advanced)", "tracks.singlePlayer.advanced.sampleMCTS.Agent");
    public static final AgentWrap sampleGAController = new AgentWrap("sampleGA", "tracks.singlePlayer.deprecated.sampleGA.Agent");
    public static final AgentWrap sampleOLETSController = new AgentWrap("sampleOLETS", "tracks.singlePlayer.advanced.olets.Agent");
    public static final AgentWrap repeatOLETS = new AgentWrap("repeatOLETS", "tracks.singlePlayer.tools.repeatOLETS.Agent");
//endregion

    //Return42 removed due to it's incompatibility to the new version of the GVGAI framework.
    //NovTea worked, but was removed due to occasional crashes discovered. Possibly due to the new GVGAI framework.
}
