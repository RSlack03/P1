package model;
/**
 * Supplied class Outsourced.java
 */

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class Outsourced extends Part{
    private String companyName;

    public Outsourced (int id, String name, double price, int stock,int min, int  max,String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getCompanyName(){
        return companyName;
    }


}
