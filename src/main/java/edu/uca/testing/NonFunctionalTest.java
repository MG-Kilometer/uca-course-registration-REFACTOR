package edu.uca.testing;

public class NonFunctionalTest {

    public NonFunctionalTest(int max_iterations) {

        long totalTime = 0;

        for (int i = 0; i < max_iterations; i++) {

            long startTime = System.nanoTime();

            //run all three test suites
            UnitTest unit_test = new UnitTest();
            ComponentTest comp_test = new ComponentTest();
            SystemTest sys_test = new SystemTest();

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            totalTime += duration;

        }

        double averageTimeMs = (totalTime / (double)max_iterations) / 1_000_000.0; //basically is nanosecond to second converter (1e-6)

        System.out.println("Average runtime for all tests over " + max_iterations + " iterations: " + averageTimeMs + " ms");

    }

}
