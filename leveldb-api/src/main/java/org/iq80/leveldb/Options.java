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
 * DB 参数选项配置对象
 */
public class Options {

    /**
     * 配置 当数据库不存在时，是否进行创建
     */
    private boolean createIfMissing = true;

    /**
     * 配置 当数据库已经存在时，是否报错
     */
    private boolean errorIfExists;

    /**
     * 写缓存大小 4096 K
     */
    private int writeBufferSize = 4 << 20;

    /**
     * 最大打开文件数量
     */
    private int maxOpenFiles = 1000;

    private int blockRestartInterval = 16;

    /**
     * 块大小 4K
     */
    private int blockSize = 4 * 1024;

    /**
     * 数据压缩类型 默认是SNAPPY
     */
    private CompressionType compressionType = CompressionType.SNAPPY;

    /**
     * 是否验证校验和 默认为true
     */
    private boolean verifyChecksums = true;

    /**
     * 是否是强校验
     */
    private boolean paranoidChecks = false;

    /**
     * 数据库比较合并器
     */
    private DBComparator comparator;

    /**
     * 日志记录器
     */
    private Logger logger = null;

    /**
     * 缓存大小
     */
    private long cacheSize;

    /**
     * 校验值不能为NULL
     *
     * @param value 数据值
     * @param name  参数名称
     */
    static void checkArgNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException("The " + name + " argument cannot be null");
        }
    }

    public boolean createIfMissing() {
        return createIfMissing;
    }

    public Options createIfMissing(boolean createIfMissing) {
        this.createIfMissing = createIfMissing;
        return this;
    }

    public boolean errorIfExists() {
        return errorIfExists;
    }

    public Options errorIfExists(boolean errorIfExists) {
        this.errorIfExists = errorIfExists;
        return this;
    }

    public int writeBufferSize() {
        return writeBufferSize;
    }

    public Options writeBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
        return this;
    }

    public int maxOpenFiles() {
        return maxOpenFiles;
    }

    public Options maxOpenFiles(int maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
        return this;
    }

    public int blockRestartInterval() {
        return blockRestartInterval;
    }

    public Options blockRestartInterval(int blockRestartInterval) {
        this.blockRestartInterval = blockRestartInterval;
        return this;
    }

    public int blockSize() {
        return blockSize;
    }

    public Options blockSize(int blockSize) {
        this.blockSize = blockSize;
        return this;
    }

    public CompressionType compressionType() {
        return compressionType;
    }

    public Options compressionType(CompressionType compressionType) {
        checkArgNotNull(compressionType, "compressionType");
        this.compressionType = compressionType;
        return this;
    }

    public boolean verifyChecksums() {
        return verifyChecksums;
    }

    public Options verifyChecksums(boolean verifyChecksums) {
        this.verifyChecksums = verifyChecksums;
        return this;
    }


    public long cacheSize() {
        return cacheSize;
    }

    public Options cacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public DBComparator comparator() {
        return comparator;
    }

    public Options comparator(DBComparator comparator) {
        this.comparator = comparator;
        return this;
    }

    public Logger logger() {
        return logger;
    }

    public Options logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public boolean paranoidChecks() {
        return paranoidChecks;
    }

    public Options paranoidChecks(boolean paranoidChecks) {
        this.paranoidChecks = paranoidChecks;
        return this;
    }
}
