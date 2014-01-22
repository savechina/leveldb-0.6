package org.iq80.leveldb.util;

import java.util.Comparator;

/**
 * Slice 比较器
 */
public final class SliceComparator implements Comparator<Slice>
{
    public static final SliceComparator SLICE_COMPARATOR = new SliceComparator();

    @Override
    public int compare(Slice sliceA, Slice sliceB)
    {
        return sliceA.compareTo(sliceB);
    }
}
