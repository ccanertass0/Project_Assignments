import sys
input_file=open(sys.argv[1],"r")           # reading the input file
read=input_file.readlines()
inputs=[]                                 # an empty list is created for getting inputs into the list
for line in read:                         # this for statements is for removing \n parts of the inputs
    if line[-1] == "\n":
        inputs.append(line[:-1])
    else:
        inputs.append(line)
output_file=open("output.txt" , "w")

Alphabet=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]   #this list is useful when in alphabetic order

category_list=[]                 # this is the list to have a short look at categories that exist and and their capacities
all_cat=[]
def create_cat():                 # this is the function for creating categories
    category_name=after_command[:after_command.index(" ")]
    size = after_command[after_command.index(" ")+1:]
    num_rows=int(size[0:size.index("x")])
    num_columns=int(size[size.index("x")+1:])
    total_seats=num_rows*num_columns
    if category_name not in category_list:
        category_list.append(category_name)
        category_list.append(total_seats)
        output_file.write(f"The category \'{category_name}\' having {total_seats} seats has been created\n")
        print(f"The category \'{category_name}\' having {total_seats} seats has been created")
        each_cat={}
        for i in range(num_rows) :         #creating each seat and "appending"? them into the each_cat dictionary which represents only one category and its seats
            for j in range(num_columns):
                each_cat.update({Alphabet[i]+str(j):"X"})
        all_cat.append(category_name)
        all_cat.append(each_cat)
    else:
        output_file.write(f"Warning: Cannot create the category for the second time. The stadium has already {category_name}\n")
        print(f"Warning: Cannot create the category for the second time. The stadium has already {category_name}")


category_table=[]
def show_cat():            # this is the function for showing the categories
    category_name=after_command
    output_file.write(f"Printing category layout of {category_name}\n\n")
    print(f"Printing category layout of {category_name}\n")
    cat_dic=all_cat[all_cat.index(category_name)+1]
    the_last_seat=cat_dic.popitem()
    cat_dic.update({the_last_seat})
    thereal_last_seat=the_last_seat[0]   # thereal_last_seat is the seat at the very top-right corner, this is special because it is both in the last row and column
    the_last_seat_alp=(the_last_seat[0])[0]     # row of the the thereal_last_seat
    the_last_seat_num=(the_last_seat[0])[1:]    # column of the the thereal_last_seat
    for i in range(Alphabet.index(the_last_seat_alp),-1,-1):           # Rows are printed here (from line 46 to 50)
        output_file.write(f"{Alphabet[i]} ")
        print(f"{Alphabet[i]} ",end="")
        for j in range(int(the_last_seat_num)+1):
            output_file.write(f"{cat_dic[Alphabet[i]+str(j)]}  ")
            print(f"{cat_dic[Alphabet[i]+str(j)]}  ",end="")
        output_file.write(f"\n")
        print()
    output_file.write(f"  ")
    print(f"  ",end="")
    for k in range(int(the_last_seat_num)+1):            # from line 52 to 58, it is about adjusting the spaces betweeen column numbers
        if k<9:
            output_file.write(f"{k}  ")
            print(f"{k}  ",end="")
        elif k<99:
            output_file.write(f"{k} ")
            print(f"{k} ", end ="")
        else:
            output_file.write(f"{k}")
            print(f"{k}",end="")
    output_file.write(f"\n")
    print()


def sell_tic():
    buyer_info=after_command.split(" ")
    buyer_name=buyer_info[0]
    if buyer_info[1]=="full":
        buyer_type="F"
    elif buyer_info[1]=="student":
        buyer_type="S"
    elif buyer_info[1]=="season":
        buyer_type="T"
    buyer_cat=buyer_info[2]
    buyer_seats=buyer_info[3:]             # This is a list
    cat_dic=all_cat[all_cat.index(buyer_cat)+1]
    the_last_seat = cat_dic.popitem()
    cat_dic.update({the_last_seat})
    the_last_seat_num = (the_last_seat[0])[1:]
    the_last_seat_alp = (the_last_seat[0])[0]
    for j in buyer_seats:
        if "-" not in j:    #this is for single ticket buying processes
            if Alphabet.index(j[0])>Alphabet.index(the_last_seat_alp) and int(j[1:])>int(the_last_seat_num):      # if rows and columns are not enough
                output_file.write(f"Error: The category '{buyer_cat}' has less row and column than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less row and column than the specified index {j}!")
            elif Alphabet.index(j[0])>Alphabet.index(the_last_seat_alp) and int(j[1:])<=int(the_last_seat_num):     # if only rows are not enoug
                output_file.write(f"Error: The category '{buyer_cat}' has less row than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less row than the specified index {j}!")
            elif Alphabet.index(j[0])<=Alphabet.index(the_last_seat_alp) and int(j[1:])>int(the_last_seat_num):      # if columns are not enough
                output_file.write(f"Error: The category '{buyer_cat}' has less column than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less column than the specified index {j}!")
            else:                                                                                                   # if everything is okay
                if (all_cat[all_cat.index(buyer_cat)+1])[j]=="X":
                    (all_cat[all_cat.index(buyer_cat)+1])[j]=buyer_type
                    output_file.write(f"Success: {buyer_name} has bought {j} at {buyer_cat}\n")
                    print(f"Success: {buyer_name} has bought {j} at {buyer_cat}")
                else:
                    output_file.write(f"Warning: The seat {j} cannot be sold to {buyer_name} since it was already sold!\n")
                    print(f"Warning: The seat {j} cannot be sold to {buyer_name} since it was already sold!")
        elif "-" in j:             #this is for when one wants to buy a couple of seats next to each other
            seat_range_alp=j[0]
            seat_range_start=j[1:j.index("-")]
            seat_range_finish=j[j.index("-")+1:]
            seats_ava=True
            if Alphabet.index(j[0])>Alphabet.index(the_last_seat_alp) and int(seat_range_finish)>int(the_last_seat_num): # if rows and columns are not enough
                output_file.write(f"Error: The category '{buyer_cat}' has less row and column than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less row and column than the specified index {j}!")
            elif Alphabet.index(j[0])>Alphabet.index(the_last_seat_alp) and int(seat_range_finish)<=int(the_last_seat_num): # if only rows are not enough
                output_file.write(f"Error: The category '{buyer_cat}' has less row than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less row than the specified index {j}!")
            elif Alphabet.index(j[0])<=Alphabet.index(the_last_seat_alp) and int(seat_range_finish)>int(the_last_seat_num): # if columns are not enough
                output_file.write(f"Error: The category '{buyer_cat}' has less column than the specified index {j}!\n")
                print(f"Error: The category '{buyer_cat}' has less column than the specified index {j}!")
            else:                                                                                                           # if everything is okay
                for n in range(int(seat_range_start),int(seat_range_finish)+1):   #from line 105 to 113 it is about changing the letter of seats that are just sold.
                    if (all_cat[all_cat.index(buyer_cat)+1])[seat_range_alp+str(n)]!="X":
                        output_file.write(f"Warning: The seats {j} cannot be sold to {buyer_name} due some of them have already been sold\n")
                        print(f"Warning: The seats {j} cannot be sold to {buyer_name} due some of them have already been sold")
                        seats_ava=False
                        break
                if seats_ava:
                    for n in range(int(seat_range_start), int(seat_range_finish) + 1):
                        (all_cat[all_cat.index(buyer_cat)+1])[seat_range_alp+str(n)]=buyer_type
                    output_file.write(f"Success: {buyer_name} has bought {j} at {buyer_cat}\n")
                    print(f"Success: {buyer_name} has bought {j} at {buyer_cat}")


def cancel_tic():     #this is for canceling tickets
    cancel_info=after_command.split(" ")
    cat_name = cancel_info[0]
    cat_seats = cancel_info[1:]
    cat_dic=all_cat[all_cat.index(cat_name)+1]
    the_last_seat = cat_dic.popitem()
    cat_dic.update({the_last_seat})
    the_last_seat_num = (the_last_seat[0])[1:]
    the_last_seat_alp = (the_last_seat[0])[0]
    for i in cat_seats:
        if Alphabet.index(i[0])>Alphabet.index(the_last_seat_alp) and int(i[1:])>int(the_last_seat_num):   #if rows and columns are not enough
            output_file.write(f"Error: The category '{cat_name}' has less row and column than the specified index {i}!\n")
            print(f"Error: The category '{cat_name}' has less row and column than the specified index {i}!")
        elif Alphabet.index(i[0])>Alphabet.index(the_last_seat_alp) and int(i[1:])<=int(the_last_seat_num):   #if only rows are not enough
            output_file.write(f"Error: The category '{cat_name}' has less row than the specified index {i}!\n")
            print(f"Error: The category '{cat_name}' has less row than the specified index {i}!")
        elif Alphabet.index(i[0])<=Alphabet.index(the_last_seat_alp) and int(i[1:])>int(the_last_seat_num):  #if columns are not enough
            output_file.write(f"Error: The category '{cat_name}' has less column than the specified index {i}!\n")
            print(f"Error: The category '{cat_name}' has less column than the specified index {i}!")
        else:          #if everything is okay
            if all_cat[all_cat.index(cat_name)+1][i] != "X":
                all_cat[all_cat.index(cat_name) + 1][i]="X"           #changing the letter of the related seat therefore making it availlable for selling again
                output_file.write(f"Success: The seat {i} at '{cat_name}' has been canceled and now ready to sell again\n")
                print(f"Success: The seat {i} at '{cat_name}' has been canceled and now ready to sell again")
            else:
                output_file.write(f"Error: The seat {i} at {cat_name} has already been free! Nothing to cancel\n")
                print(f"Error: The seat {i} at '{cat_name}' has already been free! Nothing to cancel")


def balance(): #this is for printing out the category report
    cat_name = after_command
    cat_dic = all_cat[all_cat.index(cat_name)+1]
    student_num, full_num, season_num=0, 0, 0
    for i in cat_dic:
        if cat_dic[i]=="S":
            student_num+=1
        elif cat_dic[i]=="F":
            full_num +=1
        elif cat_dic[i]=="T":
            season_num+=1
    cat_report_statement="category report of "+str(cat_name)
    output_file.write(f"category report of '{cat_name}'\n")
    print(f"category report of '{cat_name}'")
    for j in range(len(cat_report_statement)+2):
        output_file.write(f"-")
        print(f"-",end="")
    output_file.write(f"\nSum of students = {student_num}, Sum of full pay = {full_num}, Sum of season ticket = {season_num}, and Revenues = {(10*student_num)+(20*full_num)+(250*season_num)} Dollars\n")
    print(f"\nSum of students = {student_num}, Sum of full pay = {full_num}, Sum of season ticket = {season_num}, and Revenues = {(10*student_num)+(20*full_num)+(250*season_num)} Dollars")

for i in inputs:      #taking inputs from the list called input
    command=i[0:i.index(" ")]
    after_command = i[i.index(" ") + 1:]
    if command=="CREATECATEGORY":
        create_cat()
    elif command=="SHOWCATEGORY":
        show_cat()
    elif command=="SELLTICKET":
        sell_tic()
    elif command=="CANCELTICKET":
        cancel_tic()
    elif command=="BALANCE":
        balance()

output_file.close()
#My name is Can Ertas (s has a dot underneath it)
#My student number is 2220356088