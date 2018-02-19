import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class Markov {
    /**
     * 零阶markov预测
     * @param traj
     * @return
     */
    public ArrayList<Location> predict_order0(List<Location> traj,int s){
        ArrayList<Location> result=new ArrayList<>();

        //初始化markov矩阵
        ArrayList<ArrayList<Location>> row=new ArrayList<>();  //行
        ArrayList<Location> col=new ArrayList<>();  //
        col.add(new Location());
        row.add(col);

        for(int n=0;n<traj.size();n++){
            boolean same=false;  //是否在矩阵中已存在
            for(int i=0;i<row.size();i++){   //i表示第几列
                if(row.get(i).get(0).equals(traj.get(n))){    //已存在该点则计数+1
                    row.get(i).get(1).setTimes(row.get(i).get(1).getTimes()+1);
                    same=true;
                }
            }
            if(!same){   //不存在时添加这个点
                col=new ArrayList<>();
                col.add(traj.get(n));
                col.add(new Location(1));
                row.add(col);
            }

            //进行预测，将结果点添加到预测轨迹
            int max=row.get(1).get(1).getTimes();
            int index=1;
            for(int i=1;i<row.size()-1;i++){
                int temp=row.get(i+1).get(1).getTimes();
                if(temp>max){
                    max=temp;
                    index=i+1;
                }
            }
            Location prediction=new Location(row.get(index).get(0).getP());
            result.add(prediction);


            /**
             * 淘汰老旧的位置点
             * 暂时取轨迹总长度的一半为有效点的数量
             */
            if(n==traj.size()-1){
                return result;
            }

            if(!traj.get(n+1).equals(result.get(n))){
                for(int i=0;i<row.size();i++){   //i表示第几列
                    if(row.get(i).get(0).equals(traj.get(n))){
                        row.get(i).get(1).setTimes(row.get(i).get(1).getTimes()-s);
                        if(row.get(i).get(1).getTimes()<0){
                            row.get(i).get(1).setTimes(0);
                        }
                    }
                }
            }

        }
        return result;
    }


    /**
     * 一阶markov预测
     * @param traj
     * @return
     */
    public ArrayList<Location> predict_order1(List<Location> traj,int s){
        ArrayList<Location> result=new ArrayList<>();

        //生成零阶预测结果
        ArrayList<Location> order0=predict_order0(traj,s);
        result.add(order0.get(0));

        //初始化markov矩阵
        ArrayList<ArrayList<Location>> row=new ArrayList<>();  //行
        ArrayList<Location> col=new ArrayList<>();  //context列
        col.add(new Location());
        row.add(col);

        //将第一个点载入矩阵
        col=new ArrayList<Location>();
        col.add(traj.get(0));
        row.add(col);

        //从第二个点开始遍历轨迹
        for(int n=1;n<traj.size();n++){

            //查看该点是否出现过
            boolean point_exist=false;
            int index_row=0;
            for(int j=1;j<row.size();j++){    //遍历矩阵中已出现的点，即第一行
                if(traj.get(n).equals(row.get(j).get(0))){   //若已出现过该点
                    point_exist=true;
                    index_row=j;
                }
            }
            if(!point_exist){   //矩阵中不存在该点时，为其新增一列
                col=new ArrayList<Location>();
                col.add(traj.get(n));
                for(int i=1;i<row.get(0).size();i++){
                    col.add(new Location(0));
                }
                row.add(col);
                index_row=row.size()-1;
            }

            //查看context是否存在
            boolean cont_exist=false;
            int index_col=0;
            for(int i=1;i<row.get(0).size();i++){
                if(row.get(0).get(i).equals(traj.get(n-1))){{  //若其一阶context已存在，则在对应位置+1
                    cont_exist=true;
                    index_col=i;
                    row.get(index_row).get(i).setTimes(row.get(index_row).get(i).getTimes()+1);
                   }
                }
            }
            if(!cont_exist){   //矩阵中不存在该context时，为其新增一行
                row.get(0).add(traj.get(n-1));
                for(int i=1;i<row.size();i++){
                    if(i==index_row){
                        row.get(i).add(new Location(1));
                    }else {
                        row.get(i).add(new Location(0));
                    }
                }
                index_col=row.get(0).size()-1;
            }

            //进行预测
            cont_exist=false;
            for(int i=1;i<row.get(0).size();i++){
                if(row.get(0).get(i).equals(traj.get(n))){
                    cont_exist=true;
                    index_col=i;
                }
            }
            //可以进行预测
            if(cont_exist){
                int max=row.get(1).get(index_col).getTimes();
                int k=1;
                for(int i=1;i<row.size()-1;i++){
                    int temp=row.get(i+1).get(index_col).getTimes();
                    if(temp>max){
                        max=temp;
                        k=i+1;
                    }
                }
                Location prediction=new Location(row.get(k).get(0).getP());
                result.add(prediction);
            }
            //无法进行预测 则降为零阶预测
            else{
                result.add(order0.get(n));
            }

            /*
             * 淘汰老旧的位置点
             * 暂时取轨迹总长度的一半为有效点的数量
             */
            if(n==traj.size()-1){
                return result;
            }

            if(!traj.get(n+1).equals(result.get(n-1))){
                //消除前缀影响的矩阵坐标
                int index1=0;
                int index2=0;

                //遍历矩阵中已出现的点，即第一行
                for(int j=1;j<row.size();j++){
                    if(row.get(j).get(0).equals(traj.get(n))){
                        index1=j;
                    }
                }

                //遍历矩阵中已出现的context，即第一列
                for(int i=1;i<row.get(0).size();i++){
                    if(row.get(0).get(i).equals(traj.get(n-1))){
                        index2=i;
                    }
                }
                //给本次淘汰点的计数减一
                row.get(index1).get(index2).setTimes(row.get(index1).get(index2).getTimes()-s);
                if(row.get(index1).get(index2).getTimes()<0){
                    row.get(index1).get(index2).setTimes(0);
                }
            }
        }
        return result;
    }


    /**
     * 二阶markov预测
     * @param traj
     * @return
     */
    public ArrayList<Location> predict_order2(List<Location> traj,int s){
        ArrayList<Location> result=new ArrayList<>();

        //生成零阶预测结果
        ArrayList<Location> order0=predict_order0(traj,s);
        //生成1阶预测结果
        ArrayList<Location> order1=predict_order1(traj,s);

        result.add(order0.get(0));
        result.add(order1.get(0));

        //初始化markov矩阵
        ArrayList<ArrayList<Location>> row=new ArrayList<>();  //行
        ArrayList<Location> col=new ArrayList<>();  //context列
        col.add(new Location());
        row.add(col);

        //将前两个点载入矩阵
        col=new ArrayList<Location>();
        col.add(traj.get(0));
        row.add(col);
        col=new ArrayList<Location>();
        col.add(traj.get(1));
        row.add(col);

        //从第三个点开始遍历轨迹
        for(int n=2;n<traj.size();n++){

            //查看该点是否出现过
            boolean point_exist=false;
            int index_row=0;
            for(int j=1;j<row.size();j++){    //遍历矩阵中已出现的点，即第一行
                if(row.get(j).get(0).equals(traj.get(n))){   //若已出现过该点
                    point_exist=true;
                    index_row=j;
                }
            }
            if(!point_exist){   //矩阵中不存在该点时，为其新增一列
                col=new ArrayList<Location>();
                col.add(traj.get(n));
                for(int i=1;i<row.get(0).size();i++){
                    col.add(new Location(0));
                }
                row.add(col);
                index_row=row.size()-1;
            }

            //查看context是否存在
            boolean cont_exist=false;
            int index_col=0;
            for(int i=1;i<row.get(0).size();i++){
                //若其一阶context已存在，则在对应位置+1
                if(row.get(0).get(i).equals(traj.get(n-2))&&row.get(0).get(i).getNext().equals(traj.get(n-1))){{
                    cont_exist=true;
                    index_col=i;
                    row.get(index_row).get(i).setTimes(row.get(index_row).get(i).getTimes()+1);}
                }
            }
            //矩阵中不存在该context时，为其新增一行
            if(!cont_exist){
                Location context=traj.get(n-2);
                row.get(0).add(new Location(context.getP(),traj.get(n-1)));
                for(int i=1;i<row.size();i++){
                    if(i==index_row){
                        row.get(i).add(new Location(1));
                    }else {
                        row.get(i).add(new Location(0));
                    }
                }
                index_col=row.get(0).size()-1;
            }



            //开始预测

            cont_exist=false;
            for(int i=1;i<row.get(0).size();i++){
                if(row.get(0).get(i).equals(traj.get(n-1))&&row.get(0).get(i).getNext().equals(traj.get(n))){
                    cont_exist=true;
                    index_col=i;
                }
            }
            //可以进行预测
            if(cont_exist){
                int max=row.get(1).get(index_col).getTimes();
                int k=1;
                for(int i=1;i<row.size()-1;i++){
                    int temp=row.get(i+1).get(index_col).getTimes();
                    if(temp>max){
                        max=temp;
                        k=i+1;
                    }
                }
                Location prediction=new Location(row.get(k).get(0).getP());
                result.add(prediction);
            }
            //无法进行预测 则逐级降阶
            else{
                result.add(order1.get(n));
            }

            /*
             * 淘汰老旧的位置点
             * 暂时取轨迹总长度的一半为有效点的数量
             */
            if(n==traj.size()-1){
                return result;
            }
            if(!traj.get(n+1).equals(result.get(n-2))){
                //消除前缀影响的矩阵坐标
                int index1=0;
                int index2=0;

                //遍历矩阵中已出现的点，即第一行
                for(int j=1;j<row.size();j++){
                    if(row.get(j).get(0).equals(traj.get(n))){
                        index1=j;
                    }
                }

                //遍历矩阵中已出现的context，即第一列
                for(int i=1;i<row.get(0).size();i++){
                    if(row.get(0).get(i).equals(traj.get(n-2))&&row.get(0).get(i).getNext().equals(traj.get(n-1))){
                        index2=i;
                    }
                }
                //给本次淘汰点的计数减一
                row.get(index1).get(index2).setTimes(row.get(index1).get(index2).getTimes()-1);
                if(row.get(index1).get(index2).getTimes()<0){
                    row.get(index1).get(index2).setTimes(0);
                }
            }
        }
        return result;
    }

    public ArrayList<Location> predict_order3(List<Location> traj,int s){
        ArrayList<Location> result=new ArrayList<>();

        //生成零阶预测结果
        ArrayList<Location> order0=predict_order0(traj,s);
        //生成1阶预测结果
        ArrayList<Location> order1=predict_order1(traj,s);
        //生成2阶预测结果
        ArrayList<Location> order2=predict_order2(traj,s);

        result.add(order0.get(0));
        result.add(order1.get(0));
        result.add(order2.get(0));

        //初始化markov矩阵
        ArrayList<ArrayList<Location>> row=new ArrayList<>();  //行
        ArrayList<Location> col=new ArrayList<>();  //context列
        col.add(new Location());
        row.add(col);

        //将前三个点载入矩阵
        col=new ArrayList<Location>();
        col.add(traj.get(0));
        row.add(col);
        col=new ArrayList<Location>();
        col.add(traj.get(1));
        row.add(col);
        col=new ArrayList<Location>();
        col.add(traj.get(2));
        row.add(col);

        //从第四个点开始遍历轨迹
        for(int n=3;n<traj.size();n++){

            //查看该点是否出现过
            boolean point_exist=false;
            int index_row=0;
            for(int j=1;j<row.size();j++){    //遍历矩阵中已出现的点，即第一行
                if(row.get(j).get(0).equals(traj.get(n))){   //若已出现过该点
                    point_exist=true;
                    index_row=j;
                }
            }
            if(!point_exist){   //矩阵中不存在该点时，为其新增一列
                col=new ArrayList<Location>();
                col.add(traj.get(n));
                for(int i=1;i<row.get(0).size();i++){
                    col.add(new Location(0));
                }
                row.add(col);
                index_row=row.size()-1;
            }

            //查看context是否存在
            boolean cont_exist=false;
            int index_col=0;
            for(int i=1;i<row.get(0).size();i++){
                //若其一阶context已存在，则在对应位置+1
                if(row.get(0).get(i).equals(traj.get(n-3))&&row.get(0).get(i).getNext().equals(traj.get(n-2))&&row.get(0).get(i).getNext().getNext().equals(traj.get(n-1))){{
                    cont_exist=true;
                    index_col=i;
                    row.get(index_row).get(i).setTimes(row.get(index_row).get(i).getTimes()+1);}
                }
            }
            //矩阵中不存在该context时，为其新增一行
            if(!cont_exist){
                Location context=traj.get(n-3);
                row.get(0).add(new Location(context.getP(),new Location(traj.get(n-2).getP(),traj.get(n-1))));
                for(int i=1;i<row.size();i++){
                    if(i==index_row){
                        row.get(i).add(new Location(1));
                    }else {
                        row.get(i).add(new Location(0));
                    }
                }
                index_col=row.get(0).size()-1;
            }

            //开始预测

            cont_exist=false;
            for(int i=1;i<row.get(0).size();i++){
                if(row.get(0).get(i).equals(traj.get(n-2))&&row.get(0).get(i).getNext().equals(traj.get(n-1))&&row.get(0).get(i).getNext().equals(traj.get(n))){
                    cont_exist=true;
                    index_col=i;
                }
            }
            //可以进行预测
            if(cont_exist){
                int max=row.get(1).get(index_col).getTimes();
                int k=1;
                for(int i=1;i<row.size()-1;i++){
                    int temp=row.get(i+1).get(index_col).getTimes();
                    if(temp>max){
                        max=temp;
                        k=i+1;
                    }
                }
                Location prediction=new Location(row.get(k).get(0).getP());
                result.add(prediction);
            }
            //无法进行预测 则逐级降阶
            else{
                result.add(order2.get(n));
            }

            /*
             * 淘汰老旧的位置点
             * 暂时取轨迹总长度的一半为有效点的数量
             */
            if(n==traj.size()-1){
                return result;
            }
            if(!traj.get(n+1).equals(result.get(n-3))){
                //消除前缀影响的矩阵坐标
                int index1=0;
                int index2=0;

                //遍历矩阵中已出现的点，即第一行
                for(int j=1;j<row.size();j++){
                    if(row.get(j).get(0).equals(traj.get(n))){
                        index1=j;
                    }
                }

                //遍历矩阵中已出现的context，即第一列
                for(int i=1;i<row.get(0).size();i++){
                    if(row.get(0).get(i).equals(traj.get(n-3))&&row.get(0).get(i).getNext().equals(traj.get(n-2))&&row.get(0).get(i).getNext().getNext().equals(traj.get(n-1))){
                        index2=i;
                    }
                }
                //给本次淘汰点的计数减一
                row.get(index1).get(index2).setTimes(row.get(index1).get(index2).getTimes()-1);
                if(row.get(index1).get(index2).getTimes()<0){
                    row.get(index1).get(index2).setTimes(0);
                }
            }
        }
        return result;
    }
}
