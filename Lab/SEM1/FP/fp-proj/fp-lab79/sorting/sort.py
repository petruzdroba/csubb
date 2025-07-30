from typing import Iterable


class Sort(object):
    def __init__(self):
        pass

    def comb_sort(self, iterable: Iterable, cmp, reversed: bool = False) -> Iterable:
        arr = iterable

        lenght = len(arr)
        gap = lenght
        do_it_again = True

        while gap != 1 or do_it_again == True:
            gap = max(1, int(gap / 1.3))
            do_it_again = False

            for index in range(0, lenght - gap):
                if cmp(arr[index], arr[index + gap]):
                    arr[index], arr[index + gap] = arr[index + gap], arr[index]
                    do_it_again = True

        if reversed:
            arr.reverse()
        return arr

    def insertion_sort(
        self, iterable: Iterable, cmp, reversed: bool = False
    ) -> Iterable:
        arr = iterable

        for index in range(1, len(arr)):
            pole = arr[index]
            jndex = index - 1

            while jndex >= 0 and cmp(arr[jndex], pole):
                arr[jndex + 1] = arr[jndex]
                jndex -= 1
            arr[jndex + 1] = pole

        if reversed:
            arr.reverse()
        return arr
