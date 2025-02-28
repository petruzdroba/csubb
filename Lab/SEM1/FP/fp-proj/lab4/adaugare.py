import date_participanti


def adaugare_scor_ultim_participant(participants_list):
    pass
    print("Adaugare nou participant")
    nume_participant_nou = input("Numele noului participant \n >>>")
    
    date_participanti.adaugare_participant_nou(participants_list, nume_participant_nou)
    
    scor_nou = int(input("Scorul nou "))
    
    # participants_list[len(participants_list) - 1]["scor"].append(scor_nou)
    
    for index in range(len(participants_list[len(participants_list) - 1]["scor"])):
        
        if not participants_list[len(participants_list) - 1]["scor"][index]:
            
            participants_list[len(participants_list) - 1]["scor"][index] = scor_nou
            break
        
    print(f"Scorurile pentru participantul {participants_list[len(participants_list) - 1]["nume"]}")
    
    date_participanti.print_scor_participant(participants_list, len(participants_list) - 1)
        
def inserare_scor_participant(participants_list):
    contestant = input("Cui doriti sa inserati un scor? \n")
    
    is_player = False
    save_index = -1
    
    for index in range(len(participants_list)):
        if contestant in participants_list[index]["nume"]:
            is_player = True
            save_index = index
    
    if not is_player:
        print(f"\n \n{contestant} nu a fost gasit in lista de participanti \n \n \n")
    else:
        contest_number = int(input("La ce proba doriti sa inserati scorul (1-10) ? \n"))
        if contest_number < 1 and contest_number > 10:
            print("Concursul are doar 10 probe")
        else:
            scor_nou = int(input("Ce scor doriti sa inserati ? "))
            participants_list[save_index]["scor"][contest_number] = scor_nou

            date_participanti.print_scor_participant(participants_list,save_index)
            
        