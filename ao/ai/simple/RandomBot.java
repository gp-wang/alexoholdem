package ao.ai.simple;

import ao.ai.AbstractPlayer;
import ao.holdem.model.act.EasyAction;
import ao.holdem.model.card.Hole;
import ao.persist.HandHistory;
import ao.state.HandState;
import ao.state.StateManager;
import ao.util.rand.Rand;


/**
 *
 */
public class RandomBot extends AbstractPlayer
{
    //--------------------------------------------------------------------
    public void handEnded(HandHistory history) {}


    //--------------------------------------------------------------------
    protected EasyAction act(StateManager env, HandState state, Hole hole)
    {
        return Rand.fromArray( EasyAction.values() );
    }
}
