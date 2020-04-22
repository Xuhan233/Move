package move;


import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;



public class Move {
    public static String walking = "C:\\Users\\Xu Han\\IdeaProjects\\Move\\src\\move\\WALKING.csv";
    public static String wt = "C:\\Users\\Xu Han\\IdeaProjects\\Move\\src\\move\\WALKING_AND_TURNING.csv";
    public static String turing = "C:\\Users\\Xu Han\\IdeaProjects\\Move\\src\\move\\TURNING.csv";



    public static ArrayList<ArrayList<Double>> readData(String filename) throws IOException {
        ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Double> timestamp = new ArrayList<Double>();
        ArrayList<Double> accel_x = new ArrayList<Double>();
        ArrayList<Double> accel_y = new ArrayList<Double>();
        ArrayList<Double> accel_z = new ArrayList<Double>();
        ArrayList<Double> gyro_x = new ArrayList<Double>();
        ArrayList<Double> gyro_y = new ArrayList<Double>();
        ArrayList<Double> gyro_z = new ArrayList<Double>();
        ArrayList<Double> mag_x = new ArrayList<Double>();
        ArrayList<Double> mag_y = new ArrayList<Double>();
        ArrayList<Double> mag_z = new ArrayList<Double>();
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader bf = new BufferedReader(fr);
            String line;
            while ((line = bf.readLine()) != null) {
                list.add(line);
            }
            bf.close();
            fr.close();
            for (int i = 1; i < list.size(); i++) {
                String[] weight = list.get(i).split(",");

                timestamp.add(Double.parseDouble(weight[0]));
                accel_x.add(Double.parseDouble(weight[1]));
                accel_y.add(Double.parseDouble(weight[2]));
                accel_z.add(Double.parseDouble(weight[3]));
                gyro_x.add(Double.parseDouble(weight[4]));
                gyro_y.add(Double.parseDouble(weight[5]));
                gyro_z.add(Double.parseDouble(weight[6]));
                mag_x.add(Double.parseDouble(weight[7]));
                mag_y.add(Double.parseDouble(weight[8]));
                mag_z.add(Double.parseDouble(weight[9]));

                data.add(timestamp);
                data.add(accel_x);
                data.add(accel_y);
                data.add(accel_z);
                data.add(gyro_x);
                data.add(gyro_y);
                data.add(gyro_z);
                data.add(mag_x);
                data.add(mag_y);
                data.add(mag_z);

            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found.");
        } catch (IOException ex) {
            System.out.println("Index out of bound");
        }

        return data;
    }




    public static double countingstep(ArrayList<Double> e) {
        double v0 = 0;
        double x0 = 0;
        for(int i = 0; i < e.size(); i++) {
           x0 += v0*0.005 + e.get(i)*0.000025/2;
           v0 = v0 + e.get(i)*0.005;
        }
         double step = x0;
        return step;
    }

    public static double countingturn(ArrayList<Double> e){
        double index = Math.PI/4;
        double count = 0;
        double angle = 0;
        for(int i = 0; i < e.size();i++) {
            //angle = angle + e.get(i)*0.005;
            angle = e.get(i)*0.005;
            if (Math.abs(angle) % index == 0) {
                //angle -=index;
                count++;
            }
//            else if (Math.abs(angle) > index &&angle < 0){
//                angle+=index;
//                count++;
//            }
        }
        return count;
    }


    public static ArrayList<ArrayList<Double>> Graphdateset() throws IOException {
        ArrayList<ArrayList<Double>> xy = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> y = new ArrayList<Double>();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Integer> runlist1 = new ArrayList<Integer>();
        ArrayList<Integer> runlist2 = new ArrayList<Integer>();
        runlist1.add(1);
        runlist2.add(6);


        ArrayList<Double> wt_w = Smoothdataoutput("w&t",runlist1 , 0.9).get(0);
        ArrayList<Double> wt_t = Smoothdataoutput("w&t",runlist2 , 0.9).get(0);


        Double xpostion = 0.0, ypostion = 0.0, distance=0.0, angle=0.0,v0 =0.0;

        for(int i = 0; i < wt_w.size();i++){
            if (i ==0){
            x.add(0.0);
            y.add(0.0);
            }else {
                distance= v0*0.005+0.5*wt_w.get(i)*0.005*0.005;
                angle +=  + 0.005*wt_t.get(i);
                xpostion += distance*Math.cos(angle);
                ypostion += distance*Math.sin(angle);
                v0= v0 + wt_t.get(i)*0.005;
                x.add(xpostion);
                y.add(ypostion);
            }

        }
        xy.add(x);
        xy.add(y);
        return xy;
    }
    public static ArrayList<ArrayList<Double>> Smoothdataoutput(String filename, ArrayList<Integer> number, double Alpha) throws IOException {
        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();

        if (filename == "walk") {
            ArrayList<ArrayList<Double>> walking_smooth = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> walkingADT = readData(walking);
            ArrayList<Double> t = new ArrayList<Double>();
            for (int index = 0; index < number.size(); index++) {
                t.clear();
                for (int i = 0; i < (4408); i++) {
                    if (i == 0) {
                        t.add(walkingADT.get(number.get(index)).get(0));
                    } else {
                        t.add((t.get(i - 1) * (1 - Alpha)) + walkingADT.get(number.get(index)).get(i) * Alpha);
                    }
                }
                walking_smooth.add(t);
            }
            result = (ArrayList<ArrayList<Double>>) walking_smooth.clone();
        } else if (filename == "turn") {
            ArrayList<ArrayList<Double>> turning_smooth = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> turingADT = readData(turing);
            ArrayList<Double> o = new ArrayList<Double>();
            for (int index = 0; index < number.size(); index++) {
                o.clear();
                for (int j = 0; j < 4408; j++) {
                    if (j == 0) {
                        o.add(turingADT.get(number.get(index)).get(0));
                    } else {
                        o.add((o.get(j - 1) * (1 - Alpha)) + turingADT.get(number.get(index)).get(j) * Alpha);
                    }
                }
                turning_smooth.add(o);
            }
            result = (ArrayList<ArrayList<Double>>) turning_smooth.clone();
        } else if (filename == "w&t") {
            ArrayList<ArrayList<Double>> wt_smooth = new ArrayList<ArrayList<Double>>();
            ArrayList<ArrayList<Double>> wtADT = readData(wt);
            ArrayList<Double> x = new ArrayList<Double>();
            for (int index = 0; index < number.size(); index++) {
                x.clear();
                for (int k = 0; k < 4408; k++) {
                    if (k == 0) {
                        x.add(wtADT.get(number.get(index)).get(0));
                    } else {
                        x.add((x.get(k - 1) * (1 - Alpha)) + wtADT.get(number.get(index)).get(k) * Alpha);
                    }
                }
                wt_smooth.add(x);
            }
            result = (ArrayList<ArrayList<Double>>) wt_smooth.clone();
        }
        return result;
    }
}





