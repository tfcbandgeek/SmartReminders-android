package jgappsandgames.me.save.utility.pool;

import java.util.ArrayList;

public class SimplePool<T> {
    // Data
    private ArrayList<T> freeObjects;
    private PoolFactory<T> factory;
    private int maxSize;

    public SimplePool(PoolFactory<T> factory, int maxSize) {
        freeObjects = new ArrayList<>();
        this.factory = factory;
        this.maxSize = maxSize;
    }

    // Pool Methods
    public T getPoolObject() {
        if (freeObjects.size() < 1) return factory.createObject();
        return freeObjects.remove(0);
    }

    public void returnPoolObject(T poolObject) {
        if (freeObjects.size() < maxSize) freeObjects.add(poolObject);
    }

    // Internal Interfaces
    public interface PoolFactory<U> {
        U createObject();
    }
}