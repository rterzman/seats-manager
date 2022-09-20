package com.tt.restaurant.manager.model;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public record CustomerGroup(String id, int size) {
    private static final RandomStringGenerator RANDOM_STRING_GENERATOR = new RandomStringGenerator.Builder().withinRange(
            '0', 'z').filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();

    public CustomerGroup(int size) {
        this(RANDOM_STRING_GENERATOR.generate(12), size);
    }
}
