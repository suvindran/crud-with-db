package example.in.foodconnectdb;

/**
 * Created by Acer on 31-07-2017.
 */

public class Model {
    String foodname;
    int id;
    Double rate;

    public Model(String foodname, int id, Double rate) {
        this.foodname = foodname;
        this.id = id;
        this.rate = rate;
    }

    public Model() {

    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getFoodname() {
        return foodname;
    }

    public int getId() {
        return id;
    }

    public Double getRate() {
        return rate;
    }
}

/**
 * data types
 * integer,double,boolean,string,float;
 * integer example:10,20;
 * double example:10.00;
 * boolean example:true,false;
 * string examlpe:character,text;
 * float example:.0
 */