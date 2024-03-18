import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;

class Main {
    public static void main(String args[]) throws IOException {

        /// IMPORTTTANNNNTTT !!!
        ///Put the data file that has .csv extension in src folder

        int[] dataInit =  ReadFile.getData("src\\TrafficFlowDataset.csv"); // initial order data.


        //Random
        sortAndGraph(dataInit, "Random");

        //Sorted
        int [] sortedData = Sort.mergeSort(dataInit);
        sortAndGraph(sortedData, "Sorted");

        //Reversed
        int[] reversed = IntStream.range(0, sortedData.length)
                .map(i -> sortedData[sortedData.length - i - 1])
                .toArray();
        sortAndGraph(reversed, "Reversed");

        /////IMPORTANT NOTE
        ///// after the method below gets executed, a graph named Searching.png will be created
        ///// texts on that picture wronf but all the numerical data correct
        ///// left side , it should actuallyy write "in nanoseconds"
        ///// and the first graph name on upper right corner should be "Linear(Random)" not "MergeSort"
        ///// the second graph name on upper right corner should be "Linear(Sorted)" not "CountingSort"
        ///// the third graph name on upper right corner should be "Binary" not "Insertion Sort"
        searchAndGraph(dataInit, "Searching");




    }






    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Nanoseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Merge Sort ", doubleX, yAxis[0]);
        chart.addSeries("Counting Sort)", doubleX, yAxis[1]);
        chart.addSeries("Insertion Sort", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    private static void sortAndGraph(int [] baseArr, String graphName) throws IOException {
        int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 251281};
        double[][] yAxisRandom = new double[3][10];


        for(int i = 0; i < 3; i++){ // i = 0 mergesort, i = 1 countingsort, i = 2 insertion sort

            int col = 0;
            for(int size = 500; size <= 256000; size *= 2){

                size = (size == 256000) ? 250000 : size;

                int avg = 0;
                for(int exp = 0; exp < 10; exp++){

                    int [] dataToSort = Arrays.copyOf(baseArr, size);  // correct amount of data each time.
                    if(i == 0){ //mergeSort
                        long start = System.currentTimeMillis();
                        Sort.mergeSort(dataToSort);
                        long end = System.currentTimeMillis();
                        long elapsedTime = end - start;

                        avg += elapsedTime;

                    }else if(i == 1){ //Counting Sort

                        long start = System.currentTimeMillis();
                        int max = Arrays.stream(dataToSort).max().getAsInt();  //as told on piazza the time of finding the max is measured.
                        Sort.countingSort(dataToSort, max);
                        long end = System.currentTimeMillis();
                        long elapsedTime = end - start;

                        avg += elapsedTime;

                    }else { // Insertion Sort
                        long start = System.currentTimeMillis();
                        Sort.insertionSort(dataToSort);
                        long end = System.currentTimeMillis();
                        long elapsedTime = end - start;

                        avg += elapsedTime;
                    }
                }
                avg /= 10;

                yAxisRandom[i][col++] = avg;

            }
        }


        showAndSaveChart(graphName, inputAxis, yAxisRandom);

    }

    private static void searchAndGraph(int [] baseArr, String graphName) throws IOException {
        int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 251281};
        double[][] yAxis = new double[3][10];

        for(int i = 0; i < 3; i++){ // i = 0 linear random, i = 1 linear sorted, i = 2 binary sorted.

            int col = 0;
            for(int size = 500; size <= 256000; size *= 2){


                size = (size == 256000) ? 250000 : size;

                int avg = 0;
                for(int exp = 0; exp < 1000; exp++){
                    Random random = new Random();
                    int randomNumber = baseArr[random.nextInt(size)];


                    int [] dataToSort = Arrays.copyOf(baseArr, size);  // correct amount of data each time.

                    if(i == 0){ // linear random

                        long start = System.nanoTime();
                        Search.linearSearch(dataToSort, randomNumber);
                        long end = System.nanoTime();
                        long elapsed = end - start;

                        avg += elapsed;
                    }else if(i == 1){ // linear sorted
                        dataToSort = Sort.mergeSort(dataToSort);
                        long start = System.nanoTime();
                        Search.linearSearch(dataToSort, randomNumber);
                        long end = System.nanoTime();
                        long elapsed = end - start;

                        avg += elapsed;
                    }else{ //binary sorted
                        dataToSort = Sort.mergeSort(dataToSort);
                        long start = System.nanoTime();
                        Search.binarySearch(dataToSort, randomNumber);
                        long end = System.nanoTime();
                        long elapsed = end - start;

                        avg += elapsed;
                    }
                }
                avg /= 1000;

                yAxis[i][col++] = avg;
            }
        }


        showAndSaveChart(graphName, inputAxis, yAxis);
    }
}
