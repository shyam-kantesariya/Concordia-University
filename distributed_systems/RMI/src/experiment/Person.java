package experiment;

/**
 * Created by Sparta on 5/29/2017.
 */
import java.io.Serializable;

public class Person implements Serializable{
    String f_name, l_name;
    int id;
    Person(int id, String f_name, String l_name){
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
    }
}
