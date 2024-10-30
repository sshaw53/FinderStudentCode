import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: [YOUR NAME HERE]
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    public static int RADIX = 256;
    public static int first_p = 1847;
    public static int second_p = 61;
    public static ArrayList[] keys = new ArrayList[first_p];

    public Finder() {}

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        String line;
//        ArrayList[] keys = new ArrayList[first_p];
        String key;
        String value;
        int firstHash;
        int secondHash;
        while((line = br.readLine()) != null) {
            String[] splitLine = line.split(",");
            key = splitLine[keyCol];
            value = splitLine[valCol];

            firstHash = hash(key, key.length(), first_p);
            secondHash = hash(key, key.length(), second_p);
            if (keys[firstHash] == null) {
                keys[firstHash] = new ArrayList<Object[]>();
            }
            Object[] pair = new Object[2];
            pair[0] = secondHash;
            pair[1] = value;
            keys[firstHash].add(pair);
        }
        br.close();
    }

    public String query(String key){
        int keyLen = key.length();
        int firstHash = hash(key, keyLen, first_p);
        if (keys[firstHash] == null) {
            return INVALID;
        }
        ArrayList<Object> toSearch = keys[firstHash];
        int secondHash = hash(key, keyLen, second_p);
        for (int i = 0; i < toSearch.size(); i++) {
            if (toSearch.get(i)[0] == Integer.valueOf(secondHash)) {
                return toSearch.get(i)[1];
            }
        }
        return INVALID;
    }

    public static int hash(String str, int len, int p) {
        // Modifies a string to a unique number utilizing hash functions (Horner's method)
        int hashed = 0;
        for(int i = 0; i < len; i++) {
            hashed = (RADIX * hashed + str.charAt(i)) % p;
        }
        return hashed;
    }
}