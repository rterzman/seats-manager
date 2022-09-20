package com.tt.restaurant.manager.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.tt.restaurant.manager.model.CustomerGroup;
import com.tt.restaurant.manager.model.Table;

class LinearSeatingManagerTest {

    @Test
    void shouldReserveTableForCustomers() {
        List<Table> tables = generateTables(6, 4);
        SeatingManager testable = new LinearSeatingManager(tables);

        CustomerGroup customer_1 = new CustomerGroup(3);

        testable.arrives(customer_1);
        Table locate = testable.locate(customer_1);

        assertNotNull(locate);
    }

    @Test
    void shouldThrowAnExceptionForTooBigCustomerGroup() {
        SeatingManager testable = getManager(3, 6);
        CustomerGroup customer_1 = new CustomerGroup(6);

        assertThrows(IllegalStateException.class, () -> testable.arrives(customer_1));
    }

    /*
    a group of 6 is waiting for a table and there are 4 empty seats at a table for 6;
    if a group of 2 arrives you may put them at the table for 6 but only if you have nowhere else to put them.
    This may mean that the group of 6 waits a long time, possibly until they become frustrated and leave.
     */
    @Test
    void shouldProvideRightTable() {
        Table table_1 = new Table(1, 6);
        Table table_2 = new Table(2, 3);
        LinearSeatingManager testable = new LinearSeatingManager(List.of(table_1, table_2));
        CustomerGroup customer_1 = new CustomerGroup(3);
        CustomerGroup customer_2 = new CustomerGroup(2);
        CustomerGroup customer_3 = new CustomerGroup(2);
        testable.arrives(customer_1);

        Table locate = testable.locate(customer_1);
        assertNotNull(locate);
        assertEquals(locate.getId(), table_2.getId());

        testable.arrives(customer_2);
        Table locate_2 = testable.locate(customer_2);
        assertNotNull(locate_2);
        assertEquals(locate_2.getId(), table_1.getId());

        testable.arrives(customer_3);
        Table locate_3 = testable.locate(customer_3);
        assertNotNull(locate_3);
        assertEquals(locate_3.getId(), table_1.getId());
    }

    @Test
    void shouldLeavesSeatsSuccessfully() {
        SeatingManager testable = getManager(6, 12);
        CustomerGroup group = new CustomerGroup(4);
        testable.arrives(group);

        assertNotNull(testable.locate(group));

        testable.leaves(group);
        assertNull(testable.locate(group));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenThereIsNoTables() {
        LinearSeatingManager testable = new LinearSeatingManager(new ArrayList<>());

        assertThrows(IllegalStateException.class, () -> testable.arrives(new CustomerGroup(6)));
    }

    private SeatingManager getManager(int maxSize, int maxTableNumber) {
        return new LinearSeatingManager(generateTables(maxSize, maxTableNumber));
    }

    private List<Table> generateTables(int maxSize, int maxTableNumber) {
        return IntStream.range(1, maxTableNumber)
                .boxed()
                .map(id -> new Table(id, ThreadLocalRandom.current().nextInt(maxSize)))
                .collect(Collectors.toList());
    }

}