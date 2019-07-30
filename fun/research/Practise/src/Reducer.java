
public class Reducer<A extends PrimitiveType,B extends PrimitiveType> {
    public void reduce(A key, Iterable<B> valz){
        int sum=0;
        for(B val:valz){
            sum += (int)val.get();
        }
        System.out.println("Output " + key.get() + " : " + sum);
    }
}
