package edu.uca.testing;

public class TestMain {
    public static void main(String[] args) {

        // unit test
        UnitTest unit_test = new UnitTest();

        // component test
        ComponentTest comp_test = new ComponentTest(); //functionally also does functional testing (tests all menu options)

        // system test
        SystemTest sys_test = new SystemTest();

        // non functional test (timing) (25 iterations)
        NonFunctionalTest non_functional_test = new NonFunctionalTest(25);

        System.out.println("\n\nTesting is Done!");

    }
}
