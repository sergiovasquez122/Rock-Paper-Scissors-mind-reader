import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Computer class - Representation of a single computer
 * @author Sergio Vasquez
 */
public class Computer {
    /** Contains the pattern and the amount of time it has occurred */
    private HashMap<Pattern, Integer> patterns;

    /**
     * Constructor - Creates a Computer object
     */
    public Computer()
    {
        patterns = new HashMap<>();
    }

    /**
     * Computer makes a prediction given the pattern p
     * @param p string that represents the current pattern
     * @return 'r', 'p' or ,'s' depending on the pattern p
     */
    public char makePrediction(String p){
        final int THRESHOLD = 4;
        if( p.length() == THRESHOLD ){

            // Throw away the oldest choice and check most likely pattern
            String possibleCandidates[] = new String[3];
            possibleCandidates[0] = p.substring(1) + 'r';
            possibleCandidates[1] = p.substring(1) + 's';
            possibleCandidates[2] = p.substring(1) + 'p';

            ArrayList<Integer> frequencyCount = new ArrayList<>();
            frequencyCount.add( patterns.getOrDefault(new Pattern(possibleCandidates[0]), 0));
            frequencyCount.add( patterns.getOrDefault(new Pattern(possibleCandidates[1]), 0));
            frequencyCount.add( patterns.getOrDefault(new Pattern(possibleCandidates[2]), 0));

            int largestElement = Collections.max(frequencyCount);

            ArrayList<Integer> canidatesIndices = new ArrayList<>();

            for(int i = 0; i < frequencyCount.size(); ++i){
                if( largestElement == frequencyCount.get(i) ){
                    canidatesIndices.add(i);
                }
            }

            String chosenCandidate = "";

            if( canidatesIndices.size() == 1 ){
                chosenCandidate = possibleCandidates[ canidatesIndices.get(0) ];

            } else if ( canidatesIndices.size() == 2 ){
                // If more than one candidate chose one randomly
                int chosenIndex = new Random().nextInt( canidatesIndices.size() );
                chosenCandidate = possibleCandidates[ canidatesIndices.get(chosenIndex) ];
            }

            if( !chosenCandidate.isEmpty() ){
                // Find the most used hand and chose the hand that beats it
                int rockCount = chosenCandidate.length() - chosenCandidate.replaceAll("r","").length();
                int paperCount = chosenCandidate.length() - chosenCandidate.replaceAll("p", "").length();
                int scissorCount = chosenCandidate.length() -chosenCandidate.replaceAll("s", "").length();

                if( rockCount >= paperCount && rockCount >= scissorCount ){
                    return 'p';
                } else if( paperCount >= rockCount && paperCount >= scissorCount ){
                    return 's';
                } else{
                    return 'r';
                }
            }
        }
        // Choose a random item with equal likelihood and return it
        Random random = new Random();
        char choices[] = {'r', 'p', 's'};
        int randomIndex = random.nextInt(choices.length);
        return choices[randomIndex];
    }

    /**
     * stores the pattern p into the computer memory
     * @param p the pattern to be stored
     */
    public void storePattern(String p){
        Pattern potentialNewPattern = new Pattern(p);

        // Increment the pattern if it is found else create a new one
        if( patterns.containsKey(potentialNewPattern) ){
            Integer count = patterns.get(potentialNewPattern);
            count++;
            patterns.put(potentialNewPattern, count);
        } else {
            patterns.put(potentialNewPattern, 1);
        }

    }

    /**
     * Save the current hashmap to the specified file
     * @param f The file to save the hashmap to
     */
    public void saveMapToFile(File f){
        try{
            PrintWriter printWriter = new PrintWriter(f);

                for (Map.Entry<Pattern, Integer> keyValuePair : patterns.entrySet()) {
                    String patternName = keyValuePair.getKey().getPattern();
                    // file content will be defined as [pattern=frequency]
                    printWriter.println(patternName + "=" + keyValuePair.getValue());
                }

            printWriter.close();
        } catch(FileNotFoundException fnf){
            System.out.println("File was not found! Aborting save");
        }
    }

    /**
     * Reads the files content into the hashmap
     * @param f the file to be read
     * @return The most occurring pattern in the file
     */
    public String readFile(File f){
        String mostFrequentPattern = "";
        int mostFrequentPatternCount = 0;
        try{

            Scanner read = new Scanner(f);

            while( read.hasNextLine() ){

                    String line[] = read.nextLine().split("=");

                    // file content is defined as [pattern, int]
                    String pattern = line[0];
                    int currentPatternFrequency = Integer.parseInt(line[1]);

                    // Find the pattern with the highest frequency to return
                    if (currentPatternFrequency > mostFrequentPatternCount) {
                        mostFrequentPatternCount = currentPatternFrequency;
                        mostFrequentPattern = pattern;
                    }

                    patterns.put(new Pattern(pattern), currentPatternFrequency);
                }

        } catch(FileNotFoundException fnf){
            System.out.println("File was not found! Setting to default mode");
        }
        return mostFrequentPattern;
    }
}
