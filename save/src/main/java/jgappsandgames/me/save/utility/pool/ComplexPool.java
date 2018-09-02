package jgappsandgames.me.save.utility.pool;

import java.util.ArrayList;

public class ComplexPool<T extends ComplexPool.PoolObject> {
    // Data
    private ArrayList<T> freeObjects;
    private PoolFactory<T> factory;

    // Sizing Data
    private int minSize;
    private int maxSize;
    private int currentSize;

    private int increment;
    private int decrement;

    private long nextID;

    public ComplexPool(PoolFactory<T> factory, int startSize, int minSize, int maxSize, int increment, int decrement) {
        freeObjects = new ArrayList<>(startSize);
        this.factory = factory;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.increment = increment;
        this.decrement = decrement;
        this.nextID = 0L;

        increment(startSize);
    }

    // Pool Methods
    public void increment() {
        currentSize += increment;

        if (currentSize > maxSize) currentSize = maxSize;

        for (int i = 0; i < increment; i++) {
            freeObjects.add(factory.createObject(nextID++));
        }
    }

    public void decrement() {
        currentSize -= decrement;

        if (currentSize < minSize) currentSize = minSize;

        while (freeObjects.size() > currentSize) {
            freeObjects.remove(0);
        }
    }

    private void increment(int amount) {
        currentSize += amount;

        if (currentSize > maxSize) currentSize = maxSize;

        for (int i = 0; i < amount; i++) {
            freeObjects.add(factory.createObject(nextID++));
        }
    }

    private void decrement(int amount) {
        currentSize -= amount;

        if (currentSize < minSize) currentSize = minSize;

        while (freeObjects.size() > currentSize) {
            freeObjects.remove(0);
        }
    }

    public void ensureSize(int size) {
        if (currentSize < size) currentSize = size;
    }

    public void ensureAvailable(int available) {
        if (available > currentSize) currentSize = available;

        while (freeObjects.size() < available) {
            freeObjects.add(factory.createObject(nextID++));
        }
    }

    // Pool Information
    public void printInformation() {
        System.out.println("Pool Size (Min,Cur,Max)" + String.valueOf(minSize) + ", " + String.valueOf(currentSize) + ", " + String.valueOf(maxSize));
        System.out.println("Pool (Inc, Dec)" + String.valueOf(increment) + ", " + String.valueOf(decrement));
        System.out.println("Pool Free Size" + String.valueOf(freeObjects.size()));
    }

    // Object Methods
    public T getPoolObject() {
        if (freeObjects.size() < 1) increment();

        return freeObjects.remove(0);
    }

    public void returnPoolObject(T poolObject) {
        poolObject.destroy();

        if (freeObjects.size() < currentSize) freeObjects.add(poolObject);
    }

    // Internal Interfaces
    public interface PoolFactory<U> {
        U createObject(long id);
    }

    public interface PoolObject {
        long getID();
        void destroy();
    }
}