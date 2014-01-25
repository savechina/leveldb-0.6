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
package org.iq80.leveldb.table;

import com.google.common.base.Preconditions;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.SliceInput;
import org.iq80.leveldb.util.Slices;
import org.iq80.leveldb.util.SliceOutput;

import static org.iq80.leveldb.table.BlockHandle.readBlockHandle;
import static org.iq80.leveldb.table.BlockHandle.writeBlockHandleTo;
import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;

/**
 * 表尾
 */
public class Footer
{
    /**
     * 编码长度
     */
    public static final int ENCODED_LENGTH = (BlockHandle.MAX_ENCODED_LENGTH * 2) + SIZE_OF_LONG;

    /**
     * 元数据索引块处理器
     */
    private final BlockHandle metaindexBlockHandle;

    /**
     * 索引块处理器
     */
    private final BlockHandle indexBlockHandle;

    Footer(BlockHandle metaindexBlockHandle, BlockHandle indexBlockHandle)
    {
        this.metaindexBlockHandle = metaindexBlockHandle;
        this.indexBlockHandle = indexBlockHandle;
    }

    public BlockHandle getMetaindexBlockHandle()
    {
        return metaindexBlockHandle;
    }

    public BlockHandle getIndexBlockHandle()
    {
        return indexBlockHandle;
    }

    /**
     * 读取表尾
     * @param slice 数据片
     * @return  表尾
     */
    public static Footer readFooter(Slice slice)
    {
        Preconditions.checkNotNull(slice, "slice is null");
        Preconditions.checkArgument(slice.length() == ENCODED_LENGTH, "Expected slice.size to be %s but was %s", ENCODED_LENGTH, slice.length());

        SliceInput sliceInput = slice.input();

        // read metaindex and index handles
        BlockHandle metaindexBlockHandle = readBlockHandle(sliceInput);
        BlockHandle indexBlockHandle = readBlockHandle(sliceInput);

        // skip padding
        sliceInput.setPosition(ENCODED_LENGTH - SIZE_OF_LONG);

        // verify magic number
        long magicNumber = sliceInput.readUnsignedInt() | (sliceInput.readUnsignedInt() << 32);
        Preconditions.checkArgument(magicNumber == TableBuilder.TABLE_MAGIC_NUMBER, "File is not a table (bad magic number)");

        return new Footer(metaindexBlockHandle, indexBlockHandle);
    }

    /**
     * 写入表尾 返回数据分片
     * @param Footer 表尾
     * @return 写入表尾的数据分片
     */
    public static Slice writeFooter(Footer Footer)
    {
        Slice slice = Slices.allocate(ENCODED_LENGTH);
        writeFooter(Footer, slice.output());
        return slice;
    }

    /**
     * 将表尾写入给定的数据分片输出流 。
     * 表尾编码
     *
     * @param footer      表尾
     * @param sliceOutput 数据分片
     */
    public static void writeFooter(Footer footer, SliceOutput sliceOutput)
    {
        // remember the starting write index so we can calculate the padding
        int startingWriteIndex = sliceOutput.size();

        // write metaindex and index handles
        writeBlockHandleTo(footer.getMetaindexBlockHandle(), sliceOutput);
        writeBlockHandleTo(footer.getIndexBlockHandle(), sliceOutput);

        // write padding
        sliceOutput.writeZero(ENCODED_LENGTH - SIZE_OF_LONG - (sliceOutput.size() - startingWriteIndex));

        // write magic number as two (little endian) integers
        sliceOutput.writeInt((int) TableBuilder.TABLE_MAGIC_NUMBER);
        sliceOutput.writeInt((int) (TableBuilder.TABLE_MAGIC_NUMBER >>> 32));
    }

}
