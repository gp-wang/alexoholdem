package ao.regret.khun.node;

import ao.regret.InfoNode;
import ao.simple.kuhn.KuhnAction;
import ao.simple.kuhn.rules.KuhnBucket;
import ao.simple.kuhn.rules.KuhnRules;
import ao.simple.kuhn.state.StateFlow;
import ao.util.math.rand.Rand;
import ao.util.text.Txt;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 */
public class ProponentNode implements PlayerNode
{
    //--------------------------------------------------------------------
    private Map<KuhnAction, double[]> regret; // mutable doubles
    private Map<KuhnAction, InfoNode> actions;
//    private Map<KuhnAction, double[]> prob;  // mutable doubles
    private int                       visits = 0;


    //--------------------------------------------------------------------
    public ProponentNode(KuhnRules rules, KuhnBucket bucket)
    {
//        prob    = new EnumMap<KuhnAction, double[]>(KuhnAction.class);
        regret  = new EnumMap<KuhnAction, double[]>(KuhnAction.class);
        actions = new EnumMap<KuhnAction, InfoNode>(KuhnAction.class);

        for (Map.Entry<KuhnAction, KuhnRules> transition :
                rules.transitions().entrySet())
        {
            KuhnRules nextRules = transition.getValue();
            StateFlow nextState = nextRules.state();

            if (nextState.endOfHand())
            {
                actions.put(transition.getKey(),
                            new TerminalNode(
                                    bucket, nextState.outcome()));
            }
            else
            {
                actions.put(transition.getKey(),
                            new OpponentNode(nextRules, bucket));
            }
        }

        for (KuhnAction act : KuhnAction.VALUES)
        {
//            prob.put(act, new double[]{
//                             1.0 / KuhnAction.VALUES.length});
            regret.put(act, new double[]{0});
        }
    }


    //--------------------------------------------------------------------
    public KuhnAction nextAction()
    {
        double passProb = probabilities()[ 0 ];

        return Rand.nextBoolean( passProb )
                ? KuhnAction.PASS
                : KuhnAction.BET;
    }


    //--------------------------------------------------------------------
    public double probabilityOf(KuhnAction action)
    {
        return probabilities()[ action.ordinal() ];
    }

    public InfoNode child(KuhnAction forAction)
    {
        return actions.get( forAction );
    }


    //--------------------------------------------------------------------
    public void add(Map<KuhnAction, Double> counterfactualRegret)
    {
        for (Map.Entry<KuhnAction, Double> r :
                counterfactualRegret.entrySet())
        {
            regret.get( r.getKey() )[0] += r.getValue();
        }

        visits++;
    }


    //--------------------------------------------------------------------
//    public void updateActionPabilities()
//    {
//        double cumRegret = positiveCumulativeCounterfactualRegret();
//
//        if (cumRegret <= 0)
//        {
//            for (double[] p : prob.values())
//            {
//                p[0] = 1.0 / KuhnAction.VALUES.length;
//            }
//        }
//        else
//        {
//            for (Map.Entry<KuhnAction, double[]> p : prob.entrySet())
//            {
//                double cRegret = regret.get( p.getKey() )[0];
//
//                p.getValue()[0] =
//                        (cRegret < 0)
//                        ? 0
//                        : cRegret / cumRegret;
//            }
//        }
//    }

    private double positiveCumulativeCounterfactualRegret()
    {
        double positiveCumulation = 0;
        for (double[] pointRegret : regret.values())
        {
            if (pointRegret[0] > 0)
            {
                positiveCumulation += pointRegret[0];
            }
        }
        return positiveCumulation;
    }


    //--------------------------------------------------------------------
    public String toString(KuhnAction action)
    {
        return new StringBuilder()
                .append( action )
                .append( " :: " )
                .append( probabilities()[ action.ordinal() ] )
                .append( " :: " )
                .append( regret.get(action )[0] / visits)
                .append( " :: " )
                .append( visits )
                .toString();

    }

    public String toString(int depth)
    {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<KuhnAction, InfoNode> action : actions.entrySet())
        {
            str.append( Txt.nTimes("\t", depth) )
               .append( action.getKey() )
               .append( " :: " )
               .append( probabilities()[ action.getKey().ordinal() ] )
               .append( " :: " )
               .append( regret.get(action.getKey())[0] / visits)
               .append( " :: " )
               .append( visits )
               .append( "\n" )
               .append( action.getValue().toString(depth + 1) )
               .append( "\n" );
        }
        return str.substring(0, str.length()-1);
    }


    //--------------------------------------------------------------------
    private double[] probabilities()
    {
        double prob[]    = new double[2];
        double cumRegret = positiveCumulativeCounterfactualRegret();

        if (cumRegret <= 0)
        {
            prob[0] = prob[1] = 1.0 / 2;
        }
        else
        {
            prob[0] = Math.max(0,
                        regret.get( KuhnAction.PASS )[0] / cumRegret);

            prob[1] = Math.max(0,
                        regret.get( KuhnAction.BET  )[0] / cumRegret);
        }

        return prob;
    }
}
