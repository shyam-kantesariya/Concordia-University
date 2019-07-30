import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Reducer obj = new Reducer<IntWritable, IntWritable>();
        List<Integer> abc = new LinkedList<Integer>();
        abc.add(new Integer(2));
        abc.add(new Integer(3));
        obj.reduce(new IntWritable(5), abc);
    }
}