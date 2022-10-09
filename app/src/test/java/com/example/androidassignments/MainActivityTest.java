package com.example.androidassignments;

import junit.framework.TestCase;


public class MainActivityTest extends TestCase {

    public void testOnCreate() {
        assertTrue(MainActivity.class.getName().compareTo("com.example.androidassignments.MainActivity") == 0);


    }

    public void testOnActivityResult() {
    }
}