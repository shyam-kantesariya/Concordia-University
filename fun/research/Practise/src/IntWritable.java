public class IntWritable implements PrimitiveType<Integer> {
    int val;
    IntWritable(int someVal){
        this.val = someVal;
    }
    IntWritable(){
        this.val = 0;
    }

    public void set(Integer x){
        this.val = x.intValue();
    }

    public void set(int x){
        this.set(new Integer(x));
    }

    public Integer get(){
        return this.val;
    }
}
