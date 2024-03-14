import sys
try:
    alp_list = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]

    out_file = open("Battleship.out", "w")

    txt1error = False
    try:
        input_file1 = open(sys.argv[1], "r")   # Player1.txt inputs
        read1 = input_file1.readlines()
        modified1 = []  # this is a list of input lines of player1  [';;;;;;C;;;', ';;;;B;;C;;;',  ...
        for line1 in read1:
            if line1[-1] == "\n":
                modified1.append(line1[:-1])
            else:
                modified1.append(line1)
    except IOError:
        txt1error = True
        print("IOError! These input file(s) are not reachable:", sys.argv[1], end=" ")
        out_file.write(f"IOError! These input file(s) are not reachable: {sys.argv[1]} ")

    txt2error = False
    try:
        input_file2 = open(sys.argv[2], "r")  # Player2.txt inputs
        read2 = input_file2.readlines()
        modified2 = []  # this is a list of input lines of player2
        for line2 in read2:
            if line2[-1] == "\n":
                modified2.append(line2[:-1])
            else:
                modified2.append(line2)
    except IOError:
        txt2error = True
        if txt1error:
            print(sys.argv[2], end=" ")
            out_file.write(f"{sys.argv[2]} ")
        else:
            print("IOError! These input file(s) are not reachable:", sys.argv[2], end=" ")
            out_file.write(f"IOError! These input file(s) are not reachable: {sys.argv[2]} ")
    # this is a list of input lines of player2 [';;;;;;;;;S', 'D;;C;C;C;C;C;;;S', ...


    in1error = False
    try:
        global input_file1_1
        input_file1_1 = open(sys.argv[3], "r")  # Player1.in

    except IOError:
        in1error = True
        if txt2error or txt1error:
            print(sys.argv[3], end=" ")
            out_file.write(f"{sys.argv[3]}")
        else:
            print("IOError! These input file(s) are not reachable:", sys.argv[3], end=" ")
            out_file.write(f"IOError! These input file(s) are not reachable: {sys.argv[3]} ")


    try:
        global input_file2_2
        input_file2_2 = open(sys.argv[4], "r")  # Player2.in
    except IOError:
        if txt2error or txt1error or in1error:
            print(sys.argv[4], end="\n")
            out_file.write(f"{sys.argv[4]}\n")
            quit()
        else:
            print("IOError! These input file(s) are not reachable:", sys.argv[4], end="\n")
            out_file.write(f"IOError! These input file(s) are not reachable: {sys.argv[4]}\n")
            quit()

    read1_1 = input_file1_1.readlines()  # Player1.txt inputs
    moves1 = read1_1[0].split(";")  # ['5,E', '10,G', '8,I', '4,C', '8,F', ...   # the moves of player1 (p1)

    read2_2 = input_file2_2.readlines()  # Player2.txt inputs
    moves2 = read2_2[0].split(";")  # ['1,J', '6,E', '8,I', '6,I', '8,F', '7,J', ...  # the moves of player2 (p2)


    p1_dict = {}  # {'A1': '-', 'A2': '-', 'A3': '-', 'A4': '-', 'A5': '-', 'A6': '-', 'A7': '-', 'A8': '-', 'A9': '-', 'A10': '-', 'B1': '-', 'B2': '-', 'B3':
    for alp in alp_list:
        for num in range(1, 11):
            p1_dict.update({alp + str(num): "-"})


    p2_dict = {} # {'A1': '-', 'A2': '-', 'A3': '-', 'A4': '-', 'A5': '-', 'A6': '-', 'A7': '-', 'A8': '-', 'A9': '-', 'A10': '-', 'B1': '-', 'B2': '-'
    for alp2 in alp_list:
        for num2 in range(1, 11):
            p2_dict.update({alp2 + str(num2): "-"})


    big_list1 = []   # [[';', ';', ';', ';', ';', ';', 'C', ';', ';', ';'], [';', ';', ';', ';', 'B', ';', ';', 'C', ';', ';', ';'],
    for lin1 in modified1:
        lin1_list = []
        for i in lin1:
            lin1_list.append(i)
        if lin1_list not in big_list1:
            big_list1.append(lin1_list)
        else:
            lin1_list.append(1)
            big_list1.append(lin1_list)


    row1_counter = 0
    for row1 in big_list1:    # Now, after the info from txt files have been taken, here dictionaries are updated. each key will be assigned to a character which indicates the status
        column_counter1 = 0   # of the square
        for column1 in big_list1[row1_counter]:
            if big_list1[row1_counter][column_counter1] != ";" and type(big_list1[row1_counter][column_counter1]) == str:
                how_many_comma1 = big_list1[row1_counter][:column_counter1].count(";")
                p1_dict[alp_list[how_many_comma1] + str(big_list1.index(big_list1[row1_counter]) + 1)] = \
                big_list1[row1_counter][column_counter1]
            column_counter1 += 1
        row1_counter += 1

    big_list2 = []  # [[';', ';', ';', ';', ';', ';', ';', ';', ';', 'S'], ['D', ';', ';', 'C', ';', 'C', ';', 'C', ';', 'C', ';', 'C', ';', ';', ';', 'S'],
    for lin2 in modified2:
        lin2_list = []
        for i in lin2:
            lin2_list.append(i)
        if lin2_list not in big_list2:
            big_list2.append(lin2_list)
        else:
            lin2_list.append(1)
            big_list2.append(lin2_list)


    for row2 in big_list2:
        column_counter2 = 0
        for column2 in row2:
            if row2[column_counter2] != ";" and type(row2[column_counter2]) == str:
                how_many_comma2 = row2[:column_counter2].count(";")
                p2_dict[alp_list[how_many_comma2] + str(big_list2.index(row2) + 1)] = row2[column_counter2]
            column_counter2 += 1

    p1_ships = {}
    p2_ships = {}

    def ship_finder(dictt):
        p1_dict_copy = dictt.copy()
        for elem in p1_dict_copy:
            if p1_dict_copy[elem] != "P":
                p1_dict_copy[elem] = "-"

        list_of_keys_for_pbs = list(dictt.copy())
        values_of_keys_for_pbs = []  # ['A1', 'A2', 'A3', 'A4', 'A5', 'A6', 'A7',
        for element in list_of_keys_for_pbs:
            values_of_keys_for_pbs.append(p1_dict_copy[element])

        carrier = []  #the elements of this list will be deleted everytime a part of this is hit
        destroyer = []
        submarine = []
        battleships = []  #inside it, for every empty list a "X" will appear
        b_coords = []
        patrol_boats = [] # similar to battleships
        patb_coords = []

        list_of_keys = list(dictt)
        for pos in list_of_keys:
            index_pos = list_of_keys.index(pos)
            if index_pos < 90:    # if we are not in the right edge of the board
                below_one = list_of_keys[index_pos + 1]
                right_one = list_of_keys[index_pos + 10]
                if dictt[pos] == "C":  # if we came across a "C" on board
                    if pos in carrier:
                        pass
                    else:
                        if dictt[right_one] != "C":
                            for i in range(5):
                                carrier.append(list_of_keys[index_pos + i])
                        elif dictt[below_one] != "C":
                            for i in range(5):
                                carrier.append(list_of_keys[index_pos + 10 * i])
                elif dictt[pos] == "D":
                    if pos in destroyer:
                        pass
                    else:
                        if dictt[right_one] != "D":
                            for i in range(3):
                                destroyer.append(list_of_keys[index_pos + i])
                        elif dictt[below_one] != "D":
                            for i in range(3):
                                destroyer.append(list_of_keys[index_pos + 10 * i])
                elif dictt[pos] == "S":
                    if pos in submarine:
                        pass
                    else:
                        if dictt[right_one] != "S":
                            for i in range(3):
                                submarine.append(list_of_keys[index_pos + i])
                        elif dictt[below_one] != "S":
                            for i in range(3):
                                submarine.append(list_of_keys[index_pos + 10 * i])
                elif dictt[pos] == "B":
                    battleship = []
                    if pos in b_coords:
                        pass
                    else:
                        if dictt[right_one] != "B":
                            for i in range(4):
                                b_coords.append(list_of_keys[index_pos + i])
                                battleship.append(list_of_keys[index_pos + i])
                            battleships.append(battleship)
                        elif dictt[below_one] != "B":
                            for i in range(4):
                                b_coords.append(list_of_keys[index_pos + 10 * i])
                                battleship.append(list_of_keys[index_pos + 10 * i])
                            battleships.append(battleship)
                        elif dictt[right_one] == "B" and dictt[below_one] == "B":
                            if dictt[list_of_keys[index_pos + 4]] == "B":
                                for i in range(4):
                                    b_coords.append(list_of_keys[index_pos + 10 * i])
                                    battleship.append(list_of_keys[index_pos + 10 * i])
                                battleships.append(battleship)
                            else:
                                for i in range(4):
                                    b_coords.append(list_of_keys[index_pos + i])
                                    battleship.append(list_of_keys[index_pos + i])
                                battleships.append(battleship)
                elif p1_dict_copy[pos] == "P":
                    starting_index = index_pos
                    starting_index2 = index_pos

                    while values_of_keys_for_pbs.count("P") > 0:
                        below_one = list_of_keys[starting_index + 1]
                        upper_one = list_of_keys[starting_index - 1]
                        left_one = list_of_keys[starting_index - 10]
                        if starting_index < 99:
                            if p1_dict_copy[list_of_keys[starting_index]] == "P":
                                if list_of_keys[starting_index] in patb_coords:
                                    pass
                                else:
                                    if starting_index < 90:
                                        right_one = list_of_keys[starting_index + 10]
                                        patrol_boat = []
                                        if p1_dict_copy[right_one] != "P" and p1_dict_copy[below_one] != "P" and \
                                                p1_dict_copy[upper_one] != "P":
                                            patb_coords.append(list_of_keys[starting_index])
                                            patb_coords.append(list_of_keys[starting_index - 10])
                                            patrol_boat.append(list_of_keys[starting_index])
                                            patrol_boat.append(list_of_keys[starting_index - 10])
                                            patrol_boats.append(patrol_boat)
                                            values_of_keys_for_pbs.remove("P")
                                            values_of_keys_for_pbs.remove("P")
                                            p1_dict_copy[list_of_keys[starting_index]] = "-"
                                            p1_dict_copy[list_of_keys[starting_index - 10]] = "-"
                                            continue

                                        elif p1_dict_copy[right_one] != "P" and p1_dict_copy[below_one] != "P" and \
                                                p1_dict_copy[left_one] != "P":
                                            patb_coords.append(list_of_keys[starting_index])
                                            patb_coords.append(list_of_keys[starting_index - 1])
                                            patrol_boat.append(list_of_keys[starting_index])
                                            patrol_boat.append(list_of_keys[starting_index - 1])
                                            patrol_boats.append(patrol_boat)
                                            values_of_keys_for_pbs.remove("P")
                                            values_of_keys_for_pbs.remove("P")
                                            p1_dict_copy[list_of_keys[starting_index]] = "-"
                                            p1_dict_copy[list_of_keys[starting_index - 1]] = "-"
                                            continue

                                        elif p1_dict_copy[right_one] != "P" and p1_dict_copy[left_one] != "P" and \
                                                p1_dict_copy[upper_one] != "P":
                                            patb_coords.append(list_of_keys[starting_index])
                                            patb_coords.append(list_of_keys[starting_index + 1])
                                            patrol_boat.append(list_of_keys[starting_index])
                                            patrol_boat.append(list_of_keys[starting_index + 1])
                                            patrol_boats.append(patrol_boat)
                                            values_of_keys_for_pbs.remove("P")
                                            values_of_keys_for_pbs.remove("P")
                                            p1_dict_copy[list_of_keys[starting_index]] = "-"
                                            p1_dict_copy[list_of_keys[starting_index + 1]] = "-"
                                            continue

                                        elif p1_dict_copy[left_one] != "P" and p1_dict_copy[below_one] != "P" and \
                                                p1_dict_copy[upper_one] != "P":
                                            patb_coords.append(list_of_keys[starting_index])
                                            patb_coords.append(list_of_keys[starting_index + 10])
                                            patrol_boat.append(list_of_keys[starting_index])
                                            patrol_boat.append(list_of_keys[starting_index + 10])
                                            patrol_boats.append(patrol_boat)
                                            values_of_keys_for_pbs.remove("P")
                                            values_of_keys_for_pbs.remove("P")
                                            p1_dict_copy[list_of_keys[starting_index]] = "-"
                                            p1_dict_copy[list_of_keys[starting_index + 10]] = "-"
                                            continue
                                        else:
                                            pass
                                    else:
                                        patrol_boat = []
                                        if p1_dict_copy[left_one] != "P":
                                            patb_coords.append(list_of_keys[starting_index])
                                            patb_coords.append(list_of_keys[starting_index + 1])
                                            patrol_boat.append(list_of_keys[starting_index])
                                            patrol_boat.append(list_of_keys[starting_index + 1])
                                            patrol_boats.append(patrol_boat)
                                            values_of_keys_for_pbs.remove("P")
                                            values_of_keys_for_pbs.remove("P")
                                            p1_dict_copy[list_of_keys[starting_index]] = "-"
                                            p1_dict_copy[list_of_keys[starting_index + 1]] = "-"
                                            continue
                                        else:
                                            pass
                            else:
                                pass
                        else:
                            starting_index = starting_index2
                        starting_index += 1

            else:                 #to get rid of the index error, this is an else for the right edge of the board
                if dictt[pos] == "C":
                    if pos in carrier:
                        pass
                    else:
                        for i in range(5):
                            carrier.append(list_of_keys[index_pos + i])
                elif dictt[pos] == "D":
                    if pos in destroyer:
                        pass
                    else:
                        for i in range(3):
                            destroyer.append(list_of_keys[index_pos + i])
                elif dictt[pos] == "S":

                    if pos in submarine:
                        pass
                    else:
                        for i in range(3):
                            submarine.append(list_of_keys[index_pos + i])

                elif dictt[pos] == "B":
                    battleship = []
                    if pos in b_coords:
                        pass
                    else:
                        for i in range(4):
                            b_coords.append(list_of_keys[index_pos + i])
                            battleship.append(list_of_keys[index_pos + i])
                        battleships.append(battleship)

        if dictt == p1_dict:
            p1_ships["carrier"] = carrier
            p1_ships["destroyer"] = destroyer
            p1_ships["submarine"] = submarine
            p1_ships["battleships"] = battleships
            p1_ships["patrol boats"] = patrol_boats
        elif dictt == p2_dict:
            p2_ships["carrier"] = carrier
            p2_ships["destroyer"] = destroyer
            p2_ships["submarine"] = submarine
            p2_ships["battleships"] = battleships
            p2_ships["patrol boats"] = patrol_boats


    ship_finder(p1_dict)
    ship_finder(p2_dict)


    def show_board2(p1_dict, p2_dict):
        print(" ", end="")
        out_file.write(f" ")
        for i in range(2):
            for alp in alp_list:
                if i == 2:
                    print(end="")
                else:
                    print(end=" ")
                    out_file.write(f" ")
                    if alp != "J":
                        print(alp, end="")
                        out_file.write(f"{alp}")
                    else:
                        print(alp+"\t\t", end=" ")
                        out_file.write(f"{alp}\t\t ")
        print("\n", end="")
        out_file.write(f"\n")
        for row in range(1,11):
            if row == 10:
                print(row, end="")
                out_file.write(f"{row}")
            else:
                print(row, end=" ")
                out_file.write(f"{row} ")

            for column in alp_list:
                if column == "J":
                    print(p1_dict[column+str(row)], end="")
                    out_file.write(f"{p1_dict[column+str(row)]}")
                    print("\t\t", end="")
                    out_file.write(f"\t\t")
                else:
                    print(p1_dict[column + str(row)], end=" ")
                    out_file.write(f"{p1_dict[column + str(row)]} ")
            if row == 10:
                print(row, end="")
                out_file.write(f"{row}")
            else:
                print(row, end=" ")
                out_file.write(f"{row} ")

            for column in alp_list:
                if column == "J":
                    print(p2_dict[column+str(row)], end="")
                    out_file.write(f"{p2_dict[column+str(row)]}")
                else:
                    print(p2_dict[column + str(row)], end=" ")
                    out_file.write(f"{p2_dict[column+str(row)]} ")
            print()
            out_file.write(f"\n")


    p1_board_dict=p1_dict.copy()
    for elem in p1_board_dict:
        if p1_board_dict[elem] != "-":
            p1_board_dict[elem] = "-"

    p2_board_dict = p2_dict.copy()
    for elem in p2_board_dict:
        if p2_board_dict[elem] != "-":
            p2_board_dict[elem] = "-"


    def show_ship_c(p1_ships, p2_ships):
        print("Carrier\t\t", "-" if p1_ships["carrier"]!=[] else "X", end="") #1
        out_file.write(f"Carrier\t\t ")
        if p1_ships["carrier"]!=[]:
            out_file.write(f"-")
        else:
            out_file.write(f"X")

        print("\t\t\t\t", end="")
        out_file.write(f"\t\t\t\t")
        print("Carrier\t\t", "-" if p2_ships["carrier"] != [] else "X", end="\n")#2
        out_file.write(f"Carrier\t\t ")
        if p2_ships["carrier"] != []:
            out_file.write(f"-\n")
        else:
            out_file.write(f"X\n")

        how_many_battleship_sunk1 = p1_ships["battleships"].count([]) #1
        print("Battleship  ", "X"*how_many_battleship_sunk1+ "-"*(2-how_many_battleship_sunk1), end="")
        print("\t\t\t\t", end="")
        out_file.write(f"Battleship   ")
        if how_many_battleship_sunk1 == 0:
            out_file.write(f"--")
        elif how_many_battleship_sunk1 == 1:
            out_file.write(f"X-")
        elif how_many_battleship_sunk1 == 2:
            out_file.write(f"XX")

        out_file.write(f"\t\t\t\t")
        how_many_battleship_sunk2 = p2_ships["battleships"].count([]) #2
        print("Battleship  ", "X"*how_many_battleship_sunk2+ "-"*(2-how_many_battleship_sunk2))
        out_file.write(f"Battleship   ")
        if how_many_battleship_sunk2 == 0:
            out_file.write(f"--\n")
        elif how_many_battleship_sunk2 == 1:
            out_file.write(f"X-\n")
        elif how_many_battleship_sunk2 == 2:
            out_file.write(f"XX\n")

        print("Destroyer\t", "-" if p1_ships["destroyer"]!=[] else "X", end="") #1
        out_file.write(f"Destroyer\t ")
        if p1_ships["destroyer"]!=[]:
            out_file.write(f"-")
        else:
            out_file.write(f"X")

        print("\t\t\t\t", end="")
        out_file.write(f"\t\t\t\t")
        print("Destroyer\t", "-" if p2_ships["destroyer"] != [] else "X", end="\n") #2
        out_file.write(f"Destroyer\t ")
        if p2_ships["destroyer"]!=[]:
            out_file.write(f"-\n")
        else:
            out_file.write(f"X\n")
        print("Submarine\t", "-" if p1_ships["submarine"]!=[] else "X", end="") #1
        out_file.write(f"Submarine\t ")
        if p1_ships["submarine"]!=[]:
            out_file.write(f"-")
        else:
            out_file.write(f"X")
        print("\t\t\t\t", end="")
        out_file.write(f"\t\t\t\t")
        print("Submarine\t", "-" if p2_ships["submarine"] != [] else "X", end="\n") #2
        out_file.write(f"Submarine\t ")
        if p2_ships["submarine"]!=[]:
            out_file.write(f"-\n")
        else:
            out_file.write(f"X\n")
        how_many_pb_sunk1 = p1_ships["patrol boats"].count([]) #1
        print("Patrol Boat ", "X"*how_many_pb_sunk1+ "-"*(4-how_many_pb_sunk1), end="")
        out_file.write(f"Patrol Boat  ")
        if how_many_pb_sunk1 == 0:
            out_file.write(f"----")
        elif how_many_pb_sunk1 == 1:
            out_file.write(f"X---")
        elif how_many_pb_sunk1 == 2:
            out_file.write(f"XX--")
        elif how_many_pb_sunk1 == 3:
            out_file.write(f"XXX-")
        elif how_many_pb_sunk1 == 4:
            out_file.write(f"XXXX")
        print("\t\t\t", end="")
        out_file.write(f"\t\t\t")
        how_many_pb_sunk2 = p2_ships["patrol boats"].count([]) #2
        print("Patrol Boat ", "X"*how_many_pb_sunk2+ "-"*(4-how_many_pb_sunk2), end="\n")
        out_file.write(f"Patrol Boat  ")
        if how_many_pb_sunk2 == 0:
            out_file.write(f"----\n")
        elif how_many_pb_sunk2 == 1:
            out_file.write(f"X---\n")
        elif how_many_pb_sunk2 == 2:
            out_file.write(f"XX--\n")
        elif how_many_pb_sunk2 == 3:
            out_file.write(f"XXX-\n")
        elif how_many_pb_sunk2 == 4:
            out_file.write(f"XXXX\n")

    class IndexError0(Exception):
        ""
        pass

    class IndexError1(Exception):
        ",;"
        pass

    class IndexError2(Exception):
        "A;"
        pass

    class IndexError3(Exception):
        "1"
        pass

    class IndexError4(Exception):
        ",A"
        pass

    class IndexError5(Exception):
        "1,"
        pass

    class IndexError6(Exception):
        "1,"
        pass

    class ValueError0(Exception):
        "A,1"
        pass

    class ValueError1(Exception):
        "A,A"
        pass

    class ValueError2(Exception):
        "1,1"
        pass

    class ValueError3(Exception):
        "more than one comma"
        pass

    class ValueError4(Exception):
        "lenght of the move input is 3 or 4, but inside there is no comma"
        pass

    class ValueError5(Exception):
        "the input is too long"
        pass

    class AssertionError0(Exception):
        "the input has 3 characters"
        pass

    class AssertionError1(Exception):
        "the input has 4 characters"
        pass



    print("Battle of Ships Game\n")
    out_file.write(f"Battle of Ships Game\n\n")
    def move_maker(n): # is the round
        try:
            car1_sunk = 0
            dst1_sunk = 0
            sub1_sunk = 0
            bship1_sunk = 0
            pb1_sunk = 0

            car2_sunk = 0
            dst2_sunk = 0
            sub2_sunk = 0
            bship2_sunk = 0
            pb2_sunk = 0
            for roundd in range(n,len(moves1)):
                for j in range(1,3):
                    move1 = moves1[roundd - 1]
                    move2 = moves2[roundd - 1]
                    if j == 1:
                        the_move = move1
                        the_moves = moves1
                        player = "Player1"
                    else:
                        the_move = move2
                        the_moves = moves2
                        player = "Player2"

                    if the_move == '':
                        the_moves.remove(the_move)
                        raise IndexError0
                    elif the_move == "," :
                        the_moves.remove(the_move)
                        raise IndexError1
                    elif the_move in ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z"]:
                        the_moves.remove(the_move)
                        raise IndexError2
                    elif len(the_move) == 1 and type(int(the_move)) == int:
                        the_moves.remove(the_move)
                        raise IndexError3
                    elif len(the_move) == 2 and the_move[0] == ",":
                        the_moves.remove(the_move)
                        raise IndexError4
                    elif len(the_move) == 2 and the_move[1] == ",":
                        the_moves.remove(the_move)
                        raise IndexError5
                    elif len(the_move)==2 and "," not in the_move:
                        the_moves.remove(the_move)
                        raise IndexError6
                    elif len(the_move)==3 or len(the_move)==4 : # ValueErrors
                        if the_move[0] in ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z"]:
                            if the_move[1] == ",":
                                if the_move[2] in ["0","1","2","3","4","5","6","7","8","9"]:  # A,1
                                    the_moves.remove(the_move)
                                    raise ValueError0
                                elif the_move[2] in ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z"]: # A,A
                                    the_moves.remove(the_move)
                                    raise ValueError1
                        elif the_move[0] in ["0","1","2","3","4","5","6","7","8","9"]:
                            if the_move[1] == ",":
                                if the_move[2] in ["0","1","2","3","4","5","6","7","8","9"]:  # 1,1
                                    the_moves.remove(the_move)
                                    raise ValueError2
                            elif the_move.count(",")>1:
                                the_moves.remove(the_move)
                                raise ValueError3
                            elif "," not in the_move:
                                the_moves.remove(the_move)
                                raise ValueError4
                    elif len(the_move) > 4:
                        the_moves.remove(the_move)
                        raise ValueError5
                    if len(the_move) == 3 or len(the_move)==4:
                        if len(the_move) ==3:
                            if the_move[0] not in ["1","2","3","4","5","6","7","8","9"] or the_move[1]!="," or the_move[2] not in ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]:
                                the_moves.remove(the_move)
                                raise AssertionError0
                        elif len(the_move) ==4:
                            if the_move[:2] != "10" or the_move[2] != "," or the_move[3] not in ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]:
                                the_moves.remove(the_move)
                                raise AssertionError1
                    move1_nicer = move1[-1] + move1[:move1.index(",")]  # nicer just indicates that moves are written in form G2 not 2,G for example.
                    move2_nicer = move2[-1] + move2[:move2.index(",")]


                    if j == 1:
                        print("Player1's Move\n")
                        out_file.write(f"Player1's Move\n")
                        print("Round :", roundd, end="")
                        out_file.write(f"\nRound : {roundd}")
                        print("\t\t\t\t\tGrid Size: 10x10")
                        out_file.write(f"\t\t\t\t\tGrid Size: 10x10\n")
                        print("\n", end="")
                        out_file.write(f"\n")
                        print("Player1's Hidden Board\t\tPlayer2's Hidden Board")
                        out_file.write(f"Player1's Hidden Board\t\tPlayer2's Hidden Board\n")
                        show_board2(p1_board_dict, p2_board_dict)
                        out_file.write(f"\n")
                        show_ship_c(p1_ships, p2_ships)
                        print("\nEnter your move: ", move1, end="\n")
                        out_file.write(f"\nEnter your move: {move1}")
                        if p2_dict[move1_nicer] != "-":
                            typee = p2_dict[move1_nicer]
                            if typee == "C":
                                #######print(move1_nicer)
                                p2_ships["carrier"].remove(move1_nicer)
                            elif typee == "B":
                                if move1_nicer in p2_ships["battleships"][0]:
                                    p2_ships["battleships"][0].remove(move1_nicer)
                                else:
                                    p2_ships["battleships"][1].remove(move1_nicer)
                            elif typee == "D":
                                p2_ships["destroyer"].remove(move1_nicer)
                            elif typee == "S":
                                p2_ships["submarine"].remove(move1_nicer)
                            elif typee == "P":
                                for m in range(4):
                                    if move1_nicer in p2_ships["patrol boats"][m]:
                                        p2_ships["patrol boats"][m].remove(move1_nicer)
                                    else:
                                        pass
                            p2_board_dict[move1_nicer] = "X"
                        else:
                            p2_board_dict[move1_nicer] = "O"

                        if p1_ships["carrier"] == [] and car1_sunk == 0:
                            car1_sunk += 1
                            print("\nPlayer 1's carrier has been sunk!")
                            out_file.write(f"\n\nPlayer 1's carrier has been sunk!")

                        elif p1_ships["destroyer"] == [] and dst1_sunk == 0:
                            dst1_sunk += 1
                            print("\nPlayer 1's destroyer has been sunk!")
                            out_file.write(f"\n\nPlayer 1's destroyer has been sunk!")

                        elif p1_ships["submarine"] == [] and sub1_sunk ==0:
                            sub1_sunk += 1
                            print("\nPlayer 1's submarine has been sunk!")
                            out_file.write(f"\n\nPlayer 1's submarine has been sunk!")

                        elif p1_ships["battleships"].count([]) == 1 and bship1_sunk == 0:
                            bship1_sunk += 1
                            print("\nPlayer 1's one battleship has been sunk!")
                            out_file.write(f"\n\nPlayer 1's one battleship has been sunk!")
                        elif p1_ships["battleships"].count([]) == 1 and bship1_sunk == 1:
                            bship1_sunk += 1
                            print("\nPlayer 1's other battleship has been sunk!")
                            out_file.write("\n\nPlayer 1's other battleship has been sunk!")
                        elif p1_ships["patrol boats"].count([]) == 1 and pb1_sunk == 0:
                            pb1_sunk += 1
                            print("\nPlayer 1's one patrol boat has been sunk")
                            out_file.write(f"\n\nPlayer 1's one patrol boat has been sunk")
                        elif p1_ships["patrol boats"].count([]) == 2 and pb1_sunk == 1:
                            pb1_sunk += 1
                            print("\nPlayer 1's one more patrol boat has been sunk")
                            out_file.write(f"\n\nPlayer 1's one more patrol boat has been sunk")
                        elif p1_ships["patrol boats"].count([]) == 3 and pb1_sunk == 2:
                            pb1_sunk += 1
                            print("\nPlayer 1's one more patrol boat has been sunk")
                            out_file.write(f"\n\nPlayer 1's one more patrol boat has been sunk")
                        elif p1_ships["patrol boats"].count([]) == 4 and pb1_sunk == 3:
                            pb1_sunk += 1
                            print("\nPlayer 1's last patrol boat has been sunk")
                            out_file.write(f"\n\nPlayer 1's last patrol boat has been sunk")

                    elif j == 2:
                        #move2_nicer = move2[-1] + move2[:move2.index(",")]
                        print("Player2's Move\n")
                        out_file.write(f"\nPlayer2's Move\n")
                        print("Round :", roundd, end="")
                        out_file.write(f"\nRound : {roundd}")
                        print("\t\t\t\t\tGrid Size: 10x10")
                        out_file.write(f"\t\t\t\t\tGrid Size: 10x10\n")
                        print("\n", end="")
                        out_file.write(f"\n")
                        print("Player1's Hidden Board\t\tPlayer2's Hidden Board")
                        out_file.write(f"Player1's Hidden Board\t\tPlayer2's Hidden Board\n")
                        show_board2(p1_board_dict, p2_board_dict)
                        print("\n", end="")
                        out_file.write("\n")
                        show_ship_c(p1_ships, p2_ships)
                        print("\nEnter your move: ", move2)
                        out_file.write(f"\nEnter your move: {move2}")
                        print("\n", end="")
                        out_file.write(f"\n")
                        if p1_dict[move2_nicer] != "-":
                            typee = p1_dict[move2_nicer]
                            if typee == "C":
                                p1_ships["carrier"].remove(move2_nicer)
                            elif typee == "B":
                                if move2_nicer in p1_ships["battleships"][0]:
                                    p1_ships["battleships"][0].remove(move2_nicer)
                                else:
                                    p1_ships["battleships"][1].remove(move2_nicer)
                            elif typee == "D":
                                p1_ships["destroyer"].remove(move2_nicer)
                            elif typee == "S":
                                p1_ships["submarine"].remove(move2_nicer)
                            elif typee == "P":
                                for m in range(4):
                                    if move2_nicer in p1_ships["patrol boats"][m]:
                                        p1_ships["patrol boats"][m].remove(move2_nicer)
                                    else:
                                        pass
                            p1_board_dict[move2_nicer] = "X"
                        else:
                            p1_board_dict[move2_nicer] = "O"

                        if p2_ships["carrier"] == [] and car2_sunk == 0:
                            car2_sunk += 1
                            print("\nPlayer 2's carrier has been sunk!")
                            out_file.write(f"\nPlayer2's carrier has been sunk!\n")
                        elif p2_ships["destroyer"] == [] and dst2_sunk == 0:
                            dst2_sunk += 1
                            print("\nPlayer 2's destroyer has been sunk!")
                            out_file.write(f"\nPlayer2's destroyer has been sunk!\n")
                        elif p2_ships["submarine"] == [] and sub2_sunk == 0:
                            sub2_sunk += 1
                            print("\nPlayer 2's submarine has been sunk!")
                            out_file.write(f"\nPlayer2's submarine has been sunk!\n")
                        elif p2_ships["battleships"].count([]) == 1 and bship2_sunk == 0:
                            bship2_sunk += 1
                            print("\nPlayer 1's one battleship has been sunk!")
                            out_file.write(f"\nPlayer2's one battleship has been sunk!\n")
                        elif p2_ships["battleships"].count([]) == 2 and bship2_sunk == 1:
                            bship2_sunk += 1
                            print("\nPlayer 1's other battleship has been sunk!")
                            out_file.write(f"\nPlayer2's other battleship has been sunk!\n")
                        elif p2_ships["patrol boats"].count([]) == 1 and pb2_sunk == 0:
                            pb2_sunk += 1
                            print("\nPlayer 1's one patrol boat has been sunk")
                            out_file.write(f"\nPlayer2's one patrol boat has been sunk!\n")
                        elif p2_ships["patrol boats"].count([]) == 2 and pb2_sunk == 1:
                            pb2_sunk += 1
                            print("\nPlayer 1's one more patrol boat has been sunk")
                            out_file.write(f"\nPlayer 2's one more patrol boat has been sunk!\n")
                        elif p2_ships["patrol boats"].count([]) == 3 and pb2_sunk == 2:
                            pb2_sunk += 1
                            print("\nPlayer 1' one more patrol boat has been sunk")
                            out_file.write(f"\nPlayer2's one more patrol boat has been sunk!\n")
                        elif p2_ships["patrol boats"].count([]) == 4 and pb2_sunk == 3:
                            pb2_sunk += 1
                            print("\nPlayer 1's last patrol boat has been sunk")
                            out_file.write(f"\nPlayer2's last patrol boat has been sunk!\n")

                        p2win = p1_ships["carrier"] == [] and p1_ships["destroyer"] == [] and p1_ships["submarine"] == [] and \
                                p1_ships["battleships"] == [[], []] and p1_ships["patrol boats"] == [[], [], [], []]
                        p1win = p2_ships["carrier"] == [] and p2_ships["destroyer"] == [] and p2_ships["submarine"] == [] and \
                                p2_ships["battleships"] == [[], []] and p2_ships["patrol boats"] == [[], [], [], []]
                        if p1win and not p2win:
                            print("\nPlayer 1 Wins")
                            out_file.write(f"\nPlayer 1 Wins")
                        elif not p1win and p2win:
                            print("\nPlayer 2 Wins")
                            out_file.write(f"\nPlayer 2 Wins")
                        elif p1win and p2win:
                            print("\nPlayer 1 Wins\nPlayer 2 Wins\nIt is a Draw")
                            out_file.write(f"\nPlayer 1 Wins\nPlayer 2 Wins\nIt is a Draw")
                    print()
                    out_file.write(f"\n")
            print("Final Information")
            out_file.write(f"\nFinal Information\n")
            for elemm1 in p1_board_dict:
                if p1_board_dict[elemm1] == "-" and p1_dict[elemm1] != "-":
                    p1_board_dict[elemm1] = p1_dict[elemm1]
            for elemm2 in p2_board_dict:
                if p2_board_dict[elemm2] == "-" and p2_dict[elemm2] != "-":
                    p2_board_dict[elemm2] = p2_dict[elemm2]
            print("\nPlayer1's Board\t\t\t\tPlayer 2's Board")
            out_file.write(f"\nPlayer1's Board\t\t\t\tPlayer 2's Board\n")
            show_board2(p1_board_dict, p2_board_dict)
            print("\n", end="")
            out_file.write(f"\n")
            show_ship_c(p1_ships, p2_ships)
        except IndexError0:
            print("inputs can not be empty,", player+"'s move on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not be empty, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError1:
            print("inputs can not only contain \',\',", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not only contain \',\', {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError2:
            print("inputs can not only contain columns,", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not only contain columns, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError3:
            print("inputs can not only contain numbers,", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not only contain numbers, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError4:
            print("inputs can not only contain the comma and the column,", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not only the comma and the column, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError5:
            print("inputs can not only contain the row and the comma,", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs can not only the row and the comma, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except IndexError6:
            print("inputs must contain the comma,", player + "\'s input on round", roundd," was : ", the_move, "\n")
            out_file.write(f"inputs must contain the comma, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError0:
            print("Inputs can not start with columns and end with rows", player +"\'s input on round", roundd, "was : ", the_move, "\n")
            out_file.write(f"inputs can not start with columns and end with rows, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError1:
            print("Inputs can not start with columns and end with columns", player + "\'s input on round", roundd, "was : ",the_move, "\n")
            out_file.write(f"inputs can not start with columns and end with columns, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError2:
            print("Inputs can not start with rows and end with rows", player + "\'s input on round", roundd, "was : ",the_move, "\n")
            out_file.write(f"inputs can not start with rows and end with rows, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError3:
            print("Inputs can not contain more than one comma", player + "\'s input on round", roundd, "was : ",the_move, "\n")
            out_file.write(f"inputs can not contain more than one comma, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError4:
            print("Inputs must contain a comma", player + "\'s input on round", roundd, "was : ", the_move,"\n")
            out_file.write(f"inputs must contain a comma, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except ValueError5:
            print("Inputs must have lenght of three or four characters", player + "\'s input on round", roundd, "was : ", the_move, "\n")
            out_file.write(f"\ninputs must have lenght of three or four characters, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except AssertionError0:
            print("The input doesn't match the game rules", player + "\'s input on round", roundd, "was : ", the_move, "\n")
            out_file.write(f"\nThe input doesn't match the game rules, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)
        except AssertionError1:
            print("The input doesn't match the game rules", player + "\'s input on round", roundd, "was : ", the_move, "\n")
            out_file.write(f"\nThe input doesn't match the game rules, {player}\'s move on round {roundd} was : {the_move}\n\n")
            move_maker(roundd)

    move_maker(1)
    out_file.close()

except:
    print("Kabooom runnn for your life")
    out_file.write(f"\nKabooom runnn for your life")
