import java.util.Scanner;

public class Main {
    public static Scanner user_input = new Scanner(System.in);

    //possible letters
    public static final char[] LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    //reset colour
    public static final String ANSI_RESET = "\u001B[0m";
    //bold text
    public static final String ANSI_BOLD = "\u001B[1m";

    //colour codes with same index as their corresponding letters
    public static final String[] ANSI_CODE = {"\u001B[31;1m", "\033[35m", "\u001B[33;1m", "\u001B[32;1m", "\u001B[36;1m", "\u001B[34;1m", "\u001B[35;1m", "\u001B[37;1m"};

    //emojis
    public static final String SMILE_EMOJI = "\uD83D\uDE04";
    public static final String HUG_EMOJI = "\uD83E\uDD17";
    public static final String WAVE_EMOJI = "\uD83D\uDC4B";
    public static final String POINT_EMOJI = "\uD83D\uDC49";
    public static final String QUESTION_EMOJI = "\u2753";
    public static final String STAR_EMOJI = "\u2B50";
    public static final String EXCLAIMATION_EMOJI = "\u2757";
    public static final String FIREWORK_EMOJI = "\uD83C\uDF86";

    //main
    public static void main (String[] args) {
        char[][] boardCode = new char[10][]; //board for guesses
        int[][] boardResult = new int[10][]; //results for every guess

        char[] secretCode; //secret code

        boolean repeat; //if user wants the letters to repeat in the code
        boolean playAgain; //if user wants to play again

        int guesses = 10; //# of guesses
        int score = 0; //# of guesses used
        int scoreTotal = 0; //# of guesses used in the whole game
        int scoreBest = 21; //user's best score

        int roundCounter = 1; //# of rounds played
        int streakCounter = 0; //how many rounds player won in a row

        String name; //user's name

        //welcome
        welcome(); 

        name = getName();

        //instructions
        instructions(name);

        //game starts
        do {
            //ask user if they want repeated characters in code
            repeat = yesOrNo("do you want the code to have repeated letters?", name);
            
            //generate random secret code
            secretCode = generateCode(repeat);

            //starting game
            start(roundCounter);

            //for every guess
            for (int i = 0; i < guesses; i++) {
                //get user's guess
                boardCode[i] = getInput();

                //compare guess to secret code to get result code 
                boardResult[i] = compareCode(boardCode[i], secretCode);

                //output result
                printBoard(boardCode, boardResult, i+1);

                //check and output to user if they won or lost
                if (checkWin(boardResult[i])) {
                    System.out.println("You win " + FIREWORK_EMOJI);
                    streakCounter++;
                    score = i+1;
                    break;
                }
                else if (i == guesses-1) {
                    System.out.println("You lose");
                    streakCounter = 0;
                    score = 20;
                }
            }

            scoreTotal += score;

            //tell user what secret code is
            printAnswer(secretCode);

            //prints the user's score
            System.out.println("Score: " + score);
            System.out.println("Total Score: " + scoreTotal);

            //highlight best if user beat their best
            if (score < scoreBest) {
                scoreBest = score;
                System.out.println(ANSI_BOLD + "Best Score: " + scoreBest + ANSI_RESET + STAR_EMOJI);
            }
            else {
                System.out.println("Best Score: " + scoreBest);
            }
            
            System.out.println("Average Score: " + Math.round((scoreTotal*1.0/roundCounter)*10)/10.0);
            System.out.println("Streak: " + streakCounter);

            //ask if user wants to play again
            playAgain = yesOrNo("do you want to play again?", name);

            roundCounter++;
        }
        while (playAgain);   

        user_input.close();
    }

    //prints a line seperator made of length num*2
    public static void printLine(boolean top, int num, boolean spaceBefore, boolean spaceAfter) {
        if (spaceBefore) {
            System.out.println();
        }
        
        if (top) {
            System.out.print("┌");
        }
        else {
            System.out.print("└");
        }

        for (int i = 0; i < num; i++) {
            System.out.print("─");
        }

        System.out.print("⋆⋅☆⋅⋆");

        for (int i = 0; i < num; i++) {
            System.out.print("─");
        }

        if (top) {
            System.out.println("┐");
        }
        else {
            System.out.println("┘");
        }

        if (spaceAfter) {
            System.out.println();
        }
    }

    //welcomes user
    public static void welcome() {
        printLine(true, 16, false, false);
        System.out.println(" " + SMILE_EMOJI + " Welcome to the Mastermind Game " + SMILE_EMOJI + " ");
        printLine(false, 16, false, true);
    }

    //get the user's name
    public static String getName() {
        String name;

        System.out.print(HUG_EMOJI + " What is your name?: ");
        name = user_input.nextLine();

        return name;
    }

    //output instructions
    public static void instructions(String name) {

        System.out.println(WAVE_EMOJI + " Hello " + name + "!");

        //ask user if they want to read instructions
        boolean readRules = yesOrNo("do you want to read the rules?", name);

        //output instructions
        if (readRules) {
            System.out.println("\n" + STAR_EMOJI + ANSI_BOLD + " Instructions\n" + ANSI_RESET);

            System.out.print(POINT_EMOJI + " A 4 letter code is generated which is made up of the letters: \n - ");
            
            for (int i = 0; i < 8; i++) {
                System.out.print(ANSI_CODE[i] + LETTERS[i] + " " + ANSI_RESET);
            }
            
            System.out.print("\n\n" + POINT_EMOJI + " You then guess the 4 letter code, trying to duplicate the exact letters and positions of the secret code. " +
            "Each guess is made by typing in a row of letters not seperated by spaces and then pressing enter.\n ex. ");
            
            for (int i = 0; i < 4; i++) {
                System.out.print(ANSI_CODE[i] + LETTERS[i] + ANSI_RESET);
            }

            System.out.println("\n\n" + POINT_EMOJI + " You'll be given information about your guess through a code made up of " + 
            ANSI_CODE[0] + "x" + ANSI_RESET + "'s and o's (ex. " + ANSI_CODE[0] + "xx" + ANSI_RESET + "). \n" +
            " - " + ANSI_CODE[0] + "x" + ANSI_RESET + " represents that there is one character " + 
            "that is the correct letter and in the correct position in the code that you guessed. \n" +
            " - o represents there is one character that is the correct letter in the code that you guessed. \n" + 
            EXCLAIMATION_EMOJI + " The " + ANSI_CODE[0] + "x"+ ANSI_RESET + "'s and o's are not in order.\n");

            System.out.println(POINT_EMOJI + " You get to guess 10 times and win when the code that you guessed matches the secret code " +
            "perfectly. \nAt this point, the output code will be " + ANSI_CODE[0] + "xxxx" + ANSI_RESET + ".\n");

            System.out.println(EXCLAIMATION_EMOJI + "The score is the number of guesses you used so the closer it is to 0 the better! \n");

            System.out.print(POINT_EMOJI + " Press enter to continue");

            user_input.nextLine();
        }
    }

    //ask and get yes or no input from user
    public static boolean yesOrNo(String msg, String name) {
        //ask questions
        System.out.print("\n" + QUESTION_EMOJI + name + ", " + msg + " (Y/N): ");

        //get input
        String input = user_input.nextLine().toUpperCase();
        
        //error handling
        while (!input.equals("Y") && !input.equals("N")) {
            System.out.print("Please Enter Y or N: ");
            input = user_input.nextLine().toUpperCase();
        }

        //return
        if (input.equals("Y")) {
            return true;
        }

        return false;
    }

    //start game, printing possible characters in the code with their corresponding letters
    public static void start(int roundCounter) {
        System.out.println("\n" + HUG_EMOJI + " Let's get started! " + HUG_EMOJI + "\nHere are the possible characters");
        printLine(true, 1, false, false);
        
        for (int i = 0; i < 4; i++) {
            System.out.print(" " + ANSI_CODE[i] + LETTERS[i] + ANSI_RESET);
        }

        System.out.println();

        for (int i = 4; i < 8; i++) {
            System.out.print(" " + ANSI_CODE[i] + LETTERS[i] + ANSI_RESET);
        }

        printLine(false, 1, true, false);

        System.out.println("\n" + STAR_EMOJI + ANSI_BOLD + " Round " + roundCounter + ANSI_RESET + "\n");
    }

    //generates a secret code
    public static char[] generateCode(boolean repeat) {
        int randomNum;

        //temp array to store code
        char[] temp = new char[4];

        //for every letter in the code
        for (int i = 0; i < 4; i++) {
            //if the user wants repeated letters or no 
            if (repeat) {
                //generate random # corresponding to the index of the letter in the letters array
                randomNum = (int) Math.floor(Math.random()*8);
            }
            else {
                //only use indices (letters) that haven't been added into the code already
                do {
                    randomNum = (int) Math.floor(Math.random()*8);
                } while (inArray(LETTERS[randomNum], temp)); //checking if the current letter is already in the code
            }

            //adds letter to code
            temp[i] = LETTERS[randomNum];
        }

        return temp;
    }

    //get the code the user inputted
    public static char[] getInput() {
        System.out.println("Enter code:");
        
        //user input
        String temp = user_input.nextLine();

        //user input transformed into correct formatting
        char[] inputCode = new char[4];

        //checks whether the user input is valid
        boolean notValid = stringNotValid(temp);

        //while the input isn't valid, prompt the user for a new input
        while (notValid) {
            System.out.println("Please enter a 4 letter code with valid letters (ABCDEFGH), not seperated by spaces:");
            temp = user_input.nextLine();

            notValid = stringNotValid(temp);
        }

        //put the input into an array
        for (int i = 0; i < 4; i++) {
            inputCode[i] = Character.toUpperCase(temp.charAt(i));
        }
        //return the array
        return inputCode;
    }

    //checks if the user's guess is a valid guess
    public static boolean stringNotValid(String s) {
        //guess is 4 characters
        if (s.length() != 4) {
            return true;
        }

        //every character is either ABCDEFG
        for (int i = 0; i < 4; i++) {
            if (charNotValid(Character.toUpperCase(s.charAt(i)))) {
                return true;
            }
        }

        return false;
    }

    //char c is either ABCDEFG
    public static boolean charNotValid(char c) {
        for (int i = 0; i < 8; i++) {
            if (c == LETTERS[i]) {
                return false;
            }
        }

        return true;
    }

    //checks if c is in array temp
    public static boolean inArray(char c, char[] temp) {
        for (int i = 0; i < 4; i++) {
            if (temp[i] == c) {
                return true;
            }
        }

        return false;
    }

    //compares how close the user input matches with the secret code
    public static int[] compareCode(char[] inputCode, char[] code) {
        int[] result = new int[2]; //# of x and o
        boolean[] found = new boolean[4]; //if character in secret code at that index has been used already

        //check if any characters match the secret code perfectly (same pos, same char)
        for (int i = 0; i < 4; i++) {
            if (inputCode[i] == code[i]) {
                result[0]++;
                found[i] = true;
            }
        }

        //check if any characters match but position doesn't (diff pos, same char)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (inputCode[i] == code[j] && !found[j] && i != j) {
                    result[1]++;
                    found[j] = true;
                }
            }
        }

        return result;
    }

    //check if user correctly guessed secret code
    public static boolean checkWin(int[] result) {
        if (result[0] == 4) {
            return true;
        }

        return false;
    }

    //outputs the entire board
    public static void printBoard(char[][] boardCode, int[][] boardResult, int rows) {
        printLine(true, 8, false, false);
        System.out.println(" The current board is:");
        
        //for every guess
        for (int i = 0; i < rows; i++) {
            //guess #
            if (i != 9) {
                System.out.print(" " + (i+1) + ". ");
            }
            else {
                System.out.print(" " + (i+1) + ".");
            }
            
            //guessed code with colours
            for (int j = 0; j < 4; j++) {
                System.out.print(ANSI_CODE[indexOf(boardCode[i][j])] + boardCode[i][j] + ANSI_RESET);
            }

            System.out.print(" | ");

            //# of same char same position
            for (int j = 0; j < boardResult[i][0]; j++) {
                System.out.print(ANSI_CODE[0] + 'x' + ANSI_RESET);
            }

            //# of same char diff position
            for (int j = 0; j < boardResult[i][1]; j++) {
                System.out.print('o');
            }

            System.out.println();
        }
        printLine(false, 8, false, true);
    }

    //index of c in the letters array
    //used to determine colour of text
    public static int indexOf(char c) {
        for (int i = 0; i < 8; i++) {
            if (c == LETTERS[i]) {
                return i;
            }
        }

        return 0;
    }

    //outputs the secret code with colours
    public static void printAnswer(char[] code) {
        System.out.print("The code was: ");

        for (int i = 0; i < 4; i++) {
            System.out.print(ANSI_CODE[indexOf(code[i])] + code[i] + ANSI_RESET);
        }

        System.out.println("\n");
    }
}
