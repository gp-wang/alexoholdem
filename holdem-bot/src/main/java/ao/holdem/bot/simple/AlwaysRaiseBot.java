package ao.holdem.bot.simple;

import ao.holdem.bot.AbstractPlayer;
import ao.holdem.engine.state.ActionState;
import ao.holdem.model.Avatar;
import ao.holdem.model.ChipStack;
import ao.holdem.model.act.Action;
import ao.holdem.model.act.FallbackAction;
import ao.holdem.model.card.sequence.CardSequence;

import java.util.Map;

//import ao.holdem.engine.analysis.Analysis;

/**
 *
 */
public class AlwaysRaiseBot extends AbstractPlayer
{
    //--------------------------------------------------------------------
    public void handEnded(Map<Avatar, ChipStack> deltas) {}

    
    //--------------------------------------------------------------------
    public Action act(ActionState state,
                      CardSequence cards/*,
                      Analysis     analysis*/)
    {
        return state.reify(FallbackAction.RAISE_OR_CALL);
    }
}
