package ao.bucket.abstraction.access.tree.list;

import ao.bucket.abstraction.access.tree.BucketList;
import ao.util.data.LongBitSet;
import ao.util.io.Dir;

import java.io.File;

/**
 * Date: Jan 28, 2009
 * Time: 1:55:44 PM
 */
public class ThirdBucketList implements BucketList
{
    //--------------------------------------------------------------------
    public static void main(String[] args)
    {
        int        size = 1000*1000;
        BucketList bl   = new ThirdBucketList(
                new File(Dir.get("test"), "tb.test3.byte"), size);

        for (int i = 0; i < size; i++)
        {
//            if (bl.isEmpty(i)) {
                bl.set(i, (byte) (i % 8));
//                bl.flush(i, (char) 1);
//            }

            if (bl.get(i) != (i % 8)) {
                System.out.println("ERORR at " + i);
            }
        }
    }


    //--------------------------------------------------------------------
    private static final String A_FILE = "a";
    private static final String B_FILE = "b";
    private static final String C_FILE = "c";

    private static final int    A_BIT  =          1;
    private static final int    B_BIT  = A_BIT << 1;
    private static final int    C_BIT  = B_BIT << 1;


    //--------------------------------------------------------------------
    private final File       DIR;
    private final LongBitSet A, B, C;


    //--------------------------------------------------------------------
    public ThirdBucketList(File dir, long size)
    {
        DIR = dir;
        A   = retrieveOrCreate(new File(dir, A_FILE), size);
        B   = retrieveOrCreate(new File(dir, B_FILE), size);
        C   = retrieveOrCreate(new File(dir, C_FILE), size);
    }

    private static LongBitSet retrieveOrCreate(File from, long size)
    {
        LongBitSet bits = LongBitSet.retrieve(from);
        return bits == null
               ? new LongBitSet(size)
               : bits;
    }


    //--------------------------------------------------------------------
    public byte maxBuckets() {
        return 1 << 3;
    }


    //--------------------------------------------------------------------
    public void set(long index, byte bucket)
    {
        assert 0 <= bucket && bucket <= 7;

        boolean a = ((bucket & A_BIT) != 0);
        boolean b = ((bucket & B_BIT) != 0);
        boolean c = ((bucket & C_BIT) != 0);

        A.set(index, a);
        B.set(index, b);
        C.set(index, c);
    }


    //--------------------------------------------------------------------
    public byte get(long index)
    {
        boolean a = A.get(index);
        boolean b = B.get(index);
        boolean c = C.get(index);

        int    bucket  = 0;
        if (a) bucket |= A_BIT;
        if (b) bucket |= B_BIT;
        if (c) bucket |= C_BIT;

        return (byte) bucket;
    }


    //--------------------------------------------------------------------
    public void flush()
    {
        LongBitSet.persist(A, new File(DIR, A_FILE));
        LongBitSet.persist(B, new File(DIR, B_FILE));
        LongBitSet.persist(C, new File(DIR, C_FILE));
    }
}
