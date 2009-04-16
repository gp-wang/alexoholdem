package ao.regret.khun.node;

import ao.regret.InfoNode;
import ao.simple.kuhn.rules.KuhnBucket;
import ao.simple.kuhn.state.KuhnOutcome;
import ao.util.text.Txt;

/**
 *
 */
public class TerminalNode implements InfoNode
{
    //--------------------------------------------------------------------
    private final KuhnBucket  BUCKET;
    private final KuhnOutcome OUTCOME;


    //--------------------------------------------------------------------
    public TerminalNode(KuhnBucket bucket, KuhnOutcome outcome)
    {
        BUCKET  = bucket;
        OUTCOME = outcome;
    }


    //--------------------------------------------------------------------
    // this is the stack delta, ie. stakes (excludes your commit)
    public double expectedValue(TerminalNode vsLastToAct)
    {
        if (OUTCOME.isShowdown())
        {
            double toWin =
                    BUCKET.against( vsLastToAct.BUCKET );

            return OUTCOME == KuhnOutcome.SHOWDOWN // as opposed to
                    ? 2.0 * (toWin - 0.5)          // DOUBLE_SHOWDOWN
                    : 4.0 * (toWin - 0.5);
        }
        else
        {
            return OUTCOME == KuhnOutcome.PLAYER_ONE_WINS
                    ? 1 : -1;
        }
    }


    //--------------------------------------------------------------------
    public String toString()
    {
        return OUTCOME.toString();
    }

    public String toString(int depth)
    {
        return Txt.nTimes("\t", depth) +
               OUTCOME;
//        return Txt.nTimes("\t", depth) +
//                BUCKET + ", " + OUTCOME;
    }
}
