import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;



public class Main {
    // print without new line so I dont have to type System.out.println everytime
    static void println(String input){
        System.out.println(input);
    }

    // print without new line so I dont have to type System.out.print everytime
    static void print(String input){
        System.out.print(input);
    }
    static PlayerCharacter createCharacter(Scanner scanner) {
        PlayerCharacter character;
        System.out.print("Enter character name: ");
        String charName = scanner.nextLine();
        System.out.print("Enter character's class: ");
        String charClass = scanner.nextLine();
        return character = new PlayerCharacter(charName, charClass);
    }
        public static void main(String[] args) {
            List<PlayerCharacter> activeCharacters = new ArrayList<>();
            Scanner scanner = new Scanner(System.in);
            String userInput;
            do
            {
                print("> ");
                userInput = scanner.nextLine();
                if(userInput.equalsIgnoreCase("create character")){
                    PlayerCharacter character = createCharacter(scanner);
                    character.displayCharacterInfo();
                }

            } while(!userInput.equals("quit"));



    }
}