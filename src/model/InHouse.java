package model;
/**
 * Supplied class InHouse.java
 */

/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class InHouse extends Part{

    private int machineId;

    public InHouse (int id, String name, double price, int stock,int min, int  max,int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /**
     * @param machineId the machineID name to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
    /**
     * @return companyName
     */
    public int getMachineId(){
        return machineId;
    }

}
