package ao.bucket.index;

import ao.bucket.index.iso_case.CommunityCase;
import ao.holdem.model.card.Card;
import ao.util.stats.Combiner;

import java.util.HashMap;
import java.util.Map;

/**
 * Hole Isomorphism
 */
public class HoleIso
{
    public static void main(String[] args)
    {
        Card cards[] = Card.values();


        Map<CommunityCase, int[]> flopCases =
                new HashMap<CommunityCase, int[]>();
        for (Card hole[] : new Combiner<Card>(Card.VALUES, 2))
        {
            swap(cards, hole[1].ordinal(), 51  );
            swap(cards, hole[0].ordinal(), 51-1);

//            HoleCase holeCase =
//                    HoleCase.newInstance(
//                            Hole.newInstance(hole[0], hole[1]));
            for (Card flop[] : new Combiner<Card>(cards, 50, 3))
            {
                swap(cards, flop[2].ordinal(), 51-2);
                swap(cards, flop[1].ordinal(), 51-3);
                swap(cards, flop[0].ordinal(), 51-4);

                CommunityCase flopCase =
                        new CommunityCase(flop, hole);

//                for (int turnIndex = 0; turnIndex <= 51-5; turnIndex++)
//                {
//                    Card turn = cards[ turnIndex ];
//
//                    for (int riverIndex  = 0;
//                             riverIndex <= 51-6;
//                             riverIndex++)
//                    {
//                        Card river = cards[ riverIndex ];
//
//
//                    }
//                }

                int[] count = flopCases.get( flopCase );
                if (count == null)
                {
                    count = new int[1];
                    flopCases.put(flopCase, count);
                }
                count[0]++;

                swap(cards, flop[0].ordinal(), 51-4);
                swap(cards, flop[1].ordinal(), 51-3);
                swap(cards, flop[2].ordinal(), 51-2);
            }

            swap(cards, hole[0].ordinal(), 50);
            swap(cards, hole[1].ordinal(), 51);
        }

        for (Map.Entry<CommunityCase, int[]> e : flopCases.entrySet())
        {
            System.out.println(e.getKey() + " :: " + e.getValue()[0]);
        }

//        Map<IsoHole, List<Hole>> holes =
//                new LinkedHashMap<IsoHole, List<Hole>>();
//        for (Card a : Card.VALUES)
//        {
//            for (Card b : Card.VALUES)
//            {
//                if (a == b) continue;
//                Hole hole = Hole.newInstance(a, b);
//
//                IsoHole isoHole = new IsoHole( hole );
//                List<Hole> h = holes.get(isoHole);
//                if (h == null)
//                {
//                    h = new ArrayList<Hole>();
//                    holes.put(isoHole, h);
//                }
//                h.add( hole );
//            }
//        }
//
//        for (Map.Entry<IsoHole, List<Hole>> e : holes.entrySet())
//        {
//            System.out.println(e.getKey() + " :: " + e.getValue());
//        }
//        System.out.println(holes.size());
    }

    private static void swap(Card cards[], int i, int j)
    {
        Card tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
    }

    public static boolean contains(
            Card bag[], Card... cards)
    {
        for (Card x : bag)
        {
            for (Card y : cards)
            {
                if (x == y) return true;
            }
        }
        return false;
    }
}
