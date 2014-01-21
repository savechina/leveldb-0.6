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
package org.iq80.leveldb;

import java.io.Closeable;
import java.util.Map;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 *         <p/>
 *         数据库对象接口，数据库是一个迭代器，一个Key/Value 的Map
 */
public interface DB extends Iterable<Map.Entry<byte[], byte[]>>, Closeable {

    /**
     * 返回给定KEY的数值 byte[]
     *
     * @param key 数据Key
     * @return 数值key 对应的Value byte[]
     * @throws DBException 数据库异常
     */
    public byte[] get(byte[] key) throws DBException;

    /**
     * 返回给定KEY的数值 byte[] ,读取参数
     *
     * @param key     数据Key
     * @param options 读取参数
     * @return 数值key 对应的Value byte[]
     * @throws DBException 数据库读取异常
     */
    public byte[] get(byte[] key, ReadOptions options) throws DBException;

    /**
     * 返回数据库迭代器
     *
     * @return DBIterator
     */
    public DBIterator iterator();

    /**
     * 返回 给定读取参数 的数据库迭代器
     *
     * @param options 数据库读取参数
     * @return DBIterator
     */
    public DBIterator iterator(ReadOptions options);

    /**
     * 更新数据库 给定Key的 值Value
     *
     * @param key   数据Key
     * @param value Key 更新的Value
     * @throws DBException 更新操作数据库异常
     */
    public void put(byte[] key, byte[] value) throws DBException;

    /**
     * 删除给定数据库Key 的值
     *
     * @param key 数据库Key
     * @throws DBException 数据库删除操作异常
     */
    public void delete(byte[] key) throws DBException;

    /**
     * 批量写入数据到数据库
     *
     * @param updates 数据批量写对象
     * @throws DBException 数据库操作异常
     */
    public void write(WriteBatch updates) throws DBException;

    /**
     * 创建数据库批量写操作对象
     *
     * @return WriteBatch
     */
    public WriteBatch createWriteBatch();

    /**
     * @return null if options.isSnapshot()==false otherwise returns a snapshot
     * of the DB after this operation.
     */
    public Snapshot put(byte[] key, byte[] value, WriteOptions options) throws DBException;

    /**
     * @return null if options.isSnapshot()==false otherwise returns a snapshot
     * of the DB after this operation.
     */
    public Snapshot delete(byte[] key, WriteOptions options) throws DBException;

    /**
     * @return null if options.isSnapshot()==false otherwise returns a snapshot
     * of the DB after this operation.
     */
    public Snapshot write(WriteBatch updates, WriteOptions options) throws DBException;

    /**
     * 获取数据库快照
     *
     * @return Snapshot
     */
    public Snapshot getSnapshot();

    public long[] getApproximateSizes(Range... ranges);

    public String getProperty(String name);

    /**
     * Suspends any background compaction threads.  This methods
     * returns once the background compactions are suspended.
     */
    public void suspendCompactions() throws InterruptedException;

    /**
     * Resumes the background compaction threads.
     */
    public void resumeCompactions();

    /**
     * Force a compaction of the specified key range.
     *
     * @param begin if null then compaction start from the first key
     * @param end   if null then compaction ends at the last key
     * @throws DBException
     */
    public void compactRange(byte[] begin, byte[] end) throws DBException;
}
