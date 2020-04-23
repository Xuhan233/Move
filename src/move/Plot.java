package move;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class Plot extends ApplicationFrame{

    public static String walking = "C:\\Users\\Xu Han\\IdeaProjects\\Move\\src\\move\\WALKING.csv";

    public Plot(String applicationTitle, String chartTitle ) throws IOException {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Time" ,
                "Walking_x" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 2000 , 2000 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }




    public Plot(String title) {
        super(title);
    }

    private XYDataset createDataset() throws IOException {
        final XYSeries WalkingDTA = new XYSeries( "WalkingDTA" );

        for(int i = 0 ; i < Move.Graphdateset().get(0).size();i++){
            WalkingDTA.add(Move.Graphdateset().get(1).get(i) , Move.Graphdateset().get(0).get(i));
        }

        final XYSeries Smooth_walking = new XYSeries("smooth_walking");

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        int dataset_size = (Move.Smoothdataoutput("walk", list, 0.01).get(0).size())/20;
        for (int i = 1; i<dataset_size-1; i++) {
            Smooth_walking.add(i,Move.Smoothdataoutput("walk", list, 0.01).get(0).get(i*20));
        }

        final XYSeries Origin_walking = new XYSeries("origin_walking");

        int size = (Move.Smoothdataoutput("walk", list, 0.9).get(0).size())/20;
        
        for(int j = 1; j <size-1 ; j++) {

            Origin_walking.add(j, Move.readData("C:\\Users\\Xu Han\\IdeaProjects\\Move\\src\\move\\WALKING.csv").get(1).get(j*20));
        }







        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(WalkingDTA );
        dataset.addSeries(Smooth_walking);
        dataset.addSeries(Origin_walking);

        return dataset;
    }

    public static void main(String[] args ) throws IOException {
        ArrayList<Integer> smoothdatalist = new ArrayList<Integer>();
        for(int a = 1; a < 10; a++) {
            smoothdatalist.clear();
            smoothdatalist.add(a);
            System.out.println("Smooth Walking data column: " + a + "\n" + Move.Smoothdataoutput("walk", smoothdatalist,0.6).get(0));

        }
        for(int b = 1;  b< 10; b++) {
            smoothdatalist.clear();
            smoothdatalist.add(b);

            System.out.println("\nSmooth Turning data column: " + b + "\n" + Move.Smoothdataoutput("turn", smoothdatalist,0.6).get(0));
        }
        for(int c = 1; c < 10; c++) {
            smoothdatalist.clear();
            smoothdatalist.add(c);
            System.out.println("Smooth Walking and Turning data column: " + c + "\n" + Move.Smoothdataoutput("w&t", smoothdatalist,0.6).get(0));
        }



        ArrayList<Integer> runlist = new ArrayList<Integer>();

        runlist.add(2);
        System.out.println("Smooth data for Walking\n" + Move.Smoothdataoutput("walk", runlist,0.9));
        double step1 = Move.countingstep(Move.Smoothdataoutput("walk",runlist , 0.01).get(0));
        double step2 = Move.countingstep(Move.Smoothdataoutput("w&t",runlist , 0.01).get(0));
        System.out.println("\nStep Walking: " + step1
                + "\nStepS Walking and turning: " +step2
                + "\nTotal Steps: " + (step1 + step2) +"\n" );

        runlist.remove(0);
        runlist.add(6);
        //System.out.println(runlist);
        double t1 = Move.countingturn(Move.Smoothdataoutput("turn",runlist,0.01).get(0));
        double t2 = Move.countingturn(Move.Smoothdataoutput("w&t",runlist,0.01).get(0));
        System.out.println("Turning Turning " + t1
                + "\nTurning  Walking and Turning: "+ t2
                + "\nTotal Turnings " + (t1 + t2));


        Plot chart = new Plot("Browser Usage Statistics",
                "Smoothchart");
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}