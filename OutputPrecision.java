import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/26 0026.
 */
public class OutputPrecision {
    public void makeCSV(ArrayList<Double> precisions, String filename){
        final String NEW_LINE = "\r\n";  //换行符
        StringBuilder csvStr = new StringBuilder();
        try{
            File file=new File("precisions/"+filename);
            if (file.createNewFile()){
                //把ArrayList<Double>转换为List<String>
                List<String> result = new ArrayList<String>();
                for(int i=0;i<precisions.size();i=i+4){
                    String str1=Double.toString(precisions.get(i));
                    String str3=Double.toString(precisions.get(i+1));
                    String str5=Double.toString(precisions.get(i+2));
                    String str7=Double.toString(precisions.get(i+3));
                    result.add(str1+","+str3+","+str5+","+str7);
                }

                //数据行
                for(String csvData : result){
                    csvStr.append(csvData).append(NEW_LINE);
                }

                //写文件
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write(csvStr.toString());
                writer.flush();
                writer.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
