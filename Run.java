import java.io.File;
import java.util.ArrayList;



/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class Run {
    public static void main(String args[]){
        Analyze ana=new Analyze();
        int[] column=ana.findMax();

        ArrayList<Double> precisions=new ArrayList<>();
        File root=new File("TrajData");
        File[] files=root.listFiles();
        for(File file:files){
            ArrayList<Location> traj=LoadTraj.getCSVData(file);
            Markov m=new Markov();
            Compare c=new Compare();
            int []window=new int[]{1,2,3,4};
            for(int win:window){
                double d=c.getPrecision(traj,m.predict_order3(traj,win),3);
                precisions.add(d);
            }
        }
        OutputPrecision o=new OutputPrecision();
        o.makeCSV(precisions,"auto_order3.csv");
        int a=2;
    }
}
