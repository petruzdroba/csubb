import domain.Vehicle;
import repository.VehicleRepository;
import utils.MileageUnit;

public class Application {

	public static void main(String[] args) {
		//TODO - instantiate a vehicle
        Vehicle vehicle = new Vehicle("CJ 01 BOS", 0.1, 2001, MileageUnit.KM);
		
		VehicleRepository repository = new VehicleRepository();
		repository.addVehicle(vehicle);
	
		for (int i = 0; i < repository.getNumberOfVehicles(); i++) {
			Vehicle retrievedVehicle = repository.getVehicleAtPosition(i);
			//TODO -print vehicle details for retrievedVehicle object
            retrievedVehicle.printVehicleDetails();
		}
	}

}
