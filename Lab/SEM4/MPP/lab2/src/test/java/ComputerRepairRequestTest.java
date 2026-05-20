import model.ComputerRepairRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.RequestRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputerRepairRequestTest {
    @Test
    @DisplayName("Model Test")
    public void test1(){
        ComputerRepairRequest crr = new ComputerRepairRequest(1, "1","1", "1","1","1","1");
        assertEquals(1, crr.getID());
        assertEquals("1", crr.getOwnerName());
        assertEquals("1", crr.getOwnerAddress());
        assertEquals("1", crr.getDate());
        assertEquals("1", crr.getModel());
        assertEquals("1", crr.getPhoneNumber());
        assertEquals("1", crr.getProblemDescription());
    }

    @Test
    @DisplayName("Repo Test")
    public void test2(){
        RequestRepository repo = new RequestRepository();
        ComputerRepairRequest crr = new ComputerRepairRequest(1, "1", "1", "1", "1", "1", "1");
        repo.add(crr);
        assertEquals(crr, repo.findById(1));
    }
}
