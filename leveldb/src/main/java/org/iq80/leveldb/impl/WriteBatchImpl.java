/**
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iq80.leveldb.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.Slices;

import java.util.List;
import java.util.Map.Entry;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 批量写入实现
 */
public class WriteBatchImpl implements WriteBatch
{
    /**
     * 批量写入实体
     */
    private List<Entry<Slice, Slice>> batch = newArrayList();
    /**
     *批量写入数据总大小
     */
    private int approximateSize;

    public int getApproximateSize()
    {
        return approximateSize;
    }

    /**
     * 批量更新数据大小
     * @return
     */
    public int size()
    {
        return batch.size();
    }

    /**
     *  更新数据
     * @param key   数据Key
     * @param value 数据Value
     * @return
     */
    @Override
    public WriteBatchImpl put(byte[] key, byte[] value)
    {
        Preconditions.checkNotNull(key, "key is null");
        Preconditions.checkNotNull(value, "value is null");
        batch.add(Maps.immutableEntry(Slices.wrappedBuffer(key), Slices.wrappedBuffer(value)));
        approximateSize += 12 + key.length + value.length;
        return this;
    }

    /**
     * 写入给定的KEY/VALUE 数据
     * @param key 数据key
     * @param value 数据value
     * @return 批量操作
     */
    public WriteBatchImpl put(Slice key, Slice value)
    {
        Preconditions.checkNotNull(key, "key is null");
        Preconditions.checkNotNull(value, "value is null");
        batch.add(Maps.immutableEntry(key, value));
        approximateSize += 12 + key.length() + value.length();
        return this;
    }

    /**
     * 删除给定的KEY
     * @param key 数据Key {@code byte[]}
     * @return 批量操作
     */
    @Override
    public WriteBatchImpl delete(byte[] key)
    {
        Preconditions.checkNotNull(key, "key is null");
        batch.add(Maps.immutableEntry(Slices.wrappedBuffer(key), (Slice) null));
        approximateSize += 6 + key.length;
        return this;
    }

    /**
     * 删除给定的key {@code Slice}
     * @param key
     * @return
     */
    public WriteBatchImpl delete(Slice key)
    {
        Preconditions.checkNotNull(key, "key is null");
        batch.add(Maps.immutableEntry(key, (Slice) null));
        approximateSize += 6 + key.length();
        return this;
    }

    @Override
    public void close()
    {
    }

    public void forEach(Handler handler)
    {
        for (Entry<Slice, Slice> entry : batch) {
            Slice key = entry.getKey();
            Slice value = entry.getValue();
            if (value != null) {
                handler.put(key, value);
            }
            else {
                handler.delete(key);
            }
        }
    }

    public static interface Handler
    {
        void put(Slice key, Slice value);

        void delete(Slice key);
    }

}
