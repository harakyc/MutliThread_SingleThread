import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ArraySum {

    //Single-Thread Method
    public static int singleThreadArraySum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }

    //Multi-Thread Method

    public static int parallelArraySum(int[] array, int numThreads) throws InterruptedException {
        int chunkSize = array.length / numThreads;
        int[] partialSums = new int[numThreads];
        SumThread[] threads = new SumThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            // The start index for each thread, e.g. 2 * 20,000,000 = 40,000,000 --> Thread #3 starts at 40,000,000.
            int startIndex = i * chunkSize;

            // Determines the end index of the thread. If index equals the array size, hence it is the last thread, then set as end index.
            // The start and end index will be used later in the run method to determine the bounds of each thread.
            int endIndex;
            if (i == numThreads - 1) {
                endIndex = array.length;
            } else {
                endIndex = (i + 1) * chunkSize;
            }


            threads[i] = new SumThread(array, startIndex, endIndex, partialSums, i);
            threads[i].start();
        }

        for (SumThread thread : threads) {
            thread.join();
        }

        int sum = 0;
        for (int partialSum : partialSums) {
            sum += partialSum;
        }
        return sum;
    }

    private static class SumThread extends Thread {
        private final int[] array;
        private final int start;
        private final int end;
        private final int[] partialSums;
        private final int threadIndex;

        public SumThread(int[] array, int start, int end, int[] partialSums, int threadIndex) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.partialSums = partialSums;
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            partialSums[threadIndex] = sum;
        }
    }

        public static void main (String[] args) throws InterruptedException {
            long startTime, endTime, totalTime, startTime1, endTime1, totalTime1;

            Random random = new Random();

            int[] arr = new int[200000000];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = random.nextInt(10) + 1;
            }

            startTime = System.currentTimeMillis();
            int singleThreadSum = singleThreadArraySum(arr);
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            System.out.println("Single Thread Sum: " + singleThreadSum);
            System.out.println("Single Thread Elapsed Time (milliseconds): " + totalTime);

            startTime1 = System.currentTimeMillis();
            int multiThreadSum = parallelArraySum(arr, 5);
            endTime1 = System.currentTimeMillis();
            totalTime1 = endTime1 - startTime1;
            System.out.println("Multi-Thread Sum: " + singleThreadSum);
            System.out.println("Multi-Thread Elapsed Time (milliseconds): " + totalTime1);
        }
    }





