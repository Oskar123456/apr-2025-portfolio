package apr.datastructures;

/**
 * Hashmap
 */
@SuppressWarnings("unchecked")
public class Hashmap<K, V> {

    static final int szInit = 8;

    Entry<K, V>[] entries;
    int sz, load, cap;
    float lf = 0.75f;

    public Hashmap() {
        cap = szInit;
        entries = new Entry[szInit];
    }

    public void put(K key, V value) {
        float loadfactor = (float) sz / cap;
        if (loadfactor > lf) {
            sz = load = 0;
            cap *= 2;
            var entriesOld = entries;
            entries = new Entry[cap];
            for (var ent : entriesOld) {
                var e = ent;
                while (e != null) {
                    put(e.key, e.value);
                    e = e.next;
                }
            }
        }

        sz++;
        int hash = Math.abs(key.hashCode());
        int idx = hash % cap;

        if (entries[idx] == null) {
            entries[idx] = new Entry<>(key, value);
            load++;
        } else {
            var ent = entries[idx];
            while (!ent.key.equals(key) && ent.next != null) {
                ent = ent.next;
            }
            if (ent.key.equals(key)) {
                ent.value = value;
            } else {
                ent.next = new Entry<>(key, value);
            }
        }
    }

    public V get(K key) {
        int hash = Math.abs(key.hashCode());
        int idx = hash % cap;

        var ent = entries[idx];
        while (ent != null && !ent.key.equals(key)) {
            ent = ent.next;
        }

        return (ent != null) ? ent.value : null;
    }

    public long size() {
        return sz;
    }

    public String toString() {
        String str = String.format("Hashmap[sz=%d,load=%d,cap=%d]:: Entries:", sz, load, cap);

        for (var ent : entries) {
            int bucketSize = 0;
            String bucketStr = "";

            var e = ent;
            while (e != null) {
                bucketSize++;
                bucketStr += " -> " + e.toString();
                e = e.next;
            }

            str += String.format("%n\t[%d]%s", bucketSize, bucketStr);
        }

        return str;
    }

    public static class Entry<Kk, Vv> {
        public Kk key;
        public Vv value;

        public Entry<Kk, Vv> next;

        public String toString() {
            return String.format("Entry[key:%s, value:%s]", key.toString(), value.toString());
        }

        public Entry(Kk key, Vv value) {
            this.key = key;
            this.value = value;
        }
    }

}
