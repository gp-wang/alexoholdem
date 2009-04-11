package ao.bucket.index.test;

import ao.util.math.Calc;

import java.util.BitSet;

/**
 * Date: Aug 21, 2008
 * Time: 7:43:32 PM
 */
public class Gapper
{
    //--------------------------------------------------------------------
    private final BitSet indexes;
    private final BitSet indexesB;
    private       long   count    = 0;


    //--------------------------------------------------------------------
    public Gapper() {  this(0);  }

    public Gapper(long size) {
        indexes  = new BitSet((int)
                    Math.min(size, Integer.MAX_VALUE));
        indexesB = new BitSet();
    }

    //--------------------------------------------------------------------
    // takes an unsigned int
    public void set(long index)
    {
        validateIndex(index);
        count++;

        if (index > Integer.MAX_VALUE)
        {
            indexesB.set( Calc.signedPart(index) );
        }
        else
        {
            indexes.set( (int) index );
        }
    }


    public boolean get(long index)
    {
        validateIndex(index);

        if (index > Integer.MAX_VALUE)
        {
            return indexesB.get( Calc.signedPart(index) );
        }
        else
        {
            return indexes.get( (int) index );
        }
    }

    private void validateIndex(long index)
    {
        assert index >= 0 : "must be non-negatve";
        assert index < (1L << 32) : "must be 32 bit unsigned integer";
    }


    //--------------------------------------------------------------------
    public void clear()
    {
        count = 0;
        indexes.clear();
        indexesB.clear();
    }


    //--------------------------------------------------------------------
    public boolean continuous()
    {
        return  indexes.nextClearBit(0) ==  indexes.length() &&
               indexesB.nextClearBit(0) == indexesB.length();
    }

    public long length()
    {
        return Calc.unsigned(
                indexes .length() +
                indexesB.length());
    }

    public double fillRatio()
    {
        return (double) count / length();
    }


    //--------------------------------------------------------------------
    public boolean displayStatus()
    {
        boolean isContinuous = continuous();
        if (isContinuous)
        {
            System.out.println(
                "Compressed " + count + " into " +
                    length() + " bytes.");
        }
        else
        {
            long gap = indexes.nextClearBit(0);
            if (gap == indexes.length())
            {
                gap += indexesB.nextClearBit(0);
            }

            System.out.println(
                    "ERROR: gap at " + gap +
                        " of " + length() + " indexes.");
        }
        return isContinuous;
    }
}