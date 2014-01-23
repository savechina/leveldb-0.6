package org.iq80.leveldb.util;

import org.iq80.leveldb.impl.SeekingIterator;

import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * 抽象的数据查找迭代器
 *
 * @param <K> 数据KEY
 * @param <V> KEY对应的数值
 */
public abstract class AbstractSeekingIterator<K, V> implements SeekingIterator<K, V> {
    /**
     * 下一个实体元素
     */
    private Entry<K, V> nextElement;

    /**
     * 将查找游标移动到开始查找位置
     */
    @Override
    public final void seekToFirst() {
        nextElement = null;
        seekToFirstInternal();
    }

    /**
     * 将查找游标移动到大于等于给定 {@code targetKey} 最近的位置
     *
     * @param targetKey 目标值Key
     */
    @Override
    public final void seek(K targetKey) {
        nextElement = null;
        seekInternal(targetKey);
    }

    /**
     * 返回是否有下一元素
     *
     * @return true 还有下一个元素/false 已经到最后一个元素
     */
    @Override
    public final boolean hasNext() {
        if (nextElement == null) {
            nextElement = getNextElement();
        }
        return nextElement != null;
    }

    /**
     * 获取 下个元素实体 {@code Entry<K, V> }
     *
     * @return {@code Entry<K, V> } 下一个元素实体
     */
    @Override
    public final Entry<K, V> next() {
        if (nextElement == null) {
            nextElement = getNextElement();
            if (nextElement == null) {
                throw new NoSuchElementException();
            }
        }

        Entry<K, V> result = nextElement;
        nextElement = null;
        return result;
    }

    /**
     * 获取下一个元素实体，但是不改变迭代器状态
     *
     * @return {@code Entry<K, V> } 下一个元素实体
     */
    @Override
    public final Entry<K, V> peek() {
        if (nextElement == null) {
            nextElement = getNextElement();
            if (nextElement == null) {
                throw new NoSuchElementException();
            }
        }

        return nextElement;
    }


    @SuppressWarnings("not support remove operation")
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * 子类内部实现 移动游标到查找开始位置
     */
    protected abstract void seekToFirstInternal();

    /**
     * 子类内部实现 移动游标到给定{@code targetKey} 大于等于其最近的位置
     *
     * @param targetKey 查找的目标Key
     */
    protected abstract void seekInternal(K targetKey);

    /**
     * 获取迭代器下一个元素实体对象
     *
     * @return 下一个实体元素 {@code Entry<K, V> }
     */
    protected abstract Entry<K, V> getNextElement();
}
