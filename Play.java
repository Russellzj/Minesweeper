import java.util.Scanner;
public class Play {
    public static void main(String[] args) {

        boolean continuePlay;
        Scanner sc = new Scanner(System.in);
        do {
            int xAxis = 9; // Size of X Axis
            int yAxis = 9; // Size of Y Axis
            MineSweeper game = new MineSweeper(xAxis, yAxis);

            game.setBombCount(); //Retrieves number of bombs that the user wishes to plant into the map
            game.printPlayerMap(); //Prints an empty map since the bombs have yet to be planted
            game.play(); //Plays the rest of the game until the user wins or loses

            //Ask the player if they want to play the game again
            String answer;
            do {
                System.out.println("Do you want to play again? Y or N");
                answer = sc.next();
                continuePlay = "Y".equalsIgnoreCase(answer);
                if (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N"))
                    System.out.println("Please enter Y or N as your answer.");
            } while (!(answer.equalsIgnoreCase("Y")) && !(answer.equalsIgnoreCase("N")));
        } while (continuePlay);
        sc.close();
    }
}
