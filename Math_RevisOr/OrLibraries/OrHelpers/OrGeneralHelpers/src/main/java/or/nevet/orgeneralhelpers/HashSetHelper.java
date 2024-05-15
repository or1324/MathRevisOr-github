package or.nevet.orgeneralhelpers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class HashSetHelper<T> {
    public T getRandomHashSetItem(HashSet<T> set) {
        Iterator<T> iterator = set.iterator();
        Random random = new Random();
        int r = random.nextInt(set.size());
        for (int i = 0; i < r; i++) {
            iterator.next();
        }
        return iterator.next();
    }
}
