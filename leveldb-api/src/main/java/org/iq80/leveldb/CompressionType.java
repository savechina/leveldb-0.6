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

/**
 * 数据库压缩类型
 */
public enum CompressionType {
    /**
     * 不进行压缩处理
     */
    NONE(0x00),

    /**
     * 使用SNAPPY算法进行压缩处理
     */
    SNAPPY(0x01);

    /**
     * 返回给定持久化ID 的压缩类型
     *
     * @param persistentId 压缩类型ID
     * @return 压缩类型 CompressionType
     */
    public static CompressionType getCompressionTypeByPersistentId(int persistentId) {
        for (CompressionType compressionType : CompressionType.values()) {
            if (compressionType.persistentId == persistentId) {
                return compressionType;
            }
        }
        throw new IllegalArgumentException("Unknown persistentId " + persistentId);
    }

    /**
     * 压缩类型值
     */
    private final int persistentId;

    CompressionType(int persistentId) {
        this.persistentId = persistentId;
    }

    public int persistentId() {
        return persistentId;
    }
}
