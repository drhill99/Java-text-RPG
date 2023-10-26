//import java.io.ByteArrayInputStream;
//import java.io.ObjectOutputStream;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            // check for character file directory, create if does not exist
        createCharacterFileDirectory();
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
            } else if(userInput.equalsIgnoreCase("display")){
                print("character to display > ");
                userInput = scanner.nextLine(); // character Name
                printCharacterInfo(activeCharacters, userInput);
            } else if (userInput.equalsIgnoreCase("load")){

                PlayerCharacter character = loadCharacterObjectFromFile(activeCharacters, scanner);

            } else if (userInput.equalsIgnoreCase("save")){
                serializeCharacterObject(scanner, activeCharacters);
            } else if (userInput.equalsIgnoreCase("exp")){
                findCharacterObject(scanner, activeCharacters).gainExperience(500);
            }
        } while(!userInput.equals("quit"));
    }

    // FUNCTION DEFINITIONS *************************************************************
    static void shop(PlayerCharacter character){
        println(character.getName() + " you have " + character.getGold() + " . . . spend it wisely.");
        println("   Weapons:");
        println("       75g: Two handed axe: 2-10 dmg. Melee, Two handed. 16 str req.");
        println("       50g: Long Sword: 1-8 dmg. Melee, One handed. 12 str req.");
        println("       50g: Bow: 1-6. Ranged, Two handed");
        println("       50g: Mage staff: Ranged. Two handed");
        println("   Armor:");
        println("       75g: Chain mail: 8 armor. Heavy: requires 16 str. -2 agility penalty. Cannot cast spells.");
        println("       50g: Leather Armor: 5 armor. Light: No str req. Cannot cast spells.");
        println("       75g: Mage robe: 2 armor. Light: No str req.");
        println("       Shield: 1 armor. No str req.");
        println("   Consumables: ");
        println("       Arrows x 20: 20g");
        println("       Health Potion x 1: 10g. Restores 5 health.");
    }
    // print without new line so I dont have to type System.out.println everytime
    static void printCharacterInfo(List<PlayerCharacter> characters, String characterName){
        for(PlayerCharacter character : characters){
            if(character.getName().equalsIgnoreCase(characterName)){
                character.displayCharacterInfo();
            }
        }
    }

    static PlayerCharacter findCharacterObject(Scanner scanner, List<PlayerCharacter> characterObjects){
        print("enter character name: ");
        String charToFind = scanner.nextLine();
        for(PlayerCharacter character : characterObjects){
            if(character.getName().equalsIgnoreCase(charToFind)){
                return character;
            }
        }
        return null;
    }
    static PlayerCharacter createCharacter(Scanner scanner) {
        PlayerCharacter character;
        System.out.print("Enter character name: ");
        String charName = scanner.nextLine();
        System.out.print("Enter character's class: ");
        String charClass = scanner.nextLine();
        return character = new PlayerCharacter(charName, charClass);
    }
    static void println(String input){
        System.out.println(input);
    }
    // print without new line so I dont have to type System.out.print everytime
    static void print(String input){
        System.out.print(input);
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
       try(FileOutputStream fileOutput = new FileOutputStream("character_files\\" + characterObject.getName())){
           fileOutput.write(serializedCharacterObject);
       } catch (IOException e){
           e.printStackTrace();
       }
    }

    static PlayerCharacter loadCharacterObjectFromFile(List<PlayerCharacter> activeCharacters, Scanner scanner){
        print("character to load > ");
        String userInput = scanner.nextLine();
        try(ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream("character_files\\" + userInput))){
            PlayerCharacter character =  (PlayerCharacter) objectInput.readObject();
            println(character.getName() + " has been loaded.");
            character.displayCharacterInfo();
            activeCharacters.add(character);
            return character;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    static void createCharacterFileDirectory(){
        File charFileDir = new File("character_files");

        // check to see if the directory already exists, if not create it
        if(!charFileDir.exists()){
            if(charFileDir.mkdirs()){
                println("Created character file directory");
            } else {
                println("Failed to create character file directory");
            }
        } else {
            println("Character file directory already exists");
        }
    }
}