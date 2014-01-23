package org.iq80.leveldb.util;

import com.google.common.collect.Maps;
import org.iq80.leveldb.impl.InternalKey;

import java.util.Map.Entry;

/**
 * 一个内部的表遍历迭代器
 */
public class InternalTableIterator extends AbstractSeekingIterator<InternalKey, Slice> implements InternalIterator {
    /**
     * 表遍历迭代器
     */
    final TableIterator tableIterator;

    public InternalTableIterator(TableIterator tableIterator) {
        this.tableIterator = tableIterator;
    }

    @Override
    protected void seekToFirstInternal() {
        tableIterator.seekToFirst();
    }

    @Override
    public void seekInternal(InternalKey targetKey) {
        tableIterator.seek(targetKey.encode());
    }

    @Override
    protected Entry<InternalKey, Slice> getNextElement() {
        if (tableIterator.hasNext()) {
            Entry<Slice, Slice> next = tableIterator.next();
            return Maps.immutableEntry(new InternalKey(next.getKey()), next.getValue());
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InternalTableIterator");
        sb.append("{fromIterator=").append(tableIterator);
        sb.append('}');
        return sb.toString();
    }
}
