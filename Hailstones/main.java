//Emily Zhang
//January 21, 2021
//This program is a two player hailstones game
import java.util.Scanner;

//player class
class Player {
  String name; //player's name
  int score; //player's score
  int input; //player's input
  int max; //max number in the player's sequence
  int length; // length of the player's sequence
  String colour; //the player's colour
}

//main class
public class Main {
  //2 objects for the the 2 players
  public static Player player1 = new Player();
  public static Player player2 = new Player();

  //Scanner
  public static Scanner user_input = new Scanner(System.in);

  //end colour string
  public static final String ANSI_RESET = "\u001B[0m";

  //main method
  public static void main(String[] args) {
    int rounds; //number of rounds to play

    //declare colour for each player
    player1.colour = "\u001B[36m";
    player2.colour = "\u001B[31m";

    //welcome the user
    System.out.println("Welcome to the Hailstones Game!");
    //gives the user the instructions (optional)
    if (checkYesNo("Do you want to read the instructions?")) {
      printInstructions();
      System.out.print("Press Enter to Continue");
      user_input.nextLine();
    }

    //line-break
    printLine('-', 60, "", "");

    //gets input: user's names
    System.out.print("\n" + player1.colour + "Name (Player 1): " + ANSI_RESET);
    player1.name = user_input.nextLine().toUpperCase();
    System.out.print(player2.colour + "Name (Player 2): " + ANSI_RESET);
    player2.name = user_input.nextLine().toUpperCase();

    //gets input: number of rounds the user wants to play
    //error trapping
    do {
      System.out.println("\nHow many rounds would you like to have?");
      rounds = user_input.nextInt();
    } while (rounds <= 0);


    //game starts
    System.out.println("\nLet the game begin!\n");

    do {
      //reset score
      player1.score = 0;
      player2.score = 0;
      
      //loop for chosen number of rounds
      for (int i = 0; i < rounds; i++) {
        //line-break
        printLine('-', 60, "", "");
        System.out.println("\nRound " + (i+1));

        //get the 2 players' inputs
        player1.input = getInput(player1.name, player1.colour);
        player2.input = getInput(player2.name, player2.colour);
        
        //prints the hailstone sequence
        //prints max value of each sequence
        //prints length of each sequence
        printHailstone(player1);
        printHailstone(player2);

        //line-break
        printLine('-', 30, "", "");

        //prints the user with the higher maximum value
        //increase their score
        System.out.println("Maximum value:");
        printMaxValue();

        //prints the user with the longer sequence
        //increase their score
        System.out.println("\nLongest sequence:");
        printLongestSequence();

        //prints the users' scores up to this point
        System.out.println("\n" + player1.colour + player1.name + ANSI_RESET + ": " + player1.score);
        System.out.println(player2.colour + player2.name + ANSI_RESET + ": " + player2.score + "\n");
      }

      //print the winner
      printWinner();

    //ask if user wants to play again
    } while (checkYesNo("Do you want to play again?"));

    //close scanner
    user_input.close();
  }

  //gets the number the user wants to use to create their hailstone sequence
  public static int getInput(String name, String colour) {
    int input;
    //error trap
    do {
      System.out.print(colour + name + ANSI_RESET + ", enter an integer in the range of 1-100 inclusive: ");
      input = user_input.nextInt();
      user_input.nextLine();
    } while (input < 1 || input > 100);
    
    //return value
    return input;
  }

  //print the instructions
  public static void printInstructions() {
    System.out.println("\nHow to Play Hailstones");
    System.out.println("Both players must enter a number that is in the range of 1-100 inclusive. If the number is odd you multiply it by 3 and add 1, otherwise you divide it by 2. Stop when the number reaches 1 and this produces a sequence.\n");

    System.out.println("How to Win");
    System.out.println("The player with highest overall value in the sequence gets 5 points and the player with the longer sequence of numbers gets 1 point for each extra number he/she has over the opponent. The points are accumulated and the player with the most points at the end of the rounds win.\n");
  }

  //prints the hailstone sequence based on user input
  //prints max value in the sequence
  //prints length of the sequence
  public static void printHailstone(Player player) {
    //initialize value
    player.length = 1;
    player.max = 1;

    //prints player's name
    System.out.println("\n" + player.colour + player.name + ":" + ANSI_RESET);

    //perform operations until the number is equalled to 1
    while (player.input != 1) {
      //print value
      System.out.print(numPadding(player.input) + ", ");

      //find the max value
      if (player.input > player.max) {
        player.max = player.input;
      }
      
      //perform operations on the input
      //value is odd
      if (player.input%2 == 1) {
        player.input *= 3;
        player.input++;
      }
      //value is even
      else {
        player.input /= 2;
      }

      //keep 10 numbers per line
      if (player.length%10 == 0) {
        System.out.println();
      }

      //count length of sequence
      player.length++;
    }

    //print last value
    System.out.println(numPadding(1));

    //print max valuelength of sequence
    System.out.println(player.colour + "\nMaximum value = " + ANSI_RESET + player.max);
    //print length of sequence
    System.out.println(player.colour + "Length of sequence = " + ANSI_RESET + player.length);
  }

  //padding numbers
  public static String numPadding(int n) {
    String numStr = Integer.toString(n);
    int len = numStr.length();

    //pad all numbers to 4 digits
    for (int i = 0; i < 4 - len; i++) {
      numStr = " " + numStr;
    }

    return numStr;
  }

  //checks and prints which user's sequence has the greatest value
  public static void printMaxValue() {
    int points = 5;
    String statement;

    //increase score
    //concatenate statement
    
    //player 1 has a greater value
    if (player1.max > player2.max) {
      player1.score += points;
      statement = player1.colour + player1.name + ANSI_RESET + 
      " had the highest value in their sequence with " + 
      player1.max + "! (+" + points + " points)";
    }
    //player 2 has a greater value
    else if (player2.max > player1.max) {
      player2.score += points;
      statement = player2.colour + player2.name + ANSI_RESET + 
      " had the highest value in their sequence with " + 
      player2.max + "! (+" + points + " points)";
    }
    //the values are equal
    else {
      statement = "Tie. You both have the same highest value with " 
      + player1.max + ". (+0 points)";
    }

    //print statement
    System.out.println(statement);
  }

  //checks and prints which user's sequence is longer
  public static void printLongestSequence() {
    //calculate points
    int points = Math.abs(player1.length - player2.length);
    String statement;

    //increase score
    //concatenate statement

    //player 1's sequence is longer
    if (player1.length > player2.length) {
      player1.score += points;
      statement = player1.colour + player1.name + ANSI_RESET + 
      " had the longer sequence with " + player1.length + 
      " values! (+" + points + " points)";
    }
    //player 2's sequence is longer
    else if (player2.length > player1.length) {
      player2.score += points;
      statement = player2.colour + player2.name + ANSI_RESET + 
      " had the longer sequence with " + player2.length + 
      " values! (+" + points + " points)";
    }
    //lengths are equal
    else {
      statement = "Tie. The 2 sequences are the same length with " +
      player1.length + " values. (+" + points + " points)";
    }

    //print statement
    System.out.println(statement);
  }

  //print the winner of the game
  public static void printWinner() {
    //player 1 wins
    if (player1.score > player2.score) {
      printBorder(player1.name, player1.colour);
    }
    //player 2 wins
    else if (player2.score > player1.score) {
      printBorder(player2.name, player2.colour);
    }
    //tie
    else {
      System.out.println("\nTie.");
    }
  }

  //prints a border around the winner
  public static void printBorder(String name, String colour) {
    //length of name
    int len = name.length();

    //print the winner with a border around
    printLine('-', len + 9, colour, ANSI_RESET);
    System.out.println(colour + "|" + name + ANSI_RESET + " wins!!" + colour + "|" + ANSI_RESET);
    printLine('-', len + 9, colour, ANSI_RESET);
  }

  //prints a line
  public static void printLine(char c, int n, String colour, String end) {
    for (int i = 0; i < n; i++) {
      System.out.print(colour + c + end);
    }
    System.out.println();
  }
  
  //checks if the user's answer is yes or no
  public static boolean checkYesNo(String msg) {
    String choose;

    //error trapping
    do {
      System.out.print("\n" + msg + " (yes/no): ");
      choose = user_input.nextLine().toLowerCase();
    } while (!(choose.equals("yes") || choose.equals("no")));

    return choose.equals("yes");
  }
}
