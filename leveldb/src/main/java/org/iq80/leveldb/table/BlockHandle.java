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

import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.SliceInput;
import org.iq80.leveldb.util.Slices;
import org.iq80.leveldb.util.VariableLengthQuantity;
import org.iq80.leveldb.util.SliceOutput;

/**
 * 二进制数据块处理器
 */
public class BlockHandle
{
    /**
     * 最大的编码长度
     */
    public static final int MAX_ENCODED_LENGTH = 10 + 10;

    /**
     * 块偏移量
     */
    private final long offset;

    /**
     * 数据大小
     */
    private final int dataSize;

    BlockHandle(long offset, int dataSize)
    {
        this.offset = offset;
        this.dataSize = dataSize;
    }

    /**
     * 返回当前块偏移量
     * @return 块偏移量
     */
    public long getOffset()
    {
        return offset;
    }

    /**
     * 返回块数据大小
     * @return 块数据大小
     */
    public int getDataSize()
    {
        return dataSize;
    }

    /**
     * 返回全块的大小
     *
     * 全块数据大小=块数据大小+块尾编码长度
     * @return 全块的大小
     */
    public int getFullBlockSize() {
        return dataSize + BlockTrailer.ENCODED_LENGTH;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockHandle that = (BlockHandle) o;

        if (offset != that.offset) {
            return false;
        }
        if (dataSize != that.dataSize) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int) (offset ^ (offset >>> 32));
        result = 31 * result + (int) (dataSize ^ (dataSize >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("BlockHandle");
        sb.append("{offset=").append(offset);
        sb.append(", dataSize=").append(dataSize);
        sb.append('}');
        return sb.toString();
    }

    /**
     * 通过给定分片输入流{@code SliceInput}，返回读取数据块处理器
     *
     * @param sliceInput 字节分片输入流
     * @return 读取数据块处理器 {@code BlockHandle}
     */
    public static BlockHandle readBlockHandle(SliceInput sliceInput) {
        long offset = VariableLengthQuantity.readVariableLengthLong(sliceInput); //块偏移量
        long size = VariableLengthQuantity.readVariableLengthLong(sliceInput);   //块数据大小

        //最大支持的块大小为 Integer.MAX_VALUE
        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Blocks can not be larger than Integer.MAX_VALUE");
        }

        return new BlockHandle(offset, (int) size);
    }

    /**
     * 通过块处理器，初始化默认分配块内存，返回块数据分片
     *
     * @param blockHandle 块处理器
     * @return 块数据分片
     */
    public static Slice writeBlockHandle(BlockHandle blockHandle) {
        //默认分配块内存大小
        Slice slice = Slices.allocate(MAX_ENCODED_LENGTH);
        SliceOutput sliceOutput = slice.output();
        writeBlockHandleTo(blockHandle, sliceOutput);
        return slice.slice();
    }

    /**
     * 给定块处理器与数据分片输出流，VLQ编码写入块处理器
     *
     * @param blockHandle 块处理器
     * @param sliceOutput 分片输出流
     */
    public static void writeBlockHandleTo(BlockHandle blockHandle, SliceOutput sliceOutput) {
        VariableLengthQuantity.writeVariableLengthLong(blockHandle.offset, sliceOutput);
        VariableLengthQuantity.writeVariableLengthLong(blockHandle.dataSize, sliceOutput);
    }
}
