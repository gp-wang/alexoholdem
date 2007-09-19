package ao.holdem.history.state;

import ao.holdem.bots.util.Util;
import ao.holdem.def.model.cards.Hole;
import ao.holdem.def.state.action.Action;
import ao.holdem.def.state.env.RealAction;

/**
 *
 */
public class StatePlayerImpl implements StatePlayer
{
    public RealAction act(RunningState env)
    {
        HoldemState state = env.head();
        Hole        hole  = env.cards().holeFor(
                                state.nextToAct().handle() );

        int group = Util.sklanskyGroup( hole );

        if (group <= 4)
        {
            return Action.RAISE_OR_CALL.toRealAction(state);
        }
        return Action.CHECK_OR_FOLD.toRealAction(state);
    }
}