from tester import Tester
from repo import Repository
from service import Service
from ui import UI
from validator import Validator


tests = Tester()
tests.run_all_tests()

repo = Repository("sedinte.txt")
validator = Validator()
service = Service(repo, validator)
ui = UI(service)

ui.run()
