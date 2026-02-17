import java.util.Scanner;
import java.util.ArrayList;

/**
 * wordle
 *
 * @JasonChow
 * @1.0
 */

// to do:
// make the guesses capitalized (upper case)
public class wordle
{
    private static String RESET = "\u001B[0m";
    private static String WHITE_TEXT = "\u001B[37m";
    private static String BLACK_BG = "\u001B[40m";
    private static String GREEN_BG = "\u001B[42m";
    private static String YELLOW_BG = "\u001B[43m";
    
    private static Scanner input = new Scanner (System.in);
    private static String[] words = {"smash", "audio", "hitch", "watch", "eaten", "trash", "gamer", "foggy", "money", "array", "error", "tease"};
    private static String[] words4 = {"what", "fear", "gene", "mess", "hook", "bulb", "scan", "zone", "easy", "cell", "head", "lick", "bury", "myth", "work", "pony"};
    private static String[] words6 = {"locker", "tongue"};
    private static String answer = words[(int)(Math.random() * words.length)];
    private static String guess = null;
    private static int length = 5;
    private static int guesses = 6;
    private static int guessesLeft = guesses;
    private static ArrayList<String> wordsCustom = new ArrayList<String>();
    private static String answerCustom;
    private static int games = 0;
    private static int wins = 0;
    private static double winRate;
    private static boolean isCustom = false;
    
    public static void main(String[] args)
    {
        String choice;
        while(true)
        {
            System.out.println("enter start, settings, custom, stats, or stop");
            choice = input.nextLine();
            if(choice .equalsIgnoreCase ("start"))
            {
                start();
            }
            else if(choice .equalsIgnoreCase ("stop"))
            {
                break;
            }
            else if(choice .equalsIgnoreCase ("settings"))
            {
                settings();
            }
            else if(choice .equalsIgnoreCase ("custom"))
            {
                custom();
                isCustom = true;
            }
            else if(choice .equalsIgnoreCase ("stats"))
            {
                stats();
            }
            reset();
        }
    }
    
    public static void settings()
    {
        System.out.println("How many letters? 4 to 6 (default: 5)");
        length = input.nextInt();
        while(length < 4 || length > 6)
        {
            System.out.println("invalid length, enter length again");
            length = input.nextInt();
        }
        System.out.println("How many guesses? (default: 6)");
        guesses = input.nextInt();
        while(guesses < 1)
        {
            System.out.println("you can't have less than 1 guess, enter guesses again");
            guesses = input.nextInt();
        }
        System.out.println();
        // consume the return character
        // otherwise System.out.println("enter start, settings, or stop"); will run twice
        input.nextLine();
    }
    
    public static void start()
    {
        while(guessesLeft > 0)
        {
            System.out.println("You have " + guessesLeft + " guesses left.");
            System.out.println("Enter your guess");
            guess = input.nextLine().toLowerCase();
            guessValidity();
            guessCheck();
            guessesLeft--;
            if(guessesLeft == 0)
            {
                System.out.println("You lose");
                System.out.println("The word is " + answer);
                games++;
            }
            System.out.println();
        }
    }
    
    public static void guessValidity()
    {
        while(true)
        {
            if(guess.length() != length)
            {
                System.out.println("The word needs to be " + length + " letters long");
                guess = input.nextLine().toLowerCase();
            }
            else if(onlyLetters(guess) == false)
            {
                System.out.println("The word can only contain English letters");
                guess = input.nextLine().toLowerCase();
            }
            else
            {
                break;
            }
        }
    }
    
    public static boolean onlyLetters(String word)
    {
        for(int i = 0; i < word.length(); i++)
        {
            if(!Character.isLetter(word.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
        
    public static void customList()
    {
        System.out.println("Only 5 letter words are allowed");
        System.out.println("How many words?");
        int size = input.nextInt();
        input.nextLine();
        for(int i = 0; i < size; i++)
        {
            System.out.println("enter a word");
            String wordTemp = input.nextLine();
            while(wordTemp.length() != 5 || !onlyLetters(wordTemp))
            {
                System.out.println("The word needs to be 5 letters long and contains only english letters. enter another word");
                wordTemp = input.nextLine();
            }
            wordsCustom.add(wordTemp);
        }
        answerCustom = wordsCustom.get((int)(Math.random() * wordsCustom.size()));
        clearConsole();
        System.out.println("Console cleared to hide word list");
    }
    
    public static void customPlay()
    {
        answer = answerCustom;
        while(guessesLeft > 0)
        {
            System.out.println("You have " + guessesLeft + " guesses left.");
            System.out.println("Enter your guess");
            guess = input.nextLine().toLowerCase();
            guessValidity();
            guessCheck();
            guessesLeft--;
            if(guessesLeft == 0)
            {
                System.out.println("You lose");
                System.out.println("The word is " + answer);
            }
            System.out.println();
        }
    }
    
    public static void customClear()
    {
        wordsCustom.clear();
        System.out.println("Successfully reset the custom word list");
    }
    
    public static void custom()
    {
        System.out.println("word length has been set to 5");
        length = 5;
        String choice;
        while(true)
        {
            System.out.println("The custom word list saves");
            System.out.println("enter add to add more words to the word list");
            System.out.println("enter reset to clear the word list");
            System.out.println("enter start to play");
            System.out.println("enter view to see custom list");
            System.out.println("enter leave to exit");
            choice = input.nextLine();
            if(choice .equalsIgnoreCase ("add"))
            {
                customList();
            }
            else if(choice .equalsIgnoreCase ("reset"))
            {
                customClear();
            }
            else if(choice .equalsIgnoreCase ("start"))
            {
                if(!wordsCustom.isEmpty())
                {
                    customPlay();
                    resetCustom();
                }
                else
                {
                    System.out.println("The word list is empty");
                }
            }
            else if(choice .equalsIgnoreCase ("view"))
            {
                customView();
            }
            else if(choice .equalsIgnoreCase ("leave"))
            {
                isCustom = false;
                break;
            }
        }
    }
    
    public static void guessCheck()
    {
        for(int i = 0; i < answer.length(); i++)
        {
            if(guess.charAt(i) == answer.charAt(i))
            {
                System.out.print(WHITE_TEXT + GREEN_BG + guess.charAt(i) + RESET);
            }
            else if(answer.indexOf(guess.charAt(i)) != -1)
            {
                System.out.print(WHITE_TEXT + YELLOW_BG + guess.charAt(i) + RESET);
            }
            else
            {
                System.out.print(WHITE_TEXT + BLACK_BG + guess.charAt(i) + RESET);
            }
        }
        System.out.println();
        if(guess .equalsIgnoreCase (answer))
        {
            System.out.println("Correct");
            if(!isCustom)
            {
                wins++;
                games++;
            }
            guessesLeft = 0;
        }
    }
    
    public static void reset()
    {
        guessesLeft = guesses;
        newAnswer();
    }
    
    public static void newAnswer()
    {
        if(length == 5)
        {
            answer = words[(int)(Math.random() * words.length)];
        }
        else if(length == 4)
        {
            answer = words4[(int)(Math.random() * words4.length)];
        }
        else if(length == 6)
        {
            answer = words6[(int)(Math.random() * words6.length)];
        }
    }
    
    public static void resetCustom()
    {
        guessesLeft = guesses;
        answerCustom = wordsCustom.get((int)(Math.random() * wordsCustom.size()));
    }
    
    public static void stats()
    {
        if(games == 0)
        {
            System.out.println("There are no stats for standard wordle.");
        }
        else
        {
            winRate = (double) wins / games * 100;
            System.out.println("Statistics for standard wordle");
            System.out.println("Games played: " + games);
            System.out.println("Wins: " + wins);
            System.out.println("Win rate: " + winRate + "%");
        }
    }
    
    private static void customView()
    {
        System.out.println(wordsCustom);
        String done = "";
        while(!done .equalsIgnoreCase ("done"))
        {
            System.out.println("enter done to clear console");
            done = input.nextLine();
        }
        clearConsole();
    }
    
    private static void clearConsole()
    {
        // moves cursor to top left then clears console
        System.out.println("\033[H\033[2J");
        // puts the cursor back at top left
        System.out.flush();
    }
}