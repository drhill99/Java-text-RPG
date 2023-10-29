import java.util.HashMap;
import java.io.*;
import java.util.Map;
import java.util.Random;

public class PlayerCharacter implements Serializable{
    private String charName;
    private String charClass;
    private int charLevel;
    private int experience;
    private int nextLevelUp;
    private int maxHealth;
    private int curHealth;
    private int armor;
    private int gold;

        // character behavior modifiers
    private final HashMap<String, Integer> classStartingHP = new HashMap<>(); // HashMap holds character starting HP by class
    private final HashMap<String, Integer> classBonusHealthPerLevel = new HashMap<>(); // HashMap holds character bonus health per level by class
    private HashMap<String, int[]> classMeleeAttackBonuses = new HashMap<>(); // HashMap holds character melee attack bonuses, presently not implemented
    private final HashMap<String, int[]> classRangedAttackBonuses = new HashMap<>(); // HashMap holds character ranged attack bonuses, presently not implemented


        // character inventory maps
    private final HashMap<String, String> equippedGear = new HashMap<>(); // HashMap holds character equipped items by slot
    private final HashMap<String, Integer> consumables = new HashMap<>(); // HashMap holds characters consumables by type and count

        // equipment information maps
    private final HashMap<String, Integer> armorValues = new HashMap<>(); // HashMap holds armor values by type and value
    private final HashMap<String, int[]> weaponDamage = new HashMap<>(); // HashMap holds weapon damage range by name and min/max int

        // default constructor
    public PlayerCharacter(){
    }
        // parametrized constructor
    public PlayerCharacter(String name, String charClass){
        this.charName = name;
        this.charClass = charClass;
        armor = 8; // base armor
        charLevel = 1; // starting level
        experience = 0; // starting experience
        gold = 175; // starting gold
        nextLevelUp = experience + 1000; // starting experience to gain for new level
        initCharacterBehaviorMaps(); // initialize HashMaps
        initCharacterInfo(); // initialize character staring Hp

    }
        // setters
    public void initCharacterBehaviorMaps(){
            // starting health map
        classStartingHP.put("warrior", 20);
        classStartingHP.put("rogue", 16);
        classStartingHP.put("mage", 12);
            // bonus health map
        classBonusHealthPerLevel.put("warrior", 5);
        classBonusHealthPerLevel.put("rogue", 4);
        classBonusHealthPerLevel.put("mage", 3);
            // class melee attack bonuses, array[0] = to hit bonus, array[1] = dmg bonus
        classMeleeAttackBonuses.put("warrior", new int[]{0,1});
        classMeleeAttackBonuses.put("rogue", new int[]{1,0});
        classMeleeAttackBonuses.put("mage", new int[]{0,0});
            // class ranged attack bonuses, array[0] = to hit bonus, array[1] = dmg bonus
        classRangedAttackBonuses.put("warrior", new int[]{0,0});
        classRangedAttackBonuses.put("rogue", new int[]{2,2});
        classRangedAttackBonuses.put("mage", new int[]{3,1});
            // starting equipment map
        equippedGear.put("melee", "empty");
        equippedGear.put("ranged", "empty");
        equippedGear.put("armor", "empty");
        equippedGear.put("offhand", "empty");
            // consumables
        consumables.put("arrow", 0);
        consumables.put("health potion", 0);
            // equipment information
        armorValues.put("chain mail", 8);
        armorValues.put("leather armor", 5);
        armorValues.put("mage robe", 2);
        armorValues.put("shield", 1);
            // weapon information
        weaponDamage.put("long sword", new int[]{1, 8});
        weaponDamage.put("two handed axe", new int[]{2, 10});
        weaponDamage.put("bow", new int[]{1,6});
        weaponDamage.put("mage staff", new int[]{3,8});
            // heatlh potion values
//        healthPotionValues.put("health potion", 5);
//        healthPotionValues.put("medium health potion", 10);
//        healthPotionValues.put("greater health potion", 15);


    }
    public void initCharacterInfo(){
        maxHealth = classStartingHP.get(charClass); // retrieve value from HashMap
        curHealth = maxHealth; // set current health to max health
    }
    public void setHealth(boolean gainLose, int amount){
        if(gainLose){
            curHealth += amount;
        } else {
            curHealth -= amount;
        }
    }

        // add experience to character
    public void gainExperience(int exp){
        experience += exp; // add experience
        if(experience > nextLevelUp){ // if new experience value is enough to gain a level
            charLevel += 1; // increment level
            maxHealth += classBonusHealthPerLevel.get(charClass); // adjust maxHealth based on HashMap value
            curHealth = maxHealth; // set current health to max health
            nextLevelUp += 1000 + (250 * charLevel); // adjust amount to gain a level
        }
    }



        // display character attributes
    public void displayCharacterInfo(){
        System.out.println();
        println("\nParty leader:");
        println("   Name: " + charName + " - Class: " + charClass + " - Level: " + charLevel + " - Exp: " + experience + "/" + nextLevelUp);
        println("   HP: " + curHealth + "/" + maxHealth + " - Gold: " + gold);
        println("   Armor: " + armor);
    }

        // attribute getters, simply return class attributes
    public String getCharClass(){
        return charClass;
    }
    public int getCurHealth(){
        return curHealth;
    }
    public int getMaxHealth(){
        return maxHealth;
    }
    public String getName(){
        return charName;
    }




    // inventory getters
    public void displayEquippedGear(){
        for(Map.Entry<String, String> equipSlot : equippedGear.entrySet()){
            println(equipSlot.getKey() + ": " + equipSlot.getValue());
        }
        for(Map.Entry<String, Integer> consumable : consumables.entrySet()){
            println(consumable.getKey() + ": " + consumable.getValue());
        }
    }
    public int getGold(){
        return gold;
    }





    // inventory setters
    public boolean purchaseItem(String itemType, String equipSlot, String itemName, int goldCost, int count){
        if(gold >= goldCost){ // check that character object has enough gold to complete transaction
            setGold(false, goldCost); // remove specified amount of gold
                // transaction based on consumable versus equip item type
            if(itemType.equalsIgnoreCase("consumable")){
                addUseConsumable(true, itemName, count); // adjust consumable counts
                return true; // return true that the transaction was successful
            } else if(itemType.equalsIgnoreCase("equip")){
                equipItem(itemName, equipSlot); // equip the new item
                return true; // return true that the transaction was successful
            }
        } return false; // return false that the transaction failed due to lack of gold
    }
        // adjust gold attribute, true for add, false for remove
    public void setGold(boolean addGain, int amount){
        if(addGain){
            gold += amount;
        } else {
            gold -= amount;
        }
    }
        // adjust consumable count, true for add, false for use
    public void addUseConsumable(boolean addUse, String itemName, int count){
        if(addUse){
            consumables.put(itemName, consumables.get(itemName) + count); // increase the count of the consumable
//             else {
//                println("You are out of " + itemName); // informs user that they are out of the specified consumable
//            }
        } else if(consumables.get(itemName) > 0){ // check that the consumable count is greater than 0 before using.
            consumables.put(itemName, consumables.get(itemName) - 1);
        }
        if(consumables.get(itemName) == 0){
            println("You have run out of " + itemName + "s!");
        }
    }
    public void equipItem(String itemName, String itemType){
        equippedGear.put(itemType, itemName); // add new value to the equipped gear map for the itemType key
        if("armor".equalsIgnoreCase(itemType)){ // if the itemType is armor, adjust armor attribute
            armor = armor + armorValues.get(itemName);
        }
    }




    // character actions
        // attack with specified weapon type
    public double attack(String atkType){
        Random random = new Random(); // create random object
        String weaponSlot; // declare weaponSlot string
        if("m".equalsIgnoreCase(atkType)){ // if itemType parameter is 'm'
            weaponSlot = "melee";
            if(equippedGear.get(weaponSlot).equals("empty")){ // check that a weapon is equipped for the atkType
                println("You do not have a " + weaponSlot + " weapon equipped.");
            } else {
                int minDmg = weaponDamage.get(equippedGear.get(weaponSlot))[0]; // grab damage range minimum
                int maxDmg = weaponDamage.get(equippedGear.get(weaponSlot))[1]; // grab damage range maximum
                return random.nextInt(maxDmg) + minDmg; // return damage value double
            }

        } else if("r".equalsIgnoreCase(atkType)){ // execute same as above but for ranged slot
            weaponSlot = "ranged";
            if(consumables.get("arrow") > 0){ // check that sufficient number of arrows exist
                if(equippedGear.get(weaponSlot).equals("empty")){
                    println("You do not have a " + weaponSlot + " weapon equipped.");
                } else {
                    int minDmg = weaponDamage.get(equippedGear.get(weaponSlot))[0];
                    int maxDmg = weaponDamage.get(equippedGear.get(weaponSlot))[1];
                    addUseConsumable(false, "arrow", 1); // remove 1 arrow
                    return random.nextInt(maxDmg) + minDmg;
                }
            } else {
                println("You are out of arrows!");
            }
        }

        return 0;
    }
    public void useHealthPotion(){
        if(consumables.get("health potion") > 0){ // check that at least 1 health potion is held
            setHealth(true, 5); // increase current health by 5.
        } else {
            println("You dont have any health potions!");
        }
            // set health if greater after using potion
        if(curHealth > maxHealth){
            curHealth = maxHealth;
        }
            // adjust health potion count
        addUseConsumable(false, "health potion", 1);
    }
    // utility functions
        // custom println function cuz System.out.println everytime..... limited to taking string input
    static void println(String input){
        System.out.println(input);
    }

}