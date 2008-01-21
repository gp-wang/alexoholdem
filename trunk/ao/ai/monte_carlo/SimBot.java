package ao.ai.monte_carlo;

import ao.ai.AbstractPlayer;
import ao.ai.opp_model.decision.classification.RealHistogram;
import ao.holdem.model.act.EasyAction;
import ao.holdem.model.act.SimpleAction;
import ao.holdem.model.card.Hole;
import ao.persist.HandHistory;
import ao.persist.PlayerHandle;
import ao.state.HandState;
import ao.state.PlayerState;
import ao.state.StateManager;
import com.google.inject.Inject;

import java.util.*;

/**
 *
 */
public class SimBot extends AbstractPlayer
{
    //--------------------------------------------------------------------
    private static final int SIM_COUNT = 256;


    //--------------------------------------------------------------------
    @Inject PredictorService predictor;
//    private PredictorService predictor;

    
    //--------------------------------------------------------------------
    public SimBot()
    {
        //predictor = new PredictorService();
    }


    //--------------------------------------------------------------------
    protected EasyAction act(
            StateManager env,
            HandState    state,
            Hole         hole)
    {
        Map<SimpleAction, int[]>    counts      = initCounts();
        Map<SimpleAction, double[]> expectation = initExpectations();

        Map<PlayerHandle, List<Choice>> baseChoices =
                predictor.extractChoices( env.toHistory() );

        for (int i = 0; i < SIM_COUNT; i++)
        {
            runSimulation(env, state, counts, expectation, baseChoices);
        }

        SimpleAction bestAct    = SimpleAction.FOLD;
        double       mostExpect = Long.MIN_VALUE;
        for (SimpleAction act : SimpleAction.values())
        {
            if (counts.get(act)[0] == 0) continue;

            double expect = expectation.get( act )[0] /
                                counts.get( act )[0];
            //System.out.println(act + "\t" + expect);

            if (expect > mostExpect)
            {
                mostExpect = expect;
                bestAct    = act;
            }
        }

        return bestAct.toEasyAction();
    }


    //--------------------------------------------------------------------
    private void runSimulation(
            StateManager                    env,
            HandState                       state,
            Map<SimpleAction, int[]>        counts,
            Map<SimpleAction, double[]>     expectation,
            Map<PlayerHandle, List<Choice>> baseChoices)
    {
        Map<PlayerHandle, List<Choice>> choices =
                cloneBaseChoices(baseChoices);
        Map<PlayerHandle, BotPredictor> brains  =
                new HashMap<PlayerHandle, BotPredictor>();

        List<PlayerHandle> atShowdown = initBrains(state, brains);

        Simulator         sim = new Simulator(env, brains);
        Simulator.Outcome out = sim.playOutHand();

        extractSimulatedChoices(brains, choices, atShowdown);

        PlayerHandle me      = env.nextToAct();
        double       winProb = winProbability(me, choices);

        int ifLoss =
                choices.containsKey( me )
                ? -out.showdownStakes().smallBlinds()
                : -brains.get( me ).lastChoice().state()
                        .stakes().smallBlinds();
        int ifWin  = out.totalCommit().smallBlinds()
                      -out.showdownStakes().smallBlinds();

        SimpleAction act =
                out.events().get(0).getAction().toSimpleAction();
        expectation.get(act)[0] +=
                (winProb * ifWin + (1.0 - winProb) * ifLoss)
                  * out.probability();
        counts.get(act)[0]++;
    }

    private Map<PlayerHandle, List<Choice>> cloneBaseChoices(
                Map<PlayerHandle, List<Choice>> baseChoices)
    {
        Map<PlayerHandle, List<Choice>> clone =
                new HashMap<PlayerHandle, List<Choice>>();
        for (Map.Entry<PlayerHandle, List<Choice>> choice :
                baseChoices.entrySet())
        {
            clone.put(choice.getKey(),
                      new ArrayList<Choice>( choice.getValue() ));
        }
        return clone;
    }

    private double winProbability(
            PlayerHandle                    me,
            Map<PlayerHandle, List<Choice>> choices)
    {
        if (choices.isEmpty())
        {
            // everybody (including me) folded, leaving some player
            //  the winner without acting
            return 0;
        }
        else if (choices.size() == 1)
        {
            return choices.containsKey( me ) ? 1.0 : 0.0;
        }
        else
        {
            RealHistogram<PlayerHandle> results =
                    predictor.approximate( choices );
            return results.probabilityOf( me );
        }
    }

    private void extractSimulatedChoices(
            Map<PlayerHandle, BotPredictor> brains,
            Map<PlayerHandle, List<Choice>> choices,
            List<PlayerHandle>              atShowdown)
    {
        for (Map.Entry<PlayerHandle, BotPredictor> p :
                brains.entrySet())
        {
            BotPredictor predictor = p.getValue();
            if (predictor.isUnfolded() && predictor.hasActed())
            {
                List<Choice> base = choices.get( p.getKey() );
                if (base == null)
                {
                    base = new ArrayList<Choice>();
                    choices.put(p.getKey(), base);
                }
                base.addAll( p.getValue().choices() );
                atShowdown.add( p.getKey() );
            }
        }
        choices.keySet().retainAll( atShowdown );
    }


    //--------------------------------------------------------------------
    private List<PlayerHandle> initBrains(
            HandState                       state,
            Map<PlayerHandle, BotPredictor> brains)
    {
        List<PlayerHandle> atShowdown = new ArrayList<PlayerHandle>();

        for (PlayerState pState : state.unfolded())
        {
            if (! pState.isAllIn())
            {
                brains.put(pState.handle(),
                           new BotPredictor(pState.handle(),
                                            predictor));
            }
            else
            {
                atShowdown.add( pState.handle() );
            }
        }

        return atShowdown;
    }

    private Map<SimpleAction, int[]> initCounts()
    {
        Map<SimpleAction, int[]> counts =
                new EnumMap<SimpleAction, int[]>( SimpleAction.class );
        for (SimpleAction act : SimpleAction.values())
        {
            counts.put(act, new int[1]);
        }
        return counts;
    }

    private Map<SimpleAction, double[]> initExpectations()
    {
        Map<SimpleAction, double[]> expectation =
                new EnumMap<SimpleAction, double[]>( SimpleAction.class );
        for (SimpleAction act : SimpleAction.values())
        {
            expectation.put(act, new double[1]);
        }
        return expectation;
    }


    //--------------------------------------------------------------------
    public void handEnded(HandHistory history)
    {
        predictor.add( history );
    }
}