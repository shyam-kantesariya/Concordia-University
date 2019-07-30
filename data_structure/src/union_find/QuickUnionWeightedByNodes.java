package union_find;

public class QuickUnionWeightedByNodes {
    int[] elements;
    int[] size;

    public QuickUnionWeightedByNodes(int n){
        elements = new int[n];
        size = new int[n];
        initialize();
    }

    public void initialize(){
        for(int i = 0; i<elements.length; i++){
            elements[i] = i;
            size[i] = 1;
        }
    }

    private int root(int x){
        while(elements[x] != x) x=elements[x];
        return x;
    }

    public void union(int x, int y){
        int i = root(x);
        int j = root(y);
        if (size[i] > size[j]) {
            elements[j] = i;
            size[i] += size[j];
        } else {
            elements[i] = j;
            size[j] += size[i];
        }
    }

    public boolean find(int x, int y){
        return root(x) == root(y);
    }
}
