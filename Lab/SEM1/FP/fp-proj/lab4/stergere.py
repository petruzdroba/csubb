import date_participanti


def sterge_scor_participant(participants_list):
    contestant = input("Cui doriti sa ii stergeti scorul? \n")
    
    is_player = False
    save_index = -1
    
    for index in range(len(participants_list)):
        if contestant in participants_list[index]["nume"]:
            is_player = True
            save_index = index
    
    if not is_player:
        print(f"\n \n{contestant} nu a fost gasit in lista de participanti \n \n \n")
    else:
        #se inlocuieste fiecare scor cu valoarea 0
        for index in range(1,11):
            participants_list[save_index]["scor"][index] = 0
        
        date_participanti.print_scor_participant(participants_list,save_index)
        

def sterge_interval_scoruri(participants_list):
    print("\n\n\n Numele particpantilor")
    date_participanti.print_nume_participanti_lista(participants_list)
    print("\n\n\n")
    
    begin = int(input("Introduceti inceputul intervalului: \n>>>"))
    end = int(input("Indroduceti finalul intervalului: \n>>>"))
    
    #daca datele introduse trec peste limite, atunci ele vor fi setate cu marginea inferioara/superioara
    if begin < 1:
        begin = 1
        
    if end > len(participants_list):
        end = len(participants_list)
        
    for index in range(begin, end):
        for cont_index in range(1,11):
            participants_list[index]["scor"][cont_index] = 0
        # date_participanti.print_scor_participant(index)