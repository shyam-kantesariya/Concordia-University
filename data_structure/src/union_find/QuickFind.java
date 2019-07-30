package union_find;

public class QuickFind {
    int[] elements;

    public QuickFind(int n){
        elements = new int[n];
        initialize();
    }

    public void initialize(){
        for(int i = 0; i<elements.length; i++){
            elements[i] = i;
        }
    }

    public void union(int x, int y){
        for(int i=0; i<elements.length; i++){
            if(elements[i] == x) elements[i] = y;
        }
    }

    public boolean find(int i, int j){
        if (elements[i] == elements[j])
            return true;
        else
            return false;
    }

    public void print(){
        for(int i=0; i<elements.length; i++){
            System.out.println(elements[i]);
        }
    }
}
