import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Main class - Where the game takes place
 * @author Sergio Vasquez
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<String> choices = new ArrayList<>();
        // All possible choices
        Collections.addAll(choices,"p","r","s","q");

        String fileName = "rps.txt";
        Computer computer = new Computer();

        String pattern = "";
        boolean gameOver = false;
        final int PATTERN_LENGTH = 4;
        int computerWins = 0;
        int humanWins = 0;
        int totalGames = 0;

        File file = new File(fileName);

        if( file.exists() ) {
            System.out.println("Would you like to load computer data?");

            if ( CheckInput.getYesNo() ) {
                pattern = computer.readFile(new File(fileName));
            }

        }

        while( !gameOver ){
            System.out.println("(r)ock, (p)aper, (s)cissors or (q)uit:");
            String userChoice = CheckInput.getStringRange(choices);

            if( !userChoice.equals("q") ){
                char computerChoice = computer.makePrediction(pattern);

                int decision = whoWon(userChoice.charAt(0), computerChoice);

                /* 1 if the human wins
                   -1 if the computer wins
                   else game resulted in a tie
                 */
                if( decision == 1 ){
                    humanWins++;
                    System.out.println("You win!");
                    System.out.println(userChoice + " beats " + computerChoice);
                } else if( decision == -1 ){
                    computerWins++;
                    System.out.println("You lose!");
                    System.out.println(computerChoice + " beats " + userChoice);
                } else {
                    System.out.println("It's a draw!");
                    System.out.println("The computer and you both chose " + userChoice);
                }
                totalGames ++;

                double computerWinningPercentage = computerWins == 0 ? 0.0 : ((double) computerWins / (computerWins + humanWins)) * 100.0;
                double humanWinningPercentage = humanWins == 0 ? 0.0 : ((double) humanWins / (computerWins + humanWins)) * 100.0;

                // Print all useful information
                System.out.println("Computer Wins: " + computerWins);
                System.out.printf("Computer Win percentage: %.2f\n", computerWinningPercentage);
                System.out.println("Human Wins: " + humanWins);
                System.out.printf("Human Win percentage: %.2f\n", humanWinningPercentage);

                pattern += userChoice;

                if( pattern.length() >= PATTERN_LENGTH ){
                    /* Check if the pattern length has gone beyond the specified
                    * length if so fix the invariant by taking a substring of
                    * the current length */
                    int beginIndex = pattern.length() == PATTERN_LENGTH ? 0 : 1;
                    pattern = pattern.substring(beginIndex, beginIndex + PATTERN_LENGTH);
                    computer.storePattern(pattern);
                }
            } else{
                gameOver = true;
            }
        }

        // Only ask user to save file if they at least played one game
        if( totalGames >= 1 ) {

            System.out.println("Would you like to store the computer data for future games?");
            if ( CheckInput.getYesNo() ) {
                System.out.println("Saving data...");
                computer.saveMapToFile(new File(fileName));
            }

        }

        System.out.println("Goodbye Now!");
    }
    /*
     * Checks if the game ended in a draw or someone won
     * @param humanChoice the choice of the human represented as a character
     * @param computerChoice the choice of the computer represented as a character
     * @return -1 if the computer won, 0 if the game was a draw and 1 if the human won
     */
    public static int whoWon( char humanChoice, char computerChoice ){
        if( computerChoice == humanChoice ){
            return 0;
        } else if( computerChoice == 'r' && humanChoice == 'p'
                || computerChoice == 's' && humanChoice == 'r'
                || computerChoice == 'p' && humanChoice == 's'){
            return 1;
        } else {
            return -1;
        }
    }
}
