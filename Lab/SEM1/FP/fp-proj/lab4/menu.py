import adaugare
import stergere

def main_menu_console(participants_list):
    print("1. Adauga un scor la un participant")
    print("2. Modificare scor")
    print("3. Tipareste lista de participanti")
    print("4. Operatii pe un subset de participanti")
    print("5. Filtrare")
    print("6. Undo")
    print("7. Exit")
    
    choice = int(input(">>>"))
    if choice == 1:
        menu_console_1(participants_list)
        main_menu_console(participants_list)
    elif choice == 2:
        menu_console_2(participants_list)
        main_menu_console(participants_list)
    elif choice == 3:
        pass
    elif choice == 4:
        pass
    elif choice == 5:
        pass
    elif choice == 6:
        pass
    else:
        return 0
    
def menu_console_1(participants_list):
    print("1. Adauga scor ultimul participant")
    print("2. Inserare scor participant")
    print("3. Exit")
    choice = int(input(">>>"))
    if choice == 1:
        adaugare.adaugare_scor_ultim_participant(participants_list)
    elif choice == 2:
        adaugare.inserare_scor_participant(participants_list)
    else:
        return 0
    
def menu_console_2(participants_list):
    print("1. Sterge scorul pentru un participant")
    print("2. Sterge scorul pentru un interval de participanti")
    print("3. Modifica scorul pentru un participant")
    print("4. Exit")
    
    choice = int(input(">>>"))
    
    if choice == 1:
        stergere.sterge_scor_participant(participants_list)
    elif choice == 2:
        stergere.sterge_interval_scoruri(participants_list)
    elif choice == 3:
        pass
    else:
        return 0