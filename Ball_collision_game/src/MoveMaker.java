import java.io.FileNotFoundException;

public class MoveMaker{

    public String boardTxt;
    public String moveTxt;
    int finalScore;
    boolean fellIntoHole = false;

    public MoveMaker(String boardTxt, String moveTxt) {
        this.boardTxt = boardTxt;
        this.moveTxt = moveTxt;
        this.finalScore = 0;
    }

    public void main() throws FileNotFoundException {
        ReadFile boardInput = new ReadFile(boardTxt);
        boardInput.readAndAppend();
        String[] boardInputArray = boardInput.getInputArray();


        ReadFile moveInput = new ReadFile(moveTxt);
        moveInput.readAndAppend();
        String[] moveInputArray = moveInput.getInputArray();
        String moves = moveInputArray[0];
        moveMaker(boardInputArray, moves, boardInput.getPositionOfTheBall());

    }

    public void moveMaker(String[] boardInputArray, String moves, int[] positions ){
        int linePosMax = boardInputArray.length;
        int columnPosMax = boardInputArray[0].length();
        int currentLinePos = positions[0];
        int currentColumnPos = positions[1];
        char initialTarget = ' ';


        WriteToFile.print("Game Board:\n");
        for(int line = 0; line < boardInputArray.length; line++){    // game board printing
            for(int character = 0; character < boardInputArray[line].length(); character++){
                if(character == 0){
                    WriteToFile.print(String.valueOf(boardInputArray[line].charAt(0)));
                }else{
                    WriteToFile.print(" " + boardInputArray[line].charAt(character));
                }
            }
            WriteToFile.print("\n");
        }
        WriteToFile.print("\n");


        int movesPlayed = 0;
        for(int i = 0; i < moves.length(); i++){
            if(!fellIntoHole) {  // if it did not fell in to the hole
                switch (moves.charAt(i)) {
                    case 'R':
                        initialTarget = boardInputArray[currentLinePos - 1].charAt((currentColumnPos == columnPosMax) ? 0 : currentColumnPos);

                        if (initialTarget == 'W') {
                            moveHorizontal(currentLinePos, currentColumnPos, false, columnPosMax, boardInputArray);
                            currentColumnPos--;
                            currentColumnPos = (currentColumnPos == 0) ? columnPosMax : currentColumnPos;

                        } else {
                            moveHorizontal(currentLinePos, currentColumnPos, true, columnPosMax, boardInputArray);
                            currentColumnPos++;
                            currentColumnPos = (currentColumnPos == columnPosMax + 1) ? 1 : currentColumnPos;

                        }

                        break;
                    case 'U':
                        initialTarget = boardInputArray[(currentLinePos == 1) ? linePosMax - 1 : currentLinePos - 2].charAt(currentColumnPos - 1);

                        if (initialTarget == 'W') {
                            moveVertical(currentLinePos, currentColumnPos, false, linePosMax, columnPosMax, boardInputArray);
                            currentLinePos++;
                            currentLinePos = (currentLinePos == linePosMax + 1) ? 1 : currentLinePos;

                        } else {
                            moveVertical(currentLinePos, currentColumnPos, true, linePosMax, columnPosMax, boardInputArray);
                            currentLinePos--;
                            currentLinePos = (currentLinePos == 0) ? linePosMax : currentLinePos;

                        }

                        break;
                    case 'L':
                        initialTarget = boardInputArray[currentLinePos - 1].charAt((currentColumnPos == 1) ? (columnPosMax - 1) : (currentColumnPos - 2));
                        if (initialTarget == 'W') {
                            moveHorizontal(currentLinePos, currentColumnPos, true, columnPosMax, boardInputArray);
                            currentColumnPos++;
                            currentColumnPos = (currentColumnPos == 0) ? columnPosMax : currentColumnPos;

                        } else {
                            moveHorizontal(currentLinePos, currentColumnPos, false, columnPosMax, boardInputArray);
                            currentColumnPos--;
                            currentColumnPos = (currentColumnPos == 0) ? columnPosMax : currentColumnPos;

                        }

                        break;
                    case 'D':
                        initialTarget = boardInputArray[(currentLinePos == linePosMax) ? 0 : currentLinePos].charAt(currentColumnPos - 1);

                        if (initialTarget == 'W') {
                            moveVertical(currentLinePos, currentColumnPos, true, linePosMax, columnPosMax, boardInputArray);
                            currentLinePos--;
                            currentLinePos = (currentLinePos == 0) ? linePosMax : currentLinePos;
                        } else {
                            moveVertical(currentLinePos, currentColumnPos, false, linePosMax, columnPosMax, boardInputArray);
                            currentLinePos++;
                            currentLinePos = (currentLinePos == linePosMax + 1) ? 1 : currentLinePos;

                        }
                        break;
                    default:
                        break;
                }
                movesPlayed ++;
            }
        }
        WriteToFile.print("Your Movement is:\n");
        for(int move = 0; move < movesPlayed; move++){
            if(move == 0){
                WriteToFile.print(String.valueOf(moves.charAt(move)));
            }else{
                WriteToFile.print(" " + moves.charAt(move));
            }
        }
        WriteToFile.print("\n\n");

        WriteToFile.print("Your output is:\n");
        for(int line = 0; line < boardInputArray.length; line++){
            for(int character = 0; character < boardInputArray[line].length(); character++){
                if(character == 0){
                    WriteToFile.print(String.valueOf(boardInputArray[line].charAt(0)));
                }else{
                    WriteToFile.print(" " + boardInputArray[line].charAt(character));
                }
            }
            WriteToFile.print("\n");
        }
        WriteToFile.print("\n");

        if(fellIntoHole){
            WriteToFile.print("Game Over!\n");
        }

        WriteToFile.print("Score: " + finalScore);
        WriteToFile.closeFile();
    }




    public void moveHorizontal(int currentLinePos, int currentColumnPos, boolean isRight, int columnPosMax, String[] boardInputArray){
        int targetColumnPos = isRight ? (currentColumnPos + 1) : (currentColumnPos -1);

        if (targetColumnPos > columnPosMax){
            System.out.println("true");
            targetColumnPos = 1;
        }else if(targetColumnPos == 0){
            targetColumnPos = columnPosMax;
        }
        char targetLetter = boardInputArray[currentLinePos-1].charAt(targetColumnPos - 1);
        char ourBall = '*';                               // target letter is the actual destination after we give our command.
                                                         // for example if we have a 'W' on the right side, and 'Y' on the left side of us
                                                         //  and we give the command 'R'. We have the actual destination as 'Y'



        switch (targetLetter){
            case 'Y':
                this.finalScore += 5;
                targetLetter = 'X';
                break;
            case 'R':
                this.finalScore += 10;
                targetLetter = 'X';
                break;
            case 'B':
                this.finalScore -= 5;
                targetLetter = 'X';
                break;
            case 'H':
                targetLetter = ' ';
                ourBall = 'H';
                this.fellIntoHole = true;
                break;
            default:
                break;
        }
        boardInputArray[currentLinePos-1]= boardInputArray[currentLinePos - 1].substring(0,currentColumnPos - 1)
                + targetLetter                       // '*' value gets changed
                + boardInputArray[currentLinePos - 1].substring(currentColumnPos, columnPosMax);


        boardInputArray[currentLinePos - 1]= boardInputArray[currentLinePos - 1].substring(0,targetColumnPos - 1)
                + ourBall                                // letter value gets changed
                + boardInputArray[currentLinePos - 1].substring(targetColumnPos, columnPosMax);

    }


    public void moveVertical(int currentLinePos, int currentColumnPos, boolean isUp, int linePosMax, int columnPosMax, String[] boardInputArray){
        int targetLinePos = isUp ? (currentLinePos - 1) : (currentLinePos + 1);
        if (targetLinePos > linePosMax){
            targetLinePos = 1;
        }else if(targetLinePos == 0){
            targetLinePos = linePosMax;
        }
        char targetLetter = boardInputArray[targetLinePos - 1].charAt(currentColumnPos-1);
        char ourBall = '*';

        switch (targetLetter){
            case 'Y':
                this.finalScore += 5;
                targetLetter = 'X';
                break;
            case 'R':
                this.finalScore += 10;
                targetLetter = 'X';
                break;
            case 'B':
                this.finalScore -= 5;
                targetLetter = 'X';
                break;
            case 'H':
                targetLetter = ' ';
                ourBall = 'H';
                this.fellIntoHole = true;
                break;
        }


        boardInputArray[currentLinePos - 1] = boardInputArray[currentLinePos - 1].substring(0, currentColumnPos - 1)
                + targetLetter   // '*' gets changed
                + boardInputArray[currentLinePos - 1].substring(currentColumnPos, columnPosMax);


        boardInputArray[targetLinePos - 1] = boardInputArray[targetLinePos - 1].substring(0, currentColumnPos - 1)
                + ourBall     //targeted letter gets changed
                + boardInputArray[targetLinePos - 1].substring(currentColumnPos, columnPosMax);

    }

}
