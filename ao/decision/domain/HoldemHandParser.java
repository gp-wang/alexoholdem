package ao.decision.domain;

import ao.decision.attr.Attribute;
import ao.decision.attr.AttributePool;
import ao.decision.context.HoldemContext;
import ao.decision.context.HoldemExampleSet;
import ao.decision.context.immediate.FirstActContext;
import ao.decision.context.immediate.PostFlopContext;
import ao.decision.context.immediate.PreFlopContext;
import ao.decision.data.ContextImpl;
import ao.decision.data.Example;
import ao.ai.opp_model.predict.def.context.GenericContext;
import ao.ai.opp_model.predict.def.retro.HandParser;
import ao.odds.CommunityMeasure;
import ao.holdem.def.state.domain.BettingRound;
import ao.holdem.def.state.env.TakenAction;
import ao.holdem.history.HandHistory;
import ao.holdem.history.PlayerHandle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class HoldemHandParser
{
    //--------------------------------------------------------------------
    private AttributePool    pool       = new AttributePool();
    private CommunityMeasure thermostat = new CommunityMeasure();
    private HandParser       parser     = new HandParser();


    //--------------------------------------------------------------------
    public HoldemContext nextToActContext(
            HandHistory hand, PlayerHandle player)
    {
        GenericContext ctx = parser.genericNextToActContext(hand, player);
        return fromGeneric(ctx);
    }

    private HoldemContext fromGeneric(GenericContext ctx)
    {
        if (ctx.round() == BettingRound.PREFLOP)
        {
            if (ctx.isHistAware())
            {
                return new PreFlopContext(pool, ctx);
            }
            else
            {
                return new FirstActContext(pool, ctx);
            }
        }
        else
        {
            return new PostFlopContext(pool, ctx);
        }
    }

    
    //--------------------------------------------------------------------
    public HoldemExampleSet examples(
            HandHistory inHand, PlayerHandle forPlayer)
    {
        HoldemExampleSet examples = new HoldemExampleSet();
        for (GenericContext ctx :
                parser.genericCasesFor(inHand, forPlayer))
        {
            examples.add(fromGeneric(ctx),
                         pool.fromEnum( ctx.currAct() ));
        }
        return examples;
    }


    //--------------------------------------------------------------------
    public List<Example<TakenAction>> postflopExamples(
            HandHistory hand,
            PlayerHandle player)
    {
        List<Example<TakenAction>> examples =
                new ArrayList<Example<TakenAction>>();

        HandParser parser = new HandParser();
        for (GenericContext ctx :
                parser.genericCasesFor(hand, player))
        {
            if (! ctx.isHistAware()) continue;
            if (ctx.round() == BettingRound.PREFLOP) continue;

            ContextImpl decisionContext = asDecisionContext(ctx);
            Example<TakenAction> decisionExample =
                    decisionContext.withTarget(
                            pool.fromEnum( ctx.currAct() ));
            examples.add( decisionExample );
        }

        return examples;
    }


    //--------------------------------------------------------------------
    public ContextImpl asDecisionContext(GenericContext ctx)
    {
        Collection<Attribute> attrs = new ArrayList<Attribute>();

        attrs.add(pool.fromUntyped(
                "Committed This Round",
                ctx.committedThisRound()));

        attrs.add(pool.fromEnum(
                BetsToCall.fromBets(ctx.betsToCall())));

        attrs.add(pool.fromEnum( ctx.round() ));

        attrs.add(pool.fromUntyped(
                "Last Bets Called > 0",
                ctx.lastBetsToCall() > 0));
//        attrs.add(pool.fromUntyped(
//                "Last Bets Called",
//                ctx.lastBetsToCall()));

//        attrs.add(pool.fromUntyped(
//                "Last Action", ctx.lastAct()));
        attrs.add(pool.fromUntyped(
                "Last Act: Bet/Raise",
                ctx.lastAct() == TakenAction.RAISE));

        attrs.add(pool.fromEnum(
                ActivePosition.fromPosition(
                        ctx.numOpps(), ctx.activePosition())));

        attrs.add(pool.fromEnum(
                ActiveOpponents.fromActiveOpps(
                        ctx.numActiveOpps())));

        attrs.add(pool.fromEnum(
                BetRatio.fromBetRatio(
                        ctx.betRatio())));
        attrs.add(pool.fromEnum(
                PotOdds.fromPotOdds(
                        ctx.immedatePotOdds())));
        attrs.add(pool.fromEnum(
                PotRatio.fromPotRatio(
                        ctx.potRatio())));

        attrs.add(pool.fromEnum(
                Heat.fromHeat(
                        thermostat.heat(ctx.community()))));

//        attrs.add(pool.fromUntyped(
//                "Flush Possible",
//                ctx.community().flushPossible()));
//        attrs.add(pool.fromUntyped(
//                "Ace On Board",
//                ctx.community().contains(Card.Rank.ACE)));
//        attrs.add(pool.fromUntyped(
//                "King On Board",
//                ctx.community().contains(Card.Rank.KING)));
//        attrs.add(pool.fromEnum(
//                AceQueenKing.fromCommunity(
//                        ctx.community())));

        return new ContextImpl(attrs);
    }

}
