import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class LoadTraj {
    /**
     * 读取轨迹数据
     * @param file
     * @return
     */
    public static ArrayList<Location> getCSVData(File file){
        ArrayList<Location> traj = new ArrayList<>();
        BufferedReader reader=null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString=reader.readLine())!=null){
                Location l=new Location(tempString);
                traj.add(l);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try {
                    reader.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        return traj;
        }

    }

    /**
     * 获取文件名e
     * @param file 输入文件
     * @return 文件名
     */
    public String getFilename(File file){
        return file.getName();
    }
}
