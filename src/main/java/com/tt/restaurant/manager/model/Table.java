package com.tt.restaurant.manager.model;

import lombok.Getter;

@Getter
public class Table {
    private final int id;
    private final int size;
    private int availablePlaces;

    public Table(int id, int size) {
        this.id = id;
        this.size = size;
        this.availablePlaces = size;
    }

    public Table takePlaces(int count) {
        this.availablePlaces -=count;
        return this;
    }

    public Table releasePlaces(int count) {
        this.availablePlaces += count;
        return this;
    }

    public boolean isAvailable() {
        return availablePlaces > 0;
    }
}
