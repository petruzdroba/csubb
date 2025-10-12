public class AudiCar  extends  Car{

    private String market;

    public AudiCar(int year, double price, String market) {
        super(year, price);
        this.market = market;
    }

    @Override
    public String toString(){
        return super.toString() + " " + market;
    }
}
