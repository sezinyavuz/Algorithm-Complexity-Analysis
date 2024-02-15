import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void swap(final ArrayList<Integer> arr, final int pos1, final int pos2) {
        final int temp = arr.get(pos1);
        arr.set(pos1, arr.get(pos2));
        arr.set(pos2, temp);
    }

    public static ArrayList<Integer> selectionSort(ArrayList<Integer> data, int n) {
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = 0; j < n; j++) {
                if (data.get(j) < data.get(min)) {
                    min = j;
                }
            }
            if (min != i) {
                swap(data, min, i);
            }
        }
        return data;
    }

    public static int partition(ArrayList<Integer> data, int low, int high) {
        int pivot = data.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {

            if (data.get(j) <= pivot) {
                i = i + 1;
                swap(data,i,j);
            }
        }
        swap(data, i + 1, high);
        return i + 1;
    }

    public static int hash(int i, int max, int numberOfBuckets) {
        return i / max * (numberOfBuckets - 1);
    }

    public static ArrayList<Integer> bucketSort(ArrayList<Integer> data, int n) {
        int numberOfBuckets = (int) Math.sqrt(data.size());
        ArrayList<ArrayList<Integer>> buckets = new ArrayList<>(numberOfBuckets);
        int max = Collections.max(data);

        for (int i = 0; i < numberOfBuckets; i++) {
            buckets.add(new ArrayList<>());
        }
        for (int i : data) {
            buckets.get(hash(i, max, numberOfBuckets)).add(i);
        }
        Comparator<Integer> comparator = Comparator.naturalOrder();

        for (ArrayList<Integer> bucket : buckets) {
            bucket.sort(comparator);
        }
        ArrayList<Integer> sortedArray = new ArrayList<>();
        for (ArrayList<Integer> bucket : buckets) {
            for (int i : bucket) {
                sortedArray.add(i);
            }
        }
        return sortedArray;
    }


    public static ArrayList<Integer> quickSort(ArrayList<Integer> data, int low, int high) {
        int stackSize = high - low + 1;
        int[] stack = new int[stackSize];//******
        int top = -1;
        stack[++top] = low;
        stack[++top] = high;
        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];
            int pivot = partition(data, low, high);
            if (pivot - 1 > low) {
                stack[++top] = low;
                stack[++top] = pivot - 1;

            }

            if (pivot + 1 < high) {
                stack[++top] = pivot + 1;
                stack[++top] = high;

            }
        }

        return data;
    }

    public static int linearSearch(ArrayList<Integer> data, int x,int n) {
        int size = n;
        ;
        for (int i = 0; i < (size - 1); i++) {
            if (data.get(i) == x) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(ArrayList<Integer> data, int x,int n) {
        int low = 0;
        int high = n - 1;
        while (high >= low) {
            int mid = (high + low) / 2;
            if (data.get(mid) == x) {
                return mid;
            } else if (data.get(mid) < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int pickRandom(ArrayList<Integer> data,int n){
        int randomIndex = ThreadLocalRandom.current().nextInt(n);
        return data.get(randomIndex);
    }



    public static void showAndSaveChart (String title,int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]

        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("SelectionSort", doubleX, yAxis[0]);
        chart.addSeries("QuickSort", doubleX, yAxis[1]);
        chart.addSeries("BucketSort", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }


    public static void main(String[] args) throws Exception {


        int[] input = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        ArrayList<Integer> data = new ArrayList<>();

        String filecsv = "TrafficFlowDataset.csv";
        File file1 = new File(filecsv);
        Scanner scanner1 = null;
        try {
            scanner1 = new Scanner(file1);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner1.nextLine();
        while (scanner1.hasNext()) {
            String[] line = scanner1.nextLine().split(",");
            data.add(Integer.valueOf(line[6]));
        }



        int index = 0;
        double[][] random = new double[3][10];

        for (int n : input) {
            double sumOfTime1 = 0;
            double sumOfTime2 = 0;
            double sumOfTime3 = 0;

            for (int i = 0; i < 10; i++) {
                Collections.shuffle(data);
                double startTime1 = System.nanoTime();
                selectionSort(data, n);
                double endTime1 = System.nanoTime();
                double totalTime1 = endTime1 - startTime1;
                sumOfTime1 += totalTime1;
                System.out.println("Execution time in milliseconds selection : " + n + "  " + totalTime1);

                Collections.shuffle(data);
                double startTime2 = System.nanoTime();
                quickSort(data,0,n-1);
                double endTime2 = System.nanoTime();
                double totalTime2 = endTime2 - startTime2;
                sumOfTime2 += totalTime2;
                System.out.println("Execution time in milliseconds  quick : " + n + "  " + totalTime2);

                Collections.shuffle(data);
                double startTime3 = System.nanoTime();
                bucketSort(data,n);
                double endTime3 = System.nanoTime();
                double totalTime3 = endTime3 - startTime3;
                sumOfTime3 += totalTime3;
                System.out.println("Execution time in milliseconds bucket : " + n + "  " + totalTime3);


            }
            System.out.println("\n");
            double average1 = (sumOfTime1 / 10000000);
            double average2 = (sumOfTime2 / 10000000);
            double average3 = (sumOfTime3 / 10000000);
            System.out.println("average selection   "+average1+"\n"+"average quick  " +average2+"\n"+"average bucket    " +average3 +"\n");
            random[0][index] = average1;
            random[1][index] = average2;
            random[2][index] = average3;

            index = index+1;

        }


        for(double[]i : random){
            for(double j : i){
                System.out.println(j);
            }
            System.out.println("\n");
        }




        // X axis data
        int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        // Save the char as .png and show it
        showAndSaveChart("Test on Random Input", inputAxis, random);


    double[][] sorted = new double[3][10];

        for (int n : input) {
            double sumOfTime1 = 0;
            double sumOfTime2 = 0;
            double sumOfTime3 = 0;

            for (int i = 0; i < 10; i++) {
                Collections.sort(data);
                double startTime1 = System.nanoTime();
                selectionSort(data, n);
                double endTime1 = System.nanoTime();
                double totalTime1 = endTime1 - startTime1;
                sumOfTime1 += totalTime1;
                System.out.println("Execution time in milliseconds selection : " + n + "  " + totalTime1);

                Collections.sort(data);
                double startTime2 = System.nanoTime();
                quickSort(data,0,n-1);
                double endTime2 = System.nanoTime();
                double totalTime2 = endTime2 - startTime2;
                sumOfTime2 += totalTime2;
                System.out.println("Execution time in milliseconds  quick : " + n + "  " + totalTime2);

                Collections.sort(data);
                double startTime3 = System.nanoTime();
                bucketSort(data,n);
                double endTime3 = System.nanoTime();
                double totalTime3 = endTime3 - startTime3;
                sumOfTime3 += totalTime3;
                System.out.println("Execution time in milliseconds bucket : " + n + "  " + totalTime3);


            }
            System.out.println("\n");
            double average1 = (sumOfTime1 / 10000000);
            double average2 = (sumOfTime2 / 10000000);
            double average3 = (sumOfTime3 / 10000000);
            System.out.println("average selection   "+average1+"\n"+"average quick  " +average2+"\n"+"average bucket    " +average3 +"\n");
            sorted[0][index] = average1;
            sorted[1][index] = average2;
            sorted[2][index] = average3;

            index = index+1;

        }





        // Save the char as .png and show it
        showAndSaveChart("Test on Sorted Input", inputAxis, sorted);



        double[][] reversed = new double[3][10];

        for (int n : input) {
            double sumOfTime1 = 0;
            double sumOfTime2 = 0;
            double sumOfTime3 = 0;

            for (int i = 0; i < 10; i++) {
                Collections.sort(data, Collections.reverseOrder());
                double startTime1 = System.nanoTime();
                selectionSort(data, n);
                double endTime1 = System.nanoTime();
                double totalTime1 = endTime1 - startTime1;
                sumOfTime1 += totalTime1;
                System.out.println("Execution time in milliseconds selection : " + n + "  " + totalTime1);

                Collections.sort(data, Collections.reverseOrder());
                double startTime2 = System.nanoTime();
                quickSort(data,0,n-1);
                double endTime2 = System.nanoTime();
                double totalTime2 = endTime2 - startTime2;
                sumOfTime2 += totalTime2;
                System.out.println("Execution time in milliseconds  quick : " + n + "  " + totalTime2);

                Collections.sort(data, Collections.reverseOrder());
                double startTime3 = System.nanoTime();
                bucketSort(data,n);
                double endTime3 = System.nanoTime();
                double totalTime3 = endTime3 - startTime3;
                sumOfTime3 += totalTime3;
                System.out.println("Execution time in milliseconds bucket : " + n + "  " + totalTime3);


            }
            System.out.println("\n");
            double average1 = (sumOfTime1 / 10000000);
            double average2 = (sumOfTime2 / 10000000);
            double average3 = (sumOfTime3 / 10000000);
            System.out.println("average selection   "+average1+"\n"+"average quick  " +average2+"\n"+"average bucket    " +average3 +"\n");
            reversed[0][index] = average1;
            reversed[1][index] = average2;
            reversed[2][index] = average3;

            index = index+1;

        }





        // Save the char as .png and show it
        showAndSaveChart("Test on Reversed Input", inputAxis, reversed);



        double[][] search = new double[3][10];

        for (int n : input) {
            double sumOfTime1 = 0;
            double sumOfTime2 = 0;
            double sumOfTime3 = 0;



            for (int i = 0; i < 1000; i++) {

                Collections.shuffle(data);
                int x =pickRandom(data,n);
                double startTime1 = System.nanoTime();
                linearSearch(data,x, n);
                double endTime1 = System.nanoTime();
                double totalTime1 = endTime1 - startTime1;
                sumOfTime1 += totalTime1;
                System.out.println("Execution time in milliseconds linearrandom : " + n + "  " + totalTime1);

                Collections.sort(data);
                int y =pickRandom(data,n);
                double startTime2 = System.nanoTime();
                linearSearch(data,y,n);
                double endTime2 = System.nanoTime();
                double totalTime2 = endTime2 - startTime2;
                sumOfTime2 += totalTime2;
                System.out.println("Execution time in milliseconds  linearsorted : " + n + "  " + totalTime2);

                Collections.sort(data);
                int z =pickRandom(data,n);
                double startTime3 = System.nanoTime();
                binarySearch(data,z,n);
                double endTime3 = System.nanoTime();
                double totalTime3 = endTime3 - startTime3;
                sumOfTime3 += totalTime3;
                System.out.println("Execution time in milliseconds binarysorted : " + n + "  " + totalTime3);


            }
            System.out.println("\n");
            double average1 = (sumOfTime1 / 1000);
            double average2 = (sumOfTime2 / 1000);
            double average3 = (sumOfTime3 / 1000);
            System.out.println("average 1   "+average1+"\n"+"average 2  " +average2+"\n"+"average 3    " +average3 +"\n");
            search[0][index] = average1;
            search[1][index] = average2;
            search[2][index] = average3;

            index = index+1;

        }


     

        // Save the char as .png and show it
        showAndSaveChart("Test on Search Input", inputAxis, search);



    }
}

