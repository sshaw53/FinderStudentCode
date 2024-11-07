import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: SIERRA SHAW
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    public static final int RADIX = 256;
    // Picked a large prime number to avoid collisions while trying not to take up too much storage
    public static final int initialArrSize = 10937;
    public static int arrSize = initialArrSize;
    public static Pair[] keys = new Pair[initialArrSize];

    private class Pair {
        private String key;
        private String val;

        Pair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }

    public Finder() {}

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line;
        String key;
        String value;
        int sizeCounter = 0;

        // Reads through the csv file line by line
        while((line = br.readLine()) != null) {
            // Using splitLine to get the key and value from the row of data
            String[] splitLine = line.split(",");
            key = splitLine[keyCol];
            value = splitLine[valCol];
            int keyLen = key.length();

            // Increase counter that tracks how many items are in the array
            sizeCounter += 1;

            // If the array is 50% full, we need to rehash all of it to an array 2x larger
            if (sizeCounter > arrSize / 2) {
                arrSize *= 2;
                Pair[] tempKeys = new Pair[arrSize];
                rehash(tempKeys);
                keys = tempKeys;
            }

            // Either way, we add the new pair into the keys array
            int idx = hash(key, keyLen, arrSize, keys);
            keys[idx] = new Pair(key, value);
        }
        br.close();
    }

    public String query(String key){
        int idx = hash(key, key.length(), arrSize, null);
        // If it's not null, keep traversing to the next one to see when it actually matches the key
        while (keys[idx] != null) {
            if (keys[idx].key.equals(key)) {
                return keys[idx].val;
            }
            else {
                idx += 1;
            }
        }
        // Otherwise, we hit a null spot and return Invalid
        return INVALID;
    }

    public static int hash(String str, int len, int p, Pair[] array) {
        // Modifies a string to a unique number utilizing hash functions (Horner's method)
        int hashed = 0;
        for(int i = 0; i < len; i++) {
            hashed = (RADIX * hashed + str.charAt(i)) % p;
        }
        hashed %= p;

        // Keep moving to the next item in the array until you find a blank spot, but make sure to wrap if it overflows
        if (array != null) {
            while (array[hashed] != null) {
                hashed = (hashed + 1) % p;
            }
        }
        return hashed;
    }

    public static void rehash(Pair[] tempKeys) {
        // Calls the hash function and rehashes every item in the array to a new array
        for (Pair pair: keys) {
            // If not a null index in the array
            if (pair != null) {
                int idx = hash(pair.key, pair.key.length(), arrSize, tempKeys);
                tempKeys[idx] = pair;
            }
        }
    }
}