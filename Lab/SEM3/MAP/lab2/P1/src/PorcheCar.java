public class PorcheCar extends  Car{
    private String model;

    public PorcheCar(int year, double price, String model) {
        super(year, price);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString(){
        return super.toString() + " " + model;
    }
}
