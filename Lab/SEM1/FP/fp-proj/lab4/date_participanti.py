# {'name', 'nr_concurs', '[scor1, scor 2 etc]'}

def read_participants_data(participants_list):
    data_file = open('file_data_participanti.txt', 'r')
    
    
    id_assigner = 0
    for line in data_file:
        user_model = {
            'key_id': id_assigner, 
            'nume': line.strip(), 
            'scor': [f"id:{id_assigner}",0,0,0,0,0,0,0,0,0,0]  
        }
        id_assigner += 1
        participants_list.append(user_model)
        
# def get_participants_data():
#     return participants_list

def print_participants_data(participants_list):
    print(participants_list)

def print_nume_participanti_lista(participants_list):
    numerator = 1
    
    for contestant in participants_list:
        print(f"{numerator}. {contestant["nume"]}")
        numerator += 1


def print_scor_participant(participants_list, index):
    print("Dupa inserarea noului scor :")
    scoruri_noi = []
    for i in range(1, 11):
        scoruri_noi.append(participants_list[index]["scor"][i])
    print(scoruri_noi)

def adaugare_participant_nou(participants_list, nume):
    user_model = {
        'key_id': len(participants_list),
        'nume': nume,
        'scor': [f"id:{len(participants_list)}",0,0,0,0,0,0,0,0,0,0]  
    }
    participants_list.append(user_model)
    
    data_file = open('file_data_participanti.txt', 'a')
    data_file.write(f"\n{nume}")
    data_file.close()
    data_file = open('file_data_participanti.txt', 'r')
