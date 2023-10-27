//import java.io.ByteArrayInputStream;
//import java.io.ObjectOutputStream;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
            // check for character file directory, create if does not exist
        createCharacterFileDirectory();
        List<PlayerCharacter> activeCharacters = new ArrayList<>();
        PlayerCharacter activeCharacter = null;
        Scanner scanner = new Scanner(System.in);
        String userInput;
        do
        {
            if(activeCharacters.isEmpty()){
                print("No active characters, create new character or load saved character? ");
                String createOrLoad = scanner.nextLine();
                if(createOrLoad.equalsIgnoreCase("create")){
                    println("Creating new character:");
                    PlayerCharacter character = createCharacter(scanner);
                    character.displayCharacterInfo();
                    activeCharacters.add(character);
                    print("Set new character as active character?");
                    String setAsActive = scanner.next();
                    if(setAsActive.equalsIgnoreCase("y")){
                        activeCharacter = character;
                    }
                } else if(createOrLoad.equalsIgnoreCase("load")){
                    PlayerCharacter character = loadCharacterObjectFromFile(activeCharacters, scanner);
                    activeCharacters.add(character);
                }
            }
           // activeCharacter = findCharacterObject(scanner, activeCharacters);
            println("word inside ( ) indicates command");
            println("   (create) new character");
            println("   (load) character file");
            println("   (save) character to file");
            println("   (change) active character.");
            println("   (display) character info");
            println("   (shop) with active character");
            println("   add (exp) to active character");
            println("   show (inv)entory");
            print("     > ");
            scanner.nextLine();
            userInput = scanner.nextLine();

            if(userInput.equalsIgnoreCase("create")){
                println("Creating new character:");
                PlayerCharacter character = createCharacter(scanner);
                character.displayCharacterInfo();
                activeCharacters.add(character);
                print("Set new character as active character?");
                String setAsActive = scanner.next();
                if(setAsActive.equalsIgnoreCase("y")){
                    activeCharacter = character;
                }
            } else if(userInput.equalsIgnoreCase("display") && activeCharacter != null){
                print("character to display > ");
                //userInput = scanner.nextLine(); // character Name
                //printCharacterInfo(activeCharacters, userInput);
                if(activeCharacter != null){
                    activeCharacter.displayCharacterInfo();
                }
            } else if (userInput.equalsIgnoreCase("load")){
                PlayerCharacter character = loadCharacterObjectFromFile(activeCharacters, scanner);
                if(character != null){
                    activeCharacters.add(character);
                    println(character.getName() + " loaded to party.");
                } else {
                    println("failed to load character.");
                }
            } else if (userInput.equalsIgnoreCase("save")){
                serializeCharacterObject(scanner, activeCharacter);

            } else if (userInput.equalsIgnoreCase("exp")){
                //findCharacterObject(scanner, activeCharacters).gainExperience(500);
                if(activeCharacter != null){
                    activeCharacter.gainExperience(500);
                } else {
                    println("Please create or choose an active character.");
                }
            } else if (userInput.equalsIgnoreCase("shop")){
//                shop(scanner, Objects.requireNonNull(findCharacterObject(scanner, activeCharacters)));
                if(activeCharacter != null){
                    shop(scanner, activeCharacter);
                } else {
                    println("Please create or choose an active character.");
                }
            } else if (userInput.equalsIgnoreCase("change")){
                activeCharacter = findCharacterObject(scanner, activeCharacters);
            } else if (userInput.equalsIgnoreCase("inv")){
                if(activeCharacter != null){
                    activeCharacter.displayEquippedGear();
                } else {
                    println("Please create or choose an active character.");
                }
            }
        } while(!userInput.equalsIgnoreCase("quit"));
    }

    // FUNCTION DEFINITIONS *************************************************************
    static void shop(Scanner scanner, PlayerCharacter character){
        class MixedContainer {
            public final String itemType;
            public final String equipSlot;
            public final String itemName;
            public final int goldCost;
            public final int count;

            public MixedContainer(String itemType, String equipSlot, String itemName, int goldCost, int count){
                this.itemType = itemType;
                this.equipSlot = equipSlot;
                this.itemName = itemName;
                this.goldCost = goldCost;
                this.count = count;
            }
        }

        HashMap<String, MixedContainer> shopInventory = new HashMap<>();
        shopInventory.put("two handed axe", new MixedContainer("equip",  "melee" ,"two handed axe", 75, 1));
        shopInventory.put("long sword", new MixedContainer("equip",   "melee", "long sword", 50, 1));
        shopInventory.put("bow", new MixedContainer("equip", "ranged","bow", 50, 1));
        shopInventory.put("mage staff", new MixedContainer("equip", "ranged","mage staff", 50, 1));
        shopInventory.put("chain mail", new MixedContainer("equip", "armor","chain mail", 75, 1));
        shopInventory.put("leather armor", new MixedContainer("equip", "armor","leather armor", 50, 1));
        shopInventory.put("mage robe", new MixedContainer("equip", "armor","mage robe",75, 1));
        shopInventory.put("arrow", new MixedContainer("consumable", null, "arrow", 20, 20));
        shopInventory.put("health potion", new MixedContainer("consumable", null, "health potion", 10, 1));

        do {
            for(Map.Entry<String, MixedContainer> shopItem : shopInventory.entrySet()){
                println(shopItem.getKey() +
                        ": ItemType: " + shopItem.getValue().itemType +
                        ": equipSlot: " + shopItem.getValue().equipSlot +
                        ": Itemname: " + shopItem.getValue().itemName +
                        ": GoldCost: " + shopItem.getValue().goldCost +
                        ": count: " + shopItem.getValue().count);
            }
            String userInput = "";
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
            println("       Arrow x 20: 20g");
            println("       Health Potion x 1: 10g. Restores 5 health.");
            int numConPurchased = 0;

            println(" > ");

            userInput = scanner.nextLine();

            if (shopInventory.get(userInput).itemType.equalsIgnoreCase("consumable") && shopInventory.get(userInput).itemType != null ) {
                println("A");
                print("how many " + userInput + "s would you like to purchase?");
                int purchaseCount = scanner.nextInt();
                scanner.nextLine();
                for (int i = 0; i < purchaseCount; i++) {
                    println("B");
                    if(character.purchaseItem(
                            shopInventory.get(userInput).itemType,
                            shopInventory.get(userInput).equipSlot,
                            shopInventory.get(userInput).itemName,
                            shopInventory.get(userInput).goldCost,
                            shopInventory.get(userInput).count))
                    {
                        numConPurchased += 1;
                    }

                }
                println("Purchased " + numConPurchased + " " + userInput + "s before you ran out of gold.");
            } else if(shopInventory.get(userInput).itemType.equalsIgnoreCase("equip") && shopInventory.get(userInput).itemType != null){
                character.purchaseItem(
                        shopInventory.get(userInput).itemType,
                        shopInventory.get(userInput).equipSlot,
                        shopInventory.get(userInput).itemName,
                        shopInventory.get(userInput).goldCost,
                        shopInventory.get(userInput).count
                );
            } else {
                println("You got a null goin on");
            }
        } while(character.getGold() >= 10);
        println("Thank you for shopping the adventure mart!");
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
        String charName = scanner.nextLine().toLowerCase();
        System.out.print("Enter character's class: ");
        String charClass = scanner.nextLine().toLowerCase();
        return character = new PlayerCharacter(charName, charClass);
    }
    static void println(String input){
        System.out.println(input);
    }
    // print without new line so I dont have to type System.out.print everytime
    static void print(String input){
        System.out.print(input);
    }
    static void serializeCharacterObject(Scanner scanner, PlayerCharacter characterObject){
        //PlayerCharacter characterObject = findCharacterObject(scanner, characterObjects);
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