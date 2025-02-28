from tester.test_vehicul import Test_Vehicul

from ui.console import UI
from service.vehicul_service import Vehicul_Service
from repository.vehicul_repo import Vehicul_Repo
from validator.data_validator import Data_Validator
from validator.vehicul_validator import Vehicul_Validaor

repository = Vehicul_Repo("vehicule.txt")
data = Data_Validator()
vehicul_v = Vehicul_Validaor()
service = Vehicul_Service(repository, vehicul_v, data)
ui = UI(service)

test = Test_Vehicul()
test.run_all_tests()

ui.run()
