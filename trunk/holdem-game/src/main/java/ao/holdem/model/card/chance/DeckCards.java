package ao.holdem.model.card.chance;

import ao.holdem.model.Avatar;
import ao.holdem.model.Round;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DeckCards implements ChanceCards
{
    //--------------------------------------------------------------------
    private final Deck              cards;
    private final Map<Avatar, Hole> holes;
    private       Community         community = Community.PREFLOP;


    //--------------------------------------------------------------------
    public DeckCards()
    {
        cards = new Deck();
        holes = new HashMap<Avatar, Hole>();
    }

    private DeckCards(Deck copyCards,
                      Map<Avatar, Hole> copyHoles,
                      Community copyCommunity)
    {
        cards     = copyCards;
        holes     = copyHoles;
        community = copyCommunity;
    }


    //--------------------------------------------------------------------
    public Hole hole(Avatar forPlayer)
    {
        Hole hole = holes.get( forPlayer );
        if (hole == null)
        {
            hole = cards.nextHole();
            holes.put( forPlayer, hole );
        }
        return hole;
    }


    //--------------------------------------------------------------------
    public Community community(Round asOf)
    {
//        Round safeRound =
//                (asOf == null)
//                 ? Round.RIVER : asOf;
        switch (asOf)
        {
            case RIVER:
                flop();
                turn();
                river();
                break;
            
            case TURN:
                flop();
                turn();
                break;

            case FLOP:
                flop();
                break;
        }

//        return community.asOf( safeRound );
        return community.asOf( asOf );
    }

    private void flop()
    {
        if (! community.hasFlop())
        {
            community = cards.nextFlop();
        }
    }
    private void turn()
    {
        if (! community.hasTurn())
        {
            community = community.addTurn( cards.nextCard() );
        }
    }
    private void river()
    {
        if (! community.hasRiver())
        {
            community = community.addRiver( cards.nextCard() );
        }
    }


    //--------------------------------------------------------------------
    public void reset()
    {
        cards.reset();
        holes.clear();
        community = Community.PREFLOP;
    }


    //--------------------------------------------------------------------
    public DeckCards prototype()
    {
        return new DeckCards(cards.prototype(),
                             new HashMap<Avatar, Hole>(holes),
                             community);
    }
}
