import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/23 0023.
 */
public class Analyze {
    public int[] findMax() {
        int[] column=new int[]{0,0,0,0,0,0,0,0,0,0};
        File root=new File("precisions");
        File[] files=root.listFiles();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(files[0]));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String[] strs=tempString.split(",");

                double[] datas=new double[10];
                for(int i=0;i<10;i++){
                    datas[i]=Double.valueOf(strs[i]);
                }

                double max=datas[0];
                for(int i=1;i<10;i++){
                    if(datas[i]>max){
                        max=datas[i];
                    }
                }

                for(int i=0;i<10;i++){
                    if(datas[i]==max){
                        column[i]++;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return column;

    }
}
