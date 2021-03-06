package ao.holdem.abs.bucket.index.detail.preflop;

import ao.holdem.model.card.canon.hole.CanonHole;
import ao.holdem.canon.enumeration.HandEnum;
import ao.holdem.model.card.Community;
import ao.holdem.abs.odds.Odds;
import ao.holdem.abs.odds.agglom.impl.PreciseHeadsUpOdds;
import ao.util.pass.Traverser;
import org.apache.log4j.Logger;

/**
 * Date: Jan 27, 2009
 * Time: 11:02:10 PM
 */
public enum HoleOdds
{;
    //--------------------------------------------------------------------
    private static final Logger LOG =
            Logger.getLogger(HoleOdds.class);


    //--------------------------------------------------------------------
    static
    {
        LOG.debug("initializing");

        if (! HoleOddsDao.retrieve())
        {
            LOG.debug("starting from scratch");
            HoleOddsDao.init();
        }

        if (! HoleOddsDao.isPreComputed())
        {
            LOG.debug("resuming computation");

            computeOdds();
            HoleOddsDao.persist();
            HoleOddsDao.setPreComputed();
        }
    }


    //--------------------------------------------------------------------
    private static void computeOdds()
    {
        LOG.debug("computing odds");

        final int[]  skinCount      = {0};
        final int[]  calcCount      = {0};
        final long[] milestoneStart = {0};

        HandEnum.uniqueHoles(
                new Traverser<CanonHole>() {
            public void traverse(CanonHole hole) {
                if (HoleOddsDao.isSet( hole.canonIndex() )) {
                    skinCount[0]++;
                    return;
                } else if (skinCount[0] != 0) {
                    LOG.info("skipped " + skinCount[0]);
                    skinCount[0] = 0;
                }

                Odds odds = PreciseHeadsUpOdds.INSTANCE.compute(
                                hole.reify(), Community.PREFLOP);

                HoleOddsDao.set(hole.canonIndex(), odds);

                checkpoint( calcCount[0]++, milestoneStart );
            }});
    }

    private static void checkpoint(
            int count, long[] milestoneStart)
    {
        if (milestoneStart[0] == 0)
            milestoneStart[0] = System.currentTimeMillis();
        System.out.print(".");

        boolean milesoneReached = ((count + 1) % 13 == 0);
        if (milesoneReached) {
            System.out.println(
                    " " + (count + 1) + ", completed 13 in " +
                    (System.currentTimeMillis() - milestoneStart[0]));
            HoleOddsDao.persist();
        }

        if (milesoneReached)
            milestoneStart[0] = System.currentTimeMillis();
    }


    //--------------------------------------------------------------------
    public static Odds lookup(int canonHoleIndex)
    {
        return HoleOddsDao.get(canonHoleIndex);
    }
}
