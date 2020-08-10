package model;

public class Dish {
    private int dishid;
    private String name ;
    private int price;

    public int getDishid() {
        return dishid;
    }

    public void setDishid(int dishid) {
        this.dishid = dishid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "dishid=" + dishid +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
