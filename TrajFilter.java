import java.io.File;

/**
 * Created by Administrator on 2017/4/2 0002.
 */
public class TrajFilter {

    /**
     * 删除轨迹长度小于length值的文件
     * @param length
     */
    public void deleteShortTraj(int length){
        File root=new File("TrajData");
        File[] files=root.listFiles();
        for(File file:files){
            String filename=file.getName();
            String[] filestr=filename.split("_");
            String[] filestr2=filestr[4].split("\\.");
            int i = Integer.parseInt(filestr2[0]);
            if(i<length){
                file.delete();
            }
        }
    }
}
