package hardwar.branch.prediction.shared.devices;

/*
 * our Page History Table model
 * read below assumptions about pre-defined PHT
 * ------------------------------------------------------
 * ASSUMPTIONS:
 * 1) the entry of the PHT is a # of bits which # is given as PHT initial param
 *
 * 2) each entry of the PHT is mapped to a #-bit which shows the previous pattern of branches
 *
 * 3) when PHT is being read, the block associated with that address is returned
 *
 * 4) the data (bit array) which is saved in the cache is not the data (bit array) that put or
 * set default is used but a copy of it. this design is based on the fact that the outer components
 * of cache can not manipulate the cache directly!
 *
 * 5) there is no checker if the in value of PHT entry is bigger than the cache last entry address.
 * therefore, be aware! your bug won't throw any error here
 * -------------------------------------------------------
 */


import hardwar.branch.prediction.shared.Bit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PageHistoryTable implements Cache<Bit[], Bit[]> {

    private final int nRows; // number of PHT entries
    private final int nColumns; // number of bits in a block
    private final Map<String, Bit[]> PHT; // save entry and blocks


    public PageHistoryTable(int nRows, int nColumns) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.PHT = new TreeMap<>();
    }


    /**
     * Get the value associated with the given key from the cache, or a default value if the key is not found.
     *
     * @param entry the key to look up in the cache
     * @return the value associated with the key, or null if the key is not found
     */
    @Override
    public Bit[] get(Bit[] entry) {
        // Convert the entry array to a string and use it as the key for PHT.getOrDefault()
        return PHT.get(Bit.arrayToString(entry));
    }

    /**
     * Insert a new key-value pair into the cache.
     *
     * @param entry the key to insert into the cache
     * @param value the value to associate with the key
     * @throws RuntimeException if the length of the block is not equal to nColumns
     */
    @Override
    public void put(Bit[] entry, Bit[] value) {
        // Check that the length of the block is equal to nColumns
        if (value.length != nColumns) {
            throw new RuntimeException("invalid number of bits for cache block");
        }

        // Convert the entry array to a string and use it as the key for PHT.put()
        String entryS = Bit.arrayToString(entry);
        PHT.put(entryS, Arrays.copyOf(value, nColumns));
    }

    /**
     * put the value in the entry if the entry is not associated to any block.
     *
     * @param entry the address which is selected to put the data in it
     * @param value the data which is saved in address (key) if the key is not mapped to any not-null data
     */
    @Override
    public void putIfAbsent(Bit[] entry, Bit[] value) {
        Bit[] block = get(entry);

        // If the entry is not found in the cache, insert the default value and return it
        if (block == null) {
            put(entry, Arrays.copyOf(value, nColumns));
        }
    }

    /**
     * Returns the value associated with the given key, or a default value if the key is not found in the cache.
     * If the key is not found in the cache, the default value is inserted into the cache.
     *
     * @param entry        the address to look up in the cache
     * @param defaultValue the default value to insert into the cache if the key is not found
     * @return the value associated with the key, or the default value if the key is not found
     */
    public Bit[] setDefault(Bit[] entry, Bit[] defaultValue) {
        if (defaultValue == null) throw new RuntimeException("block can not be null");

        putIfAbsent(entry, defaultValue);
        return get(entry);
    }

    /**
     * Clear all entries from the cache.
     */
    @Override
    public void clear() {
        PHT.clear();
    }

    /**
     * Returns a string representation of the current state of the PHT.
     *
     * @return a table with the address and block data for each entry in the PHT
     */
    @Override
    public String monitor() {
        StringBuilder sb = new StringBuilder();
        sb.append("+----------------------------------+\n");
        sb.append(String.format("| %-19s | %-10s |\n", "Address", "Block"));
        sb.append("|---------------------|------------|\n");

        for (HashMap.Entry<String, Bit[]> entry : PHT.entrySet()) {
            String address = entry.getKey();
            Bit[] block = entry.getValue();
            if (address.length() > 16) {
                String address16 = address.substring(0, 16);
                sb.append(String.format("| %-16s... | %-10s |\n", address16, Bit.arrayToString(block)));
            } else {
                sb.append(String.format("| %-19s | %-10s |\n", address, Bit.arrayToString(block)));
            }
            sb.append("+----------------------------------+\n");

        }

        return sb.toString();
    }
}
