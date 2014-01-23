package org.iq80.leveldb.util;

import org.iq80.leveldb.table.Block;
import org.iq80.leveldb.table.BlockIterator;
import org.iq80.leveldb.table.Table;

import java.util.Map.Entry;

/**
 * 表数据迭代器 遍历表内数据块 继承
 *
 * @see org.iq80.leveldb.impl.SeekingIterator
 * @see org.iq80.leveldb.util.AbstractSeekingIterator
 */
public final class TableIterator extends AbstractSeekingIterator<Slice, Slice> {
    /**
     * 表
     */
    private final Table table;

    /**
     * 数据块迭代器
     */
    private final BlockIterator blockIterator;

    /**
     * 当前块迭代器
     */
    private BlockIterator current;

    public TableIterator(Table table, BlockIterator blockIterator) {
        this.table = table;
        this.blockIterator = blockIterator;
        current = null;
    }

    /**
     * seek to first  offset is set fist index
     */
    @Override
    protected void seekToFirstInternal() {
        // reset index to before first and clear the data iterator
        blockIterator.seekToFirst();
        current = null;
    }

    @Override
    protected void seekInternal(Slice targetKey) {
        // seek the index to the block containing the key
        blockIterator.seek(targetKey);

        // if indexIterator does not have a next, it mean the key does not exist in this iterator
        if (blockIterator.hasNext()) {
            // seek the current iterator to the key
            current = getNextBlock();
            current.seek(targetKey);
        } else {
            current = null;
        }
    }

    /**
     * 获取表的 下一个元素实体
     *
     * @return 表元素实体
     */
    @Override
    protected Entry<Slice, Slice> getNextElement() {
        // note: it must be here & not where 'current' is assigned,
        // because otherwise we'll have called inputs.next() before throwing
        // the first NPE, and the next time around we'll call inputs.next()
        // again, incorrectly moving beyond the error.
        boolean currentHasNext = false;
        while (true) {
            if (current != null) {
                currentHasNext = current.hasNext();
            }
            if (!(currentHasNext)) {
                if (blockIterator.hasNext()) {
                    current = getNextBlock();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if (currentHasNext) {
            return current.next();
        } else {
            // set current to empty iterator to avoid extra calls to user iterators
            current = null;
            return null;
        }
    }

    /**
     * 获取下一个块迭代器
     * {@code}
     *
     * @return 下一块的迭代器
     */
    private BlockIterator getNextBlock() {
        Slice blockHandle = blockIterator.next().getValue();
        Block dataBlock = table.openBlock(blockHandle);
        return dataBlock.iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ConcatenatingIterator");
        sb.append("{blockIterator=").append(blockIterator);
        sb.append(", current=").append(current);
        sb.append('}');
        return sb.toString();
    }
}
