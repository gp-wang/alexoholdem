package ao.simple.alexo.player;

import ao.simple.alexo.AlexoAction;
import ao.simple.alexo.AlexoPlayer;
import ao.simple.alexo.card.AlexoCardSequence;
import ao.simple.alexo.state.AlexoState;

/**
 *
 */
public class AlwaysFold implements AlexoPlayer
{
    public AlexoAction act(
            AlexoState        state,
            AlexoCardSequence cards)
    {
        return AlexoAction.FOLD;
    }
}
