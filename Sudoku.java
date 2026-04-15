import java.util.Scanner;
import java.util.ArrayList;
public class Sudoku{
    //Initialize board
    public static Scanner sc = new Scanner(System.in); 
    public static int[][] board  = new int[9][9];
    public static void main(String[] args){
        //Fills the entire board with 0s
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                board[r][c] = 0;
            }
        }
        //Fills in 3 3x3 boxes with randomized
        //Top left 3x3 initialized
        //Center 3x3 initialized
        //Bottom right 3x3 initialized
        for (int i = 0; i <= 6; i+=3){
            int[] shuffle = shuffle();
            int index = 0;
            for (int r = i; r < i + 3; r++){
                for (int c = i; c < i + 3; c++){
                    board[r][c] = shuffle[index];
                    index++;
                }
            }
        }
        //start at the origin (0,0)!
        fillRestOfBoard(0, 0);
        printBoard(board);
        System.out.println();
        System.out.println("~~~~~~~~~~~");
        int[][] unsolved = new int[9][9];
        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board[0].length; c++){
                unsolved[r][c] = board[r][c];
            }
        }
        //copy the answers into a new board thats going to lose some values
        for (int i = 0; i < 10; i++){
            int ran1 = (int)(Math.random() * 9);
            int ran2 = (int)(Math.random() * 9);
            unsolved[ran1][ran2] = 0;
        }
        
        printBoard(unsolved);
        System.out.println("Fill in the 0s to solve the Board!");
        boolean gameCont = true;
        while (gameCont == true && checkIfSolved(unsolved) == false){
            System.out.println("Enter a Row");
            int row = sc.nextInt();
            System.out.println("Enter a Column");
            int col = sc.nextInt();
            //check if the selected coordinates do not already have a 0
            if (unsolved[row][col] != 0){
                System.out.println("This spot is already filled");
            }
            else{
                System.out.println("Enter a number from 1-9");
                int playerChoice = sc.nextInt();
                if (board[row][col] == playerChoice)
                    unsolved[row][col] = playerChoice;
                else
                    System.out.println("Didn't work");
            }
            printBoard(unsolved);
            System.out.println("Continue? \n" + 
                               "> 0 for yes \n" + 
                               "> 1 for no \n");
            int choice = sc.nextInt();
            if (choice == 1)
                gameCont = false;
        }
        if (checkIfSolved(unsolved) == true)
            System.out.println("Congrats on solving the board; heres a cookie (Insert cookie here never)");
        

    }
    
    //check if the unsolved array contains any 0s still
    public static boolean checkIfSolved(int[][] arr){
        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board[0].length; c++){
                if (arr[r][c] == 0){
                    return false;    
                }
            }
        }
        return true;
    }
    
    //set every number to a value and check if it works in the puzzle
    public static boolean fillRestOfBoard(int r, int c){
        //base recursion result
        if (r == 9){
            return true;
        }
        //if the column is over the max it goes to the next row
        if (c == 9){
            return fillRestOfBoard(r + 1, 0);
        }
        //check if the cell is unfilled
        if (board[r][c] != 0){
            return fillRestOfBoard(r, c + 1);
        }
        else{
            //make 
            int[] shuffled = shuffle();
            for (int n : shuffled){
                if (followRule(r, c, n) == true){
                    board[r][c] = n;
                    if (fillRestOfBoard(r, c + 1) == true){
                        return true;
                    }
                    else{
                        board[r][c] = 0;
                    }
                }
            }
            return false;
        }
    }
    
    //print out the board in the nice format :)
    public static void printBoard(int[][] arr){
        for (int r = 0; r < arr.length; r++){
            for (int c = 0; c < arr[0].length; c++){
                System.out.print(arr[r][c] + " ");
                if (c == 2 || c == 5){
                    System.out.print("| ");
                }
            }
            System.out.println();
            if (r == 2 || r == 5){
                System.out.println("---------------------");
            }
        }
    }

    //Shuffle an array of numbers that be used a reference 
    public static int[] shuffle(){
        int[] result = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < 9; i++){
            int ran1 = (int)(Math.random() * 9);
            int ran2 = (int)(Math.random() * 9);
            int temp = result[ran1];
            result[ran1] = result[ran2];
            result[ran2] = temp;
        }
        return result;
    }
    
    //Takes all three rule outcomes and determines if number works
    public static boolean followRule(int row, int col, int n){
        boolean rowB = rowRule(row, col, n);
        boolean colB = colRule(row, col, n);
        boolean boxB = boxRule(row, col, n);
        return (rowB == true && colB == true && boxB == true);
    }
    
    //checks if the number doesn't repeat in its row
    //check all the COLUMNS for repeats
    public static boolean rowRule(int r, int c, int num){
        ArrayList<Integer> rowCheck = new ArrayList<Integer>(board[0].length);
        for (int col = 0; col < board[0].length; col++){
            rowCheck.add(board[r][col]);
        }
        for (int i = 0; i < rowCheck.size(); i++){
            if (board[r][i] == num){
                return false;
            }
        }
        return true;
    }
    
    //checks if the number doesn't repeat in its column
    //checks all the ROWS for repeats
    public static boolean colRule(int r, int c, int num){
        ArrayList<Integer> colCheck = new ArrayList<Integer>(board.length);
        for (int row = 0; row < board.length; row++){
            colCheck.add(board[row][c]);
        }
        for (int i = 0; i < colCheck.size(); i++){
            if (board[i][c] == num){
                return false;
            }
        }
        return true;
    }
    
    //checks if the number doesn't repeat in its box
    public static boolean boxRule(int r, int c, int num){
        int rowStart = (r / 3) * 3;
        int colStart = (c / 3) * 3;
        for (int row = rowStart; row  < rowStart + 3; row++){
            for (int col = colStart; col < colStart + 3; col++){
                if (board[row][col] == num){
                    return false;
                }
            }                
        }
        return true;
    }
}
