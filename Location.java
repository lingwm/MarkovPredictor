/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class Location {



    String p;
    int times;
    Location next;


    public Location(){}

    public Location(String str){
        p=str;
    }

    /**
     * 二阶context
     */
    public Location(String str,Location l){
        p=str;
        next=l;
    }

    /**
     * 计数点
     */
    public Location(int a){
        times=a;
    }

    /**
     * 判断相等
     * @param a
     * @return
     */
    public boolean equals(Location a){
        if(this.p==null){
            return false;
        }
        if(this.p.equals(a.getP())){
            return true;
        }else {
            return false;
        }
    }



    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Location getNext() {
        return next;
    }

    public void setNext(Location next) {
        this.next = next;
    }

}
