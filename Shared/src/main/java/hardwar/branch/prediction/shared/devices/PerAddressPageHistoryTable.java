package hardwar.branch.prediction.shared.devices;

/*
 * Our Per Address Page History Table
 * read below assumptions about pre-defined PAPHT
 * ------------------------------------------------------
 * ASSUMPTIONS:
 * 1) the entry of the PAPHT is a number of bits which the first # bits select the PHT and other bits select the block
 * associated to the PHT
 *
 * 2) each entry of the PAPHT is mapped to a smaller cache (PHT)
 *
 * 3) when PAPHT is being read, the block associated with that address is returned
 *
 * 4) the data (bit array) which is saved in the cache is not the data (bit array) that put or
 * set default is used but a copy of it. this design is based on the fact that the outer components
 * of cache can not manipulate the cache directly!
 *
 * 5) there is no checker if the in value of PAPHT entry is bigger than the cache last entry address.
 * therefore, be aware! your bug won't throw any error here
 * ------------------------------------------------------
 */

import hardwar.branch.prediction.shared.Bit;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class PerAddressPageHistoryTable implements Cache<Bit[], Bit[]> {

    private final int branchAddressSelectorBitsNumber; // number of bits from pc which determine which PHT in PAPHT must be used.
    private final int rowsPerPageHistoryTableNumber; // number of rows per PHT
    private final int columnsPerBlockNumber; // number of columns per block in a PHT
    private final Map<String, Cache<Bit[], Bit[]>> historyTables; // per address prediction history table. string represent the
    // PHT which must be used and the cache is the PHT associated to that slice of PC


    public PerAddressPageHistoryTable(int branchAddressSelectorBitsNumber, int rowsPerPageHistoryTableNumber, int columnsPerBlockNumber) {
        this.branchAddressSelectorBitsNumber = branchAddressSelectorBitsNumber;
        this.rowsPerPageHistoryTableNumber = rowsPerPageHistoryTableNumber;
        this.columnsPerBlockNumber = columnsPerBlockNumber;

        // initialize the Per Address Predication History Table
        this.historyTables = new TreeMap<>();
    }

    /**
     * @param entry think of key as address. First nPCSelector bits is used for finding the associated PHT
     *              next bits is used to find the block.
     * @return the value associated with the key, or null if the key is not found
     */
    @Override
    public Bit[] get(Bit[] entry) {
        String cacheSelector = getCacheSelector(entry);
        Bit[] blockSelector = getBlockSelector(entry);

        Cache<Bit[], Bit[]> historyTable = historyTables.get(cacheSelector);
        return historyTable.get(blockSelector);
    }

    /**
     * Map the value to the entry.
     * if the associated cache or block is not defined then exception will be thrown
     *
     * @param entry the address which is selected to put the data in it
     * @param value the data which is saved in address (key)
     */
    @Override
    public void put(Bit[] entry, Bit[] value) {
        // Check that the length of the block is equal to nColumns
        if (value.length != columnsPerBlockNumber) {
            throw new RuntimeException("invalid number of bits for cache block");
        }

        String cacheSelector = getCacheSelector(entry);
        Bit[] blockSelector = getBlockSelector(entry);

        Cache<Bit[], Bit[]> historyTable = historyTables.get(cacheSelector);
        if (historyTable == null) throw new RuntimeException("The history table is not associated to the PAPHT");
        historyTable.put(blockSelector, value);
    }

    /**
     * If the cache is not associated yet or no block is mapped to the PHT then map the default value
     * to the history tables
     *
     * @param entry the address which is selected to put the data in it
     * @param value the data which is saved in address (key) if the key is not mapped to any not-null data
     */
    @Override
    public void putIfAbsent(Bit[] entry, Bit[] value) {
        // Check that the length of the block is equal to nColumns
        if (value.length != columnsPerBlockNumber) {
            throw new RuntimeException("invalid number of bits for cache block");
        }

        String cacheSelector = getCacheSelector(entry);
        Bit[] blockSelector = getBlockSelector(entry);

        Cache<Bit[], Bit[]> historyTable = historyTables.get(cacheSelector);
        if (historyTable == null) {
            historyTable = new PageHistoryTable(rowsPerPageHistoryTableNumber, columnsPerBlockNumber);
            historyTables.put(cacheSelector, historyTable);
        }
        historyTable.putIfAbsent(blockSelector, value);
    }

    /**
     * @param entry        the address
     * @param defaultValue default value if the address is not associated with any block in cache
     * @return the old value if exist otherwise the default value
     */
    @Override
    public Bit[] setDefault(Bit[] entry, Bit[] defaultValue) {
        if (defaultValue == null) throw new RuntimeException("block can not be null");

        putIfAbsent(entry, defaultValue);
        return get(entry);
    }

    /**
     * Get the cache selector string for PAPHT
     *
     * @param entry the address
     * @return the cache selector bits
     */
    private String getCacheSelector(Bit[] entry) {
        return Bit.arrayToString(Arrays.copyOf(entry, branchAddressSelectorBitsNumber));
    }

    /**
     * Get the block selector Bits for PHT
     *
     * @param entry the address
     * @return the block selector bits.
     */
    private Bit[] getBlockSelector(Bit[] entry) {
        return Arrays.copyOfRange(entry, branchAddressSelectorBitsNumber, entry.length);
    }


    /**
     * Clear all the caches.
     */
    @Override
    public void clear() {
        historyTables.forEach((k, v) -> v.clear());
        historyTables.clear();
    }

    /**
     * Returns a string representing the current state of the Per Address Prediction History Table.
     * The string includes a separate monitor report for each PHT in the PAPHT map, along with the selector
     * string for each PHT.
     *
     * @return a string representing the current state of the Per Address Prediction History Table
     */
    @Override
    public String monitor() {
        StringBuilder sb = new StringBuilder();
        historyTables.forEach((k, v) -> {
            sb.append("PHT for selector: ");
            sb.append(k);
            sb.append("\n");
            sb.append(v.monitor());
            sb.append("\n");
        });
        return sb.toString();
    }
}