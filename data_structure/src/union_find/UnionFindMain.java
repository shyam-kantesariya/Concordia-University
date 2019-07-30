package union_find;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UnionFindMain {
    public static void main(String[] args){
        UnionFindMain main = new UnionFindMain();
        //main.executeQuickFind();
        main.executeQuickUnion();
    }
    public void execute(String name, int n){
        Object qf;
        try {
            Constructor c = Class.forName(name).getConstructor(int.class);
            qf = c.newInstance(n);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        qf.union(0,1);
        qf.union(0,3);
        qf.union(3,7);
        qf.print();
        System.out.println(qf.find(1,8));
    }

    public void executeQuickUnion(){
        QuickUnion qf = new QuickUnion(10);
        qf.union(0,1);
        qf.union(0,3);
        qf.union(3,7);
        qf.print();
        System.out.println(qf.find(1,8));
    }
    public void executeQuickUnionWeightedByNodes(String name){
        Object qf = Class.forName(name);
        qf.union(0,1);
        qf.union(0,3);
        qf.union(3,7);
        qf.print();
        System.out.println(qf.find(1,8));
    }
}
