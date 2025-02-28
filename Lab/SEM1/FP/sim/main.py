from teste import Teste
from ui import Ui
from service import Service
from repo import Repository


test = Teste()

test.run_all_tests()

repo = Repository("mobila.txt")
service = Service(repo)
console = Ui(service)

console.run()
