package edu.uca.testing;

public class TestMain {
    public static void main(String[] args) {

        // unit test
        UnitTest unit_test = new UnitTest();
        
        // component test
        ComponentTest comp_test = new ComponentTest();

        // system test
        SystemTest sys_test = new SystemTest(); // runs tests

    }
}
