package com.java.lang;

import org.junit.Test;

public class CharacterTest {

    Character character = Character.valueOf('\u0000');

    @Test
    public void testToString() {
        System.out.println(character);
        character = Character.valueOf('\uffff');
        System.out.println(character);
    }
}
