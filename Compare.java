import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6 0006.
 */
public class Compare {
    /**
     * 将轨迹数据和预测轨迹对比，计算预测准确率
     * @param traj
     * @param result
     * @return
     */
    public double getPrecision(ArrayList<Location> traj,ArrayList<Location> result,int k){
        int j=0;  //预测正确个数
        for(int i=k+1;i<traj.size();i++){
            if(traj.get(i).equals(result.get(i-k-1))){
                j++;
            }
        }
        double precision=((double)j)/(result.size()-k-1);
        return precision;
    }
}
