public class Application{
    public static void main(String[] args){
        Car car = new Car(2020,33.3);
        AudiCar audiCar =   new AudiCar(2014, 5000.1, "Germania");
        PorcheCar porcheCar = new PorcheCar(2019, 6600, "911");
        PorcheCar anotherPorcheCar  = new PorcheCar(2019, 6600, "911");

        System.out.println(car.toString());
        System.out.println(audiCar.toString());
        System.out.println(porcheCar.toString());
        System.out.println(anotherPorcheCar);


//        porcheCar = audiCar;
//        System.out.println(porcheCar.toString());


    }
}