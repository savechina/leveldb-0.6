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

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 *         <p/>
 *         DBFactory 数据库工厂类
 */
public interface DBFactory {

    /**
     * 给定数据文件创建数据库对象
     *
     * @param path    文件
     * @param options 数据库参数
     * @return 数据库对象 DB
     * @throws IOException 打开数据库IO异常
     */
    public DB open(File path, Options options) throws IOException;

    /**
     * 删除给定目录文件的数据库
     *
     * @param path    数据库文件
     * @param options 数据库参数
     * @throws IOException 操作IO异常
     */
    public void destroy(File path, Options options) throws IOException;

    /**
     * 修复给定数据文件的数据库
     *
     * @param path    数据库文件
     * @param options 数据库参数
     * @throws IOException 修复操作IO异常
     */
    public void repair(File path, Options options) throws IOException;

}
