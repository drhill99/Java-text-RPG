import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
            // check for character file directory, create if does not exist
        createCharacterFileDirectory();
            // empty list of PlayerCharacter objects
        List<PlayerCharacter> activeCharacters = new ArrayList<>();
            // iniot activeCharacter object as null
        PlayerCharacter activeCharacter = null;
            // create scanner object for user input
        Scanner scanner = new Scanner(System.in);
        String userInput; // declare userInput string.
        do
        {
            userInput = "";

            if(activeCharacters.isEmpty()){
                println("No active characters, (c)reate new character or (l)oad saved character?");
                String createOrLoad = scanner.nextLine().trim();
                System.out.println(createOrLoad + createOrLoad.length());
                if(createOrLoad.equalsIgnoreCase("c")){
                    PlayerCharacter character = createCharacter(scanner);
                    println("Creating new character:");

//                    println("Set new character as active character?");
//                    String setAsActive = scanner.nextLine().trim();
                    activeCharacters.add(character);
//                    if(setAsActive.equalsIgnoreCase("y")){
//                        activeCharacter = character;
//                    }
                } else if(createOrLoad.equalsIgnoreCase("l")){
                    boolean noSaveFiles = listOfSavedCharacers();
                    if(!noSaveFiles){
                        PlayerCharacter character = loadCharacterObjectFromFile(activeCharacters, scanner);
                        activeCharacter = character;
                    } else {
                        println("There are no save files. Creating new character....");
                        PlayerCharacter character = createCharacter(scanner);
                        activeCharacters.add(character);
                        activeCharacter = character;
                    }
                }
            }
           // label A
            if(activeCharacter != null){
                activeCharacter.displayCharacterInfo();
                activeCharacter.displayEquippedGear();
                System.out.println();
            }
            println("character(s) inside (  ) indicates command");
            println("   (c)reate new character");
            println("   (l)oad character file");
            println("   (s)ave character to file");
            println("   print list of character (f)iles");
            println("   change (a)ctive character.");
            println("   print characters in par(t)y");
            println("   (d)isplay character info");
            println("   sho(p) with active character");
            println("   add e(x)perience to active character");
            println("   add (g)old");
            println("   show (i)nventory");
            println("   attack(k)");
            println("   user health (pot)ion");
            println("   take (dam)age");

            userInput = scanner.nextLine();
            activeCharacter = executeUserInput(userInput, activeCharacter, activeCharacters, scanner);
//            println("End of menu loop, active character is: " + activeCharacter.getName());
        } while(!userInput.equalsIgnoreCase("quit"));
    }

    // FUNCTION DEFINITIONS *************************************************************
    static PlayerCharacter executeUserInput(String userInput, PlayerCharacter activeCharacter, List<PlayerCharacter> activeCharacters, Scanner scanner){
            // create new character
        if("c".equalsIgnoreCase(userInput)){
            println("Creating new character:");
            PlayerCharacter character = createCharacter(scanner); // create new character object
            activeCharacters.add(character); // add new character object to list of active character objects
            print("Set new character as active character?");
            String setAsActive = scanner.nextLine();
            if(setAsActive.equalsIgnoreCase("y")){
                activeCharacter = character; // set new character object to be the active character object
                println(activeCharacter.getName() + " is now the party leader.");
            }
        }

        // load character from save files
        else if (userInput.equalsIgnoreCase("l")){
                // print list of saved character object files, returns true if no files, false if there are files
            boolean noSaveFiles = listOfSavedCharacers();
            if(!noSaveFiles){  // if the save file directory is not empty
                    // load character from object save file
                PlayerCharacter character = loadCharacterObjectFromFile(activeCharacters, scanner);
                if(character != null){ // check that character object is not null

                    println(character.getName() + " loaded to party.");
                } else {
                    println("failed to load character.");
                }
                println("Set new character as active character?");
                String setAsActive = scanner.nextLine().trim(); // get user input

                if(setAsActive.equalsIgnoreCase("y")){
                    activeCharacter = character; // set hew character object as active
                }
            } else {
                println("There are no save files. Do you want to create a new character now? y/n");
                String createNewChar = scanner.nextLine();
                if (createNewChar.equalsIgnoreCase("y")) {
                    PlayerCharacter character = createCharacter(scanner); // create new character object
                    println("Set new character as active character?");
                    String setAsActive = scanner.nextLine().trim();

                    if(setAsActive.equalsIgnoreCase("y")){
                        activeCharacter = character;
                    }
                } else {
                    System.out.println();
                }
            }
        }

            // save character object to file
        else if (userInput.equalsIgnoreCase("s")){
            serializeCharacterObject(scanner, activeCharacter); // save character object to file
        }
            // print list of character object save files
        else if (userInput.equalsIgnoreCase("f")) {
            println("Saved character files:");
            listOfSavedCharacers(); // print list of saved character object files
        }

        // change the active character
        else if (userInput.equalsIgnoreCase("a")){
            printCharactersInParty(activeCharacters); // print list of active character objects
                // retrieve specific character object from list of active objects and set as the active object
            activeCharacter = findCharacterObject(scanner, activeCharacters); ;
        }

        // print list of active characters
        else if(userInput.equalsIgnoreCase("t")){
            println("Characters in the party: ");
            printCharactersInParty(activeCharacters); // print list of active character objects
        }

        // display active character information
        else if(userInput.equalsIgnoreCase("d")){
            PlayerCharacter character = findCharacterObject(scanner, activeCharacters);
            if(character != null){ // check that character object is not null
                character.displayCharacterInfo(); // display character info
            } else {
                println("Please create or choose an active character.");
            }
        }

            // shop with the active character
        else if (userInput.equalsIgnoreCase("p")){
            if(activeCharacter != null){ // check that character object is not null
                shop(scanner, activeCharacter); // call shop function with active character
            } else {
                println("Please create or choose an active character.");
            }
        }

            // add experience to active character
        else if (userInput.equalsIgnoreCase("x")){
            //findCharacterObject(scanner, activeCharacters).gainExperience(500);
            if(activeCharacter != null){ // check that character object is not null
                activeCharacter.gainExperience(500); // add experience to character object
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }

            // add gold to active character
        else if(userInput.equalsIgnoreCase("g")){
            if(activeCharacter != null){ // check that character object is not null
                activeCharacter.setGold(true, 100); // update character gold
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }

            // print character inventory
        else if (userInput.equalsIgnoreCase("i")){

            if(activeCharacter != null){ // check that character object is not null
                println("Character inventory: ");
                activeCharacter.displayEquippedGear();
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }
            // user characters attack function
        else if (userInput.equalsIgnoreCase("k")){
            if(activeCharacter != null){ // check that character object is not null
                print("(M)elee or (R)anged?");
                String atkType = scanner.nextLine();
                println("You attack dealt " + activeCharacter.attack(atkType));
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }
            // use health potion
        else if(userInput.equalsIgnoreCase("pot")){
            if(activeCharacter != null){ // check that character object is not null
                activeCharacter.useHealthPotion();
                println("you gained 5 health");
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }
            // cause character to take damage
        else if(userInput.equalsIgnoreCase("dam")){
            if(activeCharacter != null){ // check that character object is not null
                activeCharacter.setHealth(false, 10); // reduce health of character object
                println("You took 10 dmg!");
            } else {
                println("Please create or choose an active character."); // prompt user to create new character or update active
            }
        }
            // the active character may be changed in the above if else if statements, so return active character back to menu loop
        return activeCharacter;
    }
        // print names of active character objects
    static void printCharactersInParty(List<PlayerCharacter> activeCharacters){
        if(activeCharacters.isEmpty()){ // check that the list of active character objects is not empty
            println("there are no characters in the party, create or load a character.");
        } else {
                // step through and print names of active character objects
            for(PlayerCharacter character : activeCharacters){
                println(character.getName() + " - " + character.getCharClass());
            }
        }
    }
        // print names of character object save files and return true or false for empty directory
    static boolean listOfSavedCharacers(){
        boolean empty = false; // set empty base case to false
        File directory = new File("character_files"); // create a file of the save directory
        File[] files = directory.listFiles(); // create an array of the files
            // step through and print the name of each file in the files array
        if(files != null){ // check that the files array is not null
            for(File file : files){
                println(file.getName());
            }
            if(files.length == 0){
                empty = true;
            }
        } else {
            println("Error retrieving character save files, or the directory is empty");
        }
        return empty;
    }
        // shop function, allows user to buy equipment and consumables
    static void shop(Scanner scanner, PlayerCharacter character){
            // class creates an object that simply holds attributes
            // class to be used in a HashMap
        class MixedContainer {
            public final String itemType; // holds item type, equip, consumable
            public final String equipSlot; // item slot, armor, melee, ranged, null for consumables
            public final String itemName; // item name, long sword, bow etc
            public final int goldCost; // cost of the item
            public final int count; // the number of items purchased in a single transaction

                // parametrized constructor
            public MixedContainer(String itemType, String equipSlot, String itemName, int goldCost, int count){
                this.itemType = itemType;
                this.equipSlot = equipSlot;
                this.itemName = itemName;
                this.goldCost = goldCost;
                this.count = count;
            }
        }
            // declare then initialize HashMap containing the available shop items with there type, slot, name, cost and count.
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

            // do while loop continues until user leaves or runs out of gold.
        do {
                // displays item names, descriptions and cost.
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
            println("   (leave) shop");
            int numConPurchased = 0; // init number of purchased consumables at 0

            userInput = scanner.nextLine().trim(); // take user input
            if (userInput.equalsIgnoreCase("leave")){  // break if user types "leave"
                break;
            }
                // if userInput value is "consumable" checking that the itemType isnt null.
            if (shopInventory.get(userInput).itemType.equalsIgnoreCase("consumable") && shopInventory.get(userInput).itemType != null ) {

                print("how many " + userInput + "s would you like to purchase?"); // prompts user to enter a count of consumables to purchase
                int purchaseCount = scanner.nextInt();
                scanner.nextLine(); // gobble up any errant new line characters
                    // call character purchase item method based on user input count
                for (int i = 0; i < purchaseCount; i++) {
                    if(character.purchaseItem(
                            shopInventory.get(userInput).itemType,
                            shopInventory.get(userInput).equipSlot,
                            shopInventory.get(userInput).itemName,
                            shopInventory.get(userInput).goldCost,
                            shopInventory.get(userInput).count))
                    {
                        numConPurchased += 1; // increment purchased consumable count by 1
                    }
                }
                    // print to user how many potions they purchased
                if(userInput.equalsIgnoreCase("health potion")){
                    println("Purchased " + numConPurchased + " " + userInput +"s");
                    // print to user how many stacks of arrows they purchased
                } else if(userInput.equalsIgnoreCase("arrow")){
                    println("Purchased " + numConPurchased + " stacks of " + userInput + "s");
                }
                // call characger purchase item method if the itemType is equip
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
        } while(character.getGold() >= 10); // ends the shop loop if the player doesnt have enough gold to buy 1 of the cheapest item.
        println("Thank you for shopping the adventure mart!");
    }


        // take user input and loop through the active character objects and print the info of the correct object
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
        // prompt user to name a character, its class, then return a new PlayerCharacter object
    static PlayerCharacter createCharacter(Scanner scanner) {

        System.out.print("Enter character name: ");
        String charName = scanner.nextLine().toLowerCase();
        System.out.print("Enter character's class: ");
        String charClass = scanner.nextLine().toLowerCase();
        return new PlayerCharacter(charName, charClass);
    }
        // custom println function to avoid typing out System.out..... every time. limited in that it must take a string
    static void println(String input){
        System.out.println(input);
    }
        // custom println function to avoid typing out System.out..... every time. limited in that it must take a string
    static void print(String input){
        System.out.print(input);
    }

        // serialize character object and save as file
    static void serializeCharacterObject(Scanner scanner, PlayerCharacter characterObject){
       byte[] serializedCharacterObject = new byte[0]; // init empty byte array
            // try block attempts to create a new ByteArrayOutputstream object,
            // then a new ObjectOutputStream object from the ByteArrayOutputStream object
            // then writes the character object to the ObjectOutputStream object.
       try(ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
           ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
           objectOutput.writeObject(characterObject);
           serializedCharacterObject = byteOutput.toByteArray();
       } catch (IOException e){
           e.printStackTrace();
       }
            // try block creates a new FileOutputStream object with a given directory and file name
       try(FileOutputStream fileOutput = new FileOutputStream("character_files\\" + characterObject.getName())){
                // write the previously serialized output object to the file output object.
           fileOutput.write(serializedCharacterObject);
       } catch (IOException e){
           e.printStackTrace();
       }
    }
        // load and deserialize saved character object back into a PlayerCharacter object
    static PlayerCharacter loadCharacterObjectFromFile(List<PlayerCharacter> activeCharacters, Scanner scanner){
        print("character to load > ");
        String userInput = scanner.nextLine();
            // try block attempts to create a new ObjectInput object from a new FileInputStream from the desired directory and saved object name
        try(ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream("character_files\\" + userInput))){
            PlayerCharacter character =  (PlayerCharacter) objectInput.readObject();
            println(character.getName() + " has been loaded.");
            activeCharacters.add(character);
            return character;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
        // return newly loaded character object
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