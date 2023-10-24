package model;
/**
 * Supplied class Inventory.java
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * @author Ryan.Slack
 * JavaDoc route: file:///C:/Users/RyanG/Desktop/Software%20Submission/JavaDocs/index.html
 */

public class Inventory {
    /**
     * Creates observable array list of all parts
     */
    private static final ObservableList<Part>  allParts = FXCollections.observableArrayList();
    /**
     * Creates observable array list of all products
     */
    private static final ObservableList<Product>  allProducts = FXCollections.observableArrayList();

    /**
     * @param part addPart
     */
    public static void addPart(Part part) {allParts.add(part); }

    /**
     * @param product addProduct
     */
    public static void addProduct(Product product) {allProducts.add(product); }

    /**
     * @param part lookupPart
     */
    public static void lookupPart(Part part){ allParts.get(part.getId()); }

    /**
     * @param product lookupProduct
     */
    public static void lookupProduct(Product product){ allProducts.get(product.getId()); }

    /**
     * @return allParts
     */
    public static ObservableList<Part> lookupPart() { return allParts; }

    /**@return allProducts*/
    public static ObservableList<Product> lookupProduct() { return allProducts; }

    /**
     * @param part updatePart
     */
    public static void updatePart(Part part){ allParts.add(part); }
    /**@param id partID
     * @param name partName
     * @param stock partInv
     * @param price partPrice
     * @param max partMax
     * @param min partMin
     */
    public static void updateProduct(int id, String name, double price, int stock, int min, int max) {
        Product product= new Product(id, name, price, stock, min, max);
        allProducts.add(product); }

    /**
     * @param part deletePart
     */
    public static void deletePart(Part part){ allParts.remove(part); }

    /**
     * @param product deleteProduct
     */
    public static void deleteProduct(Product product){ allProducts.remove(product); }

    /**
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() { return allParts; }

    /**
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts(){ return allProducts; }
}




