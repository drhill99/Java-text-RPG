//import java.io.ByteArrayInputStream;
//import java.io.ObjectOutputStream;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
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
                activeCharacters.add(character);
            } else if(userInput.equalsIgnoreCase("display character info")){
                print("> ");
                userInput = scanner.nextLine(); // character Name
                printCharacterInfo(activeCharacters, userInput);
            }

        } while(!userInput.equals("quit"));
        serializeCharacterObject(scanner, activeCharacters);
    }

    // FUNCTION DEFINITIONS *************************************************************

    // print without new line so I dont have to type System.out.println everytime
    static void printCharacterInfo(List<PlayerCharacter> characters, String characterName){
        for(PlayerCharacter character : characters){
            if(character.getName().equalsIgnoreCase(characterName)){
                character.displayCharacterInfo();
            }
        }
    }

    static PlayerCharacter findCharacterObject(Scanner scanner, List<PlayerCharacter> characterObjects){
        print("enter the character to find: ");
        String charToFind = scanner.nextLine();
        for(PlayerCharacter character : characterObjects){
            if(character.getName().equalsIgnoreCase(charToFind)){
                return character;
            }
        }
        return null;
    }
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
    static void serializeCharacterObject(Scanner scanner, List<PlayerCharacter> characterObjects){
        PlayerCharacter characterObject = findCharacterObject(scanner, characterObjects);
        byte[] serializedCharacterObject = new byte[0];
       try(ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
           ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
           objectOutput.writeObject(characterObject);
           serializedCharacterObject = byteOutput.toByteArray();
       } catch (IOException e){
           e.printStackTrace();
       }
       try(FileOutputStream fileOutput = new FileOutputStream(characterObject.getName())){
           fileOutput.write(serializedCharacterObject);
       } catch (IOException e){
           e.printStackTrace();
       }
    }
}