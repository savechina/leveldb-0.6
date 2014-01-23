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
package org.iq80.leveldb.util;

import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * 整型向量 动态的整型数组
 * <p/>
 * 不是线程安全的数据类型
 */
public class IntVector {
    /**
     * 大小
     */
    private int size;

    /**
     * 原始数据数组
     */
    private int[] values;

    public IntVector(int initialCapacity) {
        this.values = new int[initialCapacity];
    }

    /**
     * 返回整形向量数值数量
     *
     * @return 数量
     */
    public int size() {
        return size;
    }

    /**
     * 清除向量内容
     */
    public void clear() {
        size = 0;
    }

    /**
     * 添加 整型数值到 {@code IntVector}
     *
     * @param value int 数值
     */
    public void add(int value) {
        Preconditions.checkArgument(size + 1 >= 0, "Invalid minLength: %s", size + 1);

        ensureCapacity(size + 1);

        values[size++] = value;
    }

    /**
     * 数组容量扩充
     *
     * @param minCapacity 最小容量
     */
    private void ensureCapacity(int minCapacity) {
        if (values.length >= minCapacity) {
            return;
        }

        //当数组容量不足时，进行数组容量扩充
        int newLength = values.length;
        if (newLength == 0) {
            newLength = 1;
        } else {
            newLength <<= 1;

        }
        values = Arrays.copyOf(values, newLength);
    }

    /**
     * 以整型数组返回向量数值
     *
     * @return 整型数组 向量原始数据
     */
    public int[] values() {
        return Arrays.copyOf(values, size);
    }

    /**
     * 将向量里的数据写入给定的分片流里
     *
     * @param sliceOutput 分片流
     */
    public void write(SliceOutput sliceOutput) {
        for (int index = 0; index < size; index++) {
            sliceOutput.writeInt(values[index]);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IntVector");
        sb.append("{size=").append(size);
        sb.append(", values=").append(Arrays.toString(values));
        sb.append('}');
        return sb.toString();
    }
}
