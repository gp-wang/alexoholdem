package ao.holdem.abs.calc;

import ao.holdem.abs.bucket.index.detail.river.compact.MemProbCounts;
import ao.holdem.canon.river.River;

/**
 * 13/02/14 6:43 PM
 */
public class MemProbCountsTest
{
    //--------------------------------------------------------------------
    public static void main(String[] args)
    {
        double totalProb = 0;
        for (long r = 0; r < River.CANONS; r++) {
//            totalCount += MemProbCounts.riverCount(r);
            totalProb += MemProbCounts.compactNonLossProbability(r) - 0.5;
        }
        System.out.println("totalProb: " + totalProb);

//        RiverEvalLookup.traverse(
//                new CanonRange[]{CanonRange.newFromCount(
//                    0, 100
////                    (long) Integer.MAX_VALUE - 1, 100
////                        0, River.CANONS
//                )},
//                new RiverEvalLookup.VsRandomVisitor() {
//                    public void traverse(
//                            long canonIndex,
//                            double strengthVsRandom,
//                            byte represents) {
//                        byte rep = MemProbCounts.riverCount(canonIndex);
//                        if (rep != represents) {
//                            System.out.println(canonIndex + " err: " + rep + " vs " + represents);
//                            System.exit(1);
//                        }
//                    }
//                });
    }
}
