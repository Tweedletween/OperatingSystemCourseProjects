/**
 * Created by miaoyanz on 11/7/16.
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.lang.Integer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class NFUAgingDemo extends Application{

    //a map to store the reference bit of each page in the memory
    public static Map<Integer, Integer> pageRefs = new HashMap<Integer, Integer>();
    //a map to store the counter of each page in the memory
    public static Map<Integer, Integer> pageCounter = new HashMap<Integer, Integer>();
    //a map to store the total number of page faults of corresponding number of page frames
    public static Map<Integer, Integer> frames_faults = new TreeMap<Integer, Integer>();

    public static void main(String[] args){
        //testing from number of page frame = 4
        int framesNo = 4;
        int pageFaults = 0;
        //assume that there are 10 page references in each clock tick
        int it = 10;

        //loop until the number of page frame > 512
        while(framesNo <= 512) {
            //read in the page references
            File fil;
            try {
                fil = new File("pageReferences.txt");
                Scanner scanner = new Scanner(fil);
                int pageNo;
                while (scanner.hasNextInt()) {
                    pageNo = scanner.nextInt();
                    if (handlePageRequest(pageNo,framesNo)){
                        pageFaults++;
                    }

                    it--;
                    //update the counter every 10 page references
                    if(it == 0) {
                        counterWithAging();
                        it = 10;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error Reading File");
                e.printStackTrace();
            }

            frames_faults.put(framesNo, pageFaults);
            pageRefs.clear();
            pageCounter.clear();
            //change the number of frames to find page faults next loop
            framesNo *= 2;
            pageFaults = 0;
        }

        drawFaultRate(frames_faults, args);
    }

    public static boolean handlePageRequest(int pageNo, int framesNo){
        if(pageCounter.containsKey(pageNo)){
            //set the reference bit
            if(pageRefs.get(pageNo) == 0){
                pageRefs.put(pageNo, 1);
            }
            return false;
        }else{
            handlePageFault(pageNo, framesNo);
            return true;
        }
    }

    public static void handlePageFault(int pageNo, int framesNo){
        if (pageCounter.size() >= framesNo){
            //get the pageNo with smallest counter
            int lstRefPage = 0;
            int lsRef = Integer.MAX_VALUE;
            for(int k: pageCounter.keySet()){
                if(pageCounter.get(k) < lsRef){
                    lstRefPage = k;
                    lsRef = pageCounter.get(k);
                }
            }
            //remove the pageNo with smallest counter
            pageCounter.remove(lstRefPage);
            pageRefs.remove(lstRefPage);
        }
        pageCounter.put(pageNo, 0);
        pageRefs.put(pageNo, 1);
    }

    public static void counterWithAging(){
        for(int kp: pageRefs.keySet()) {
            int temp;
            temp = pageCounter.get(kp);
            if (pageRefs.get(kp) == 1) {
                temp = temp / 2 + 128;
                pageRefs.put(kp,0);
            } else {
                temp = temp / 2;
            }
            pageCounter.put(kp,temp);
        }
    }

    public static void drawFaultRate(Map<Integer, Integer> frames_faults, String[] args){
        System.out.println("No of frames  | Total Page Faults | " + "Page Faults / 1000 |");
        for (int fr : frames_faults.keySet()){
            System.out.printf("%13d | %17d | %18.1f |\n",fr, frames_faults.get(fr), frames_faults.get(fr) / 10.0);
        }
        launch(args);
    }

    @Override public void start(Stage stage) {
        stage.setTitle("Page Fault Ratio");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Page Frames");
        yAxis.setLabel("Page Faults / 1000");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        for (int fr : frames_faults.keySet()){
            series.getData().add(new XYChart.Data(fr, frames_faults.get(fr) / 10.0));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

}
