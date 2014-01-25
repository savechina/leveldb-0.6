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
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.util.SliceInput;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.Slices;
import org.iq80.leveldb.util.SliceOutput;

/**
 * 二进制格式数据块尾
 * <p/>
 * 块尾格式： 1 byte 数据压缩类型ID + 4 byte 块数据CRC32校验和
 */
public class BlockTrailer {
    /**
     * 块尾编码长度 5 byte
     */
    public static final int ENCODED_LENGTH = 5;

    /**
     * 数据压缩类型
     */
    private final CompressionType compressionType;

    /**
     * CRC32 校验和
     */
    private final int crc32c;

    public BlockTrailer(CompressionType compressionType, int crc32c) {
        Preconditions.checkNotNull(compressionType, "compressionType is null");

        this.compressionType = compressionType;
        this.crc32c = crc32c;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public int getCrc32c() {
        return crc32c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlockTrailer that = (BlockTrailer) o;

        if (crc32c != that.crc32c) {
            return false;
        }
        if (compressionType != that.compressionType) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = compressionType.hashCode();
        result = 31 * result + crc32c;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BlockTrailer");
        sb.append("{compressionType=").append(compressionType);
        sb.append(", crc32c=0x").append(Integer.toHexString(crc32c));
        sb.append('}');
        return sb.toString();
    }

    /**
     * 从字节数据分片读取块尾信息
     *
     * @param slice 数据分片
     * @return 块尾
     */
    public static BlockTrailer readBlockTrailer(Slice slice) {
        //分片输入流
        SliceInput sliceInput = slice.input();

        //根据持久化ID，获取压缩类型
        CompressionType compressionType = CompressionType.getCompressionTypeByPersistentId(sliceInput.readUnsignedByte());

        //读取校验和
        int crc32c = sliceInput.readInt();

        return new BlockTrailer(compressionType, crc32c);
    }

    /**
     * 根据块尾信息，写入块尾信息到数据分片
     *
     * @param blockTrailer 块尾
     * @return 写入块尾后的数据分片
     */
    public static Slice writeBlockTrailer(BlockTrailer blockTrailer) {
        //分配数据分片
        Slice slice = Slices.allocate(ENCODED_LENGTH);

        writeBlockTrailer(blockTrailer, slice.output());
        return slice;
    }

    /**
     * 写入块尾信息到，数据分片输出流
     *
     * @param blockTrailer 块尾
     * @param sliceOutput  数据分片输出流
     */
    public static void writeBlockTrailer(BlockTrailer blockTrailer, SliceOutput sliceOutput) {
        sliceOutput.writeByte(blockTrailer.getCompressionType().persistentId());
        sliceOutput.writeInt(blockTrailer.getCrc32c());
    }
}
