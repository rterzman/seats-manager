package com.tt.restaurant.manager.service;

import static java.util.Objects.nonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.tt.restaurant.manager.model.CustomerGroup;
import com.tt.restaurant.manager.model.Table;

public class LinearSeatingManager implements SeatingManager {
    private final int maxTableSize;
    private final Map<CustomerGroup, Table> customerTableMap;
    private final TreeMap<Integer, List<Table>> tableSizeMap;

    /**
     * O(n^2) memory usage: tableSizeMap -> O(n) & customerTableMap -> O(n) => O(n*n) = O(n)
     */
    public LinearSeatingManager(List<Table> tables) {
        this.maxTableSize = tables.stream().map(Table::getSize).max(Integer::compare).orElse(6);
        this.tableSizeMap = tables.stream()
                .collect(Collectors.groupingBy(Table::getSize, TreeMap::new, Collectors.toList()));
        this.customerTableMap = new HashMap<>();
    }

    /**
     * O(N) time complexity in the worst scenario
     */
    @Override
    public void arrives(CustomerGroup group) {
        int groupSize = group.size();
        if (groupSize > maxTableSize) {
            throw new IllegalStateException("There are no tables for group in " + group.size());
        }
        Table table = getTable(groupSize);
        if (nonNull(table)) {
            table.takePlaces(groupSize);
            customerTableMap.put(group, table);
        } else {
            throw new IllegalStateException("Table for group in " + group.size() + " is not found");
        }
    }

    private Table getTable(int groupSize) {
        int lookupAmount = groupSize;
        while (lookupAmount < maxTableSize) {
            List<Table> tables = getTables(lookupAmount);
            if (tables.isEmpty()) {
                lookupAmount++;
                continue;
            }
            for (Table table : tables) {
                if (table.isAvailable() && groupSize <= table.getAvailablePlaces()) {
                    return table;
                }
            }
            lookupAmount++;
        }
        return null;
    }

    private List<Table> getTables(int placeAmount) {
        return Optional.ofNullable(this.tableSizeMap.ceilingEntry(placeAmount))
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());
    }

    /**
     * O(1) time complexity
     */
    @Override
    public void leaves(CustomerGroup group) {
        Table table = customerTableMap.remove(group);
        table.releasePlaces(group.size());
    }

    @Override
    public Table locate(CustomerGroup group) {
        return customerTableMap.get(group);
    }
}
