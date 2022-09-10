public class Users {

    private String role;
    private int roleId;
    private double quantity;
    private double price;

    public Users(String role, int roleId, double quantity, double price) {
        this.role = role;
        this.roleId = roleId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return roleId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return role + roleId + "\tQuantity: " + quantity + "\tPrice: " + price;
    }
}
