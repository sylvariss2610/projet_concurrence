package PageIndexer;

import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Mapping :
 */
class Index {

    private HashSet<URL> index;
    ReentrantReadWriteLock RRWL = new ReentrantReadWriteLock();
    private Lock readLock = RRWL.readLock();
    private Lock writeLock = RRWL.writeLock();

    Index() {
        index = new HashSet<>();
    }

    boolean verify(URL pageURL) {
        //System.out.println("Verifying if the page " + pageURL + "has already been caught.");
        this.readLock.lock();
        try {
            if(this.index.contains(pageURL)) {
                return true;
            } else {
                return false;
            }
        } finally {
            this.readLock.unlock();
        }
    }

    void write(URL pageURL) {
        if (!verify(pageURL)) {
            //System.out.println("The page at " + pageURL + " will be verified/scanned.");
            this.writeLock.lock();
            try {
                this.index.add(pageURL);
            } finally {
                this.writeLock.unlock();
            }
        }
    }
}
