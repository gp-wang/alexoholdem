package ao.irc;

import ao.holdem.engine.Player;
import ao.holdem.engine.RuleBreach;
import ao.holdem.engine.state.State;
import ao.holdem.model.Avatar;
import ao.holdem.model.ChipStack;
import ao.holdem.model.act.Action;
import ao.holdem.model.card.sequence.CardSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import ao.holdem.engine.analysis.Analysis;

/**
 *
 */
public class IrcPlayer implements Player
{
    //--------------------------------------------------------------------
    private List<Action> acts;
    private int          nextAction;


    //--------------------------------------------------------------------
    public void handEnded(Map<Avatar, ChipStack> deltas) {}

    
    //--------------------------------------------------------------------
    public IrcPlayer(IrcAction action)
    {
        acts       = new ArrayList<Action>();
        nextAction = 0;

        acts.addAll(Arrays.asList( action.preFlop() ));
        acts.addAll(Arrays.asList( action.onFlop()  ));
        acts.addAll(Arrays.asList( action.onTurn()  ));
        acts.addAll(Arrays.asList( action.onRiver() ));
    }


    //--------------------------------------------------------------------
    @Override public void observe(State nextToActState) {}


    //--------------------------------------------------------------------
    public Action act(State        state,
                      CardSequence cards/*,
                      Analysis     analysis*/)
    {
        if (! nextActionInBounds())
        {
            throw new RuleBreach("unspecified move: " + state);
        }

        return acts.get( nextAction++ );
    }

    public boolean hasQuit()
    {
        boolean isQuit = nextActionInBounds() &&
                         acts.get(nextAction).equals( Action.QUIT );
        if (isQuit) nextAction++;
        return isQuit;
    }


    //--------------------------------------------------------------------
    private boolean nextActionInBounds()
    {
        return acts.size() > nextAction;
    }
}
