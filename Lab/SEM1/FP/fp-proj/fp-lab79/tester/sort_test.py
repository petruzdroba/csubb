from sorting.sort import Sort
import unittest


class SortTest(unittest.TestCase):
    def __init__(self, methodName="runTest"):
        super().__init__(methodName)
        self.sort_module = Sort()

    def run_all_tests(self):
        self.__test_insertion_sort()

    def __test_insertion_sort(self):
        test_number_list = [9, 1, 2, 3, 4]

        sorted_array = self.sort_module.insertion_sort(
            test_number_list, lambda nr1, nr2: nr1 > nr2
        )

        self.assertEqual(sorted_array, [1, 2, 3, 4, 9])

    def __test_comb_sort(self):
        test_number_list = [9, 1, 2, 3, 4]

        sorted_array = self.sort_module.comb_sort(
            test_number_list, lambda nr1, nr2: nr1 > nr2
        )

        self.assertEqual(sorted_array, [1, 2, 3, 4, 9])
