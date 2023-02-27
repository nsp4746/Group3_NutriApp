import java.sql.Time;

public class User {
    private String name;
    private double height;
    private double weight;
    private Time birthdate;
    private int age;
    //private Goal goal;
    private Observer observer;
    
    public User(String name, double height, double weight, Time birthdate, int age){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.birthdate = birthdate;
        this.age = age;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setWeight(double weight){
        this.weight = weight;
        //check weight against goal
        //if goal achieved, notify
    }

    /*
     * public void setGoal(Goal goal){
     *      this.goal = goal;
     * }
     */

    public void subscribe(Observer observer){
        this.observer = observer;
    }

    public void notifyObserver(){
        //notify the observer of the new weight
    }
}