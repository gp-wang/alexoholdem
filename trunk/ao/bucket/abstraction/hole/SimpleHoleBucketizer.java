package ao.bucket.abstraction.hole;

import ao.bucket.index.CanonTraverser;
import ao.bucket.index.CanonTraverser.Traverser;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;
import ao.holdem.model.card.sequence.CardSequence;
import ao.odds.agglom.impl.GeneralOddFinder;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 */
public class SimpleHoleBucketizer implements HoleBucketizer
{
    //--------------------------------------------------------------------
    private final Hole   revIndex[];
    private final Double histograms[];
    private final Short  inOrder[];


    //--------------------------------------------------------------------
    public SimpleHoleBucketizer()
    {
        revIndex   = new Hole  [ Hole.CANONICAL_COUNT ];
        histograms = new Double[ Hole.CANONICAL_COUNT ];
        inOrder    = new Short [ Hole.CANONICAL_COUNT ];
    }


    //--------------------------------------------------------------------
    private void initOrder()
    {
        new CanonTraverser().traverse(new Traverser() {
            public void traverse(CardSequence cards) {
                Hole hole = cards.hole();

                histograms[ hole.canonIndex() ] =
                        new GeneralOddFinder().compute(
                                hole, Community.PREFLOP
                        ).strengthVsRandom();
//                        new GeneralHistFinder().compute(
//                                hole, Community.PREFLOP).mean();
                revIndex  [ hole.canonIndex() ] = hole;
            }
        });

        for (short i = 0; i < inOrder.length; i++) inOrder[ i ] = i;
        Arrays.sort(inOrder, new Comparator<Short>() {
            public int compare(Short a, Short b) {
                return histograms[ a ].compareTo(
                            histograms[ b ]);
            }
        });
    }


    //--------------------------------------------------------------------
    public short[][] bucketize( int buckets )
    {
        if (histograms[0] == null) initOrder();

        short holes[][] = new short[ buckets ][];

        int index = 0;
        int chunk = (int) Math.ceil(
                      ((double) histograms.length) / holes.length);
        for (int i = 0; i < holes.length; i++)
        {
            holes[ i ] = new short[
                    Math.min(chunk,
                             histograms.length - index) ];
            for (int j = 0;
                     j < chunk && index < histograms.length;
                     j++)
            {
                holes[ i ][ j ] = inOrder[ index++ ];
            }
        }

        return holes;
    }


    //--------------------------------------------------------------------
    public void display( short buckets[][] )
    {
        for (short canons[] : buckets)
        {
            display( canons );
        }
    }
    public void display(short canons[])
    {
        if (canons.length == 0) return;
        if (histograms[0] == null) initOrder();

        for (short canon : canons)
        {
            System.out.print( revIndex[ canon ] + "\t" );
        }
        System.out.println();
    }


    //--------------------------------------------------------------------
    public String id()
    {
        return "simple_odds";
//        return "simple_histograms";
    }
    public String toString()
    {
        return id();
    }
}