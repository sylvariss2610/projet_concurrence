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
    ReentrantReadWriteLock ReWrLock = new ReentrantReadWriteLock();
    private Lock readLock = ReWrLock.readLock();
    private Lock writeLock = ReWrLock.writeLock();

    Index() {
        index = new HashSet<>();
    }

    boolean Verification(URL pageURL) {
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

    void Ecriture(URL pageURL) {
        if (!Verification(pageURL)) {
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
