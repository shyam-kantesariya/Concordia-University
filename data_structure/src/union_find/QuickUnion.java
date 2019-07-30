package union_find;

public class QuickUnion {
    int[] elements;

    public QuickUnion(int n){
        elements = new int[n];
        initialize();
    }

    public void initialize(){
        for(int i = 0; i<elements.length; i++){
            elements[i] = i;
        }
    }

    private int root(int x){
        while(elements[x] != x) {
            x=elements[x];
        }
        return x;
    }

    public void union(int x, int y){
        int i = root(x);
        int j = root(y);
        elements[i] = j;
    }

    public boolean find(int x, int y){
        return root(x) == root(y);
    }

    public void print(){
        for(int i=0; i<elements.length; i++){
            System.out.println(elements[i]);
        }
    }
}