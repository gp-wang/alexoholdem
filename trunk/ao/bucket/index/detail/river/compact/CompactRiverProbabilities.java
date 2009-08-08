package ao.bucket.index.detail.river.compact;

import ao.Infrastructure;
import ao.bucket.index.canon.river.RiverLookup;
import ao.bucket.index.detail.range.CanonRange;
import ao.bucket.index.detail.river.ProbabilityEncoding;
import ao.bucket.index.detail.river.RiverEvalLookup;
import ao.util.data.primitive.CharList;
import ao.util.persist.PersistentChars;
import ao.util.time.Stopwatch;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * User: alex
 * Date: 1-Jul-2009
 * Time: 5:58:02 PM
 */
public class CompactRiverProbabilities
{
    //--------------------------------------------------------------------
    private static final Logger LOG =
            Logger.getLogger(CompactRiverProbabilities.class);

    private CompactRiverProbabilities() {}

    public static void main(String[] args) {
        for (int raw = 0; raw < ProbabilityEncoding.COUNT; raw++) {
            LOG.info(raw + "\t" + (int) compact((char) raw));
        }
    }


    //--------------------------------------------------------------------
    private static final String   compactFile = Infrastructure.path(
                        "lookup/eval/river/compact_prob.char");
    private static final String unCompactFile = Infrastructure.path(
                        "lookup/eval/river/uncompact_prob.char");

    private static final char[]    COMPACT;
    private static final char[] UN_COMPACT;

    static
    {
        char[][] compactUncompact = compactUncompact();

           COMPACT = compactUncompact[ 0 ];
        UN_COMPACT = compactUncompact[ 1 ];
    }


    public  static final int    COUNT = 1950;
                                    // COMPACT[ COMPACT.length - 1 ] + 1;


    //--------------------------------------------------------------------
    private static char[][] compactUncompact()
    {
        LOG.debug("attempting to retrieve or computing");
        Stopwatch timer = new Stopwatch();

        char   compact[] = PersistentChars.retrieve(  compactFile);
        char unCompact[] = PersistentChars.retrieve(unCompactFile);
        if (compact != null && unCompact != null) {
            LOG.debug("retrieved, took " + timer);
            return new char[][]{compact, unCompact};
        }

        LOG.debug("computing");
        char[][] compactUncompact = computeCompactUncompact();
        PersistentChars.persist(compactUncompact[0],   compactFile);
        PersistentChars.persist(compactUncompact[1], unCompactFile);
        
        LOG.debug("done compiting, took " + timer);
        return compactUncompact;
    }


    //--------------------------------------------------------------------
    private static char[][] computeCompactUncompact()
    {
        char     usedStrengths[] = usedStrengths();

        char     nextCompact     = 0;
        char     compact[]       = new char[ ProbabilityEncoding.COUNT ];

        CharList unCompact       = new CharList();

        for (int i = 0; i < compact.length; i++) {
            boolean used = (Arrays.binarySearch(
                                     usedStrengths, (char) i) >= 0);

            if (used) {
                compact[ i ] = nextCompact++;
                unCompact.add((char) i);
            } else {
                compact[ i ] = Character.MAX_VALUE;
            }
        }

        return new char[][]{compact, unCompact.toArray()};
    }


    //--------------------------------------------------------------------
    private static char[] usedStrengths()
    {
        int[] rawProbCounts = rawProbCounts();

        LOG.debug("computing used strengths");
        CharList usedStrengths = new CharList();
        for (int strength = 0;
                 strength < ProbabilityEncoding.COUNT;
                 strength++) {
            if (rawProbCounts[(char) strength] != 0) {
                usedStrengths.add((char) strength);
            }
        }
        return usedStrengths.toArray();
    }

    private static int[] rawProbCounts()
    {
        LOG.debug("computing raw probability counts");

        final int counts[] = new int[ ProbabilityEncoding.COUNT ];
        RiverEvalLookup.traverse(
            new CanonRange[]{CanonRange.newFromCount(
                    0, RiverLookup.CANONS)},
            new RiverEvalLookup.VsRandomVisitor() {
                public void traverse(
                        long   canonIndex,
                        double strengthVsRandom,
                        byte   represents)
                {
                    char strAsChar =
                            ProbabilityEncoding.encodeWinProb(
                                    strengthVsRandom);
                    counts[ strAsChar ] += represents;
                }
            });
        return counts;
    }


    //--------------------------------------------------------------------
    public static char compact(double nonLossProbability)
    {
        return compact(ProbabilityEncoding.encodeWinProb(
                           nonLossProbability));
    }

    public static char compact(char nonLossProbability)
    {
        assert isValidRaw( nonLossProbability );
        return COMPACT[ nonLossProbability ];
    }

    public static char nonLossProbability(char compact)
    {
        return UN_COMPACT[compact];
    }

    public static boolean isValidRaw(char nonLossProbability)
    {
        return COMPACT[ nonLossProbability ] != Character.MAX_VALUE;
    }
}
