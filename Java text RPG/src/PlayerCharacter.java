import java.util.HashMap;
import java.io.*;
import java.util.Map;

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
    private final HashMap<String, Integer> classStartingHP = new HashMap<>();
    private final HashMap<String, Integer> classBonusHealthPerLevel = new HashMap<>();
    private HashMap<String, int[]> classMeleeAttackBonuses = new HashMap<>();
    private HashMap<String, int[]> classRangedAttackBonuses = new HashMap<>();
    // character inventory maps
    private HashMap<String, String> equippedGear = new HashMap<>();
    private HashMap<String, Integer> consumables = new HashMap<>();

    // equipment information maps
    private HashMap<String, Integer> armorValues = new HashMap<>();
    private HashMap<String, int[]> weaponDamage = new HashMap<>();

    // default constructor
    public PlayerCharacter(){
    }
    // parametrized constructor
    public PlayerCharacter(String name, String charClass){
        this.charName = name;
        this.charClass = charClass;
        armor = 8; // base armor
        charLevel = 1;
        experience = 0;
        gold = 175;
        nextLevelUp = experience + 1000;
        initCharacterBehaviorMaps();
        initCharacterInfo();

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


    }
    public void initCharacterInfo(){
        maxHealth = classStartingHP.get(charClass);
        curHealth = maxHealth;
    }
    public void gainExperience(int exp){
        experience += exp;
        int expOvershoot = experience - nextLevelUp;
        if(experience > nextLevelUp){
            charLevel += 1;
            maxHealth += classBonusHealthPerLevel.get(charClass);
            nextLevelUp += 1000;
        }
    }
    // attribute getters
    public void displayCharacterInfo(){
        println("Name: " + charName + " - Class: " + charClass + " - Level: " + charLevel + " - Exp: " + experience + "/" + nextLevelUp);
        println("HP: " + curHealth + "/" + maxHealth);
        println("Armor: " + armor);
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
    // inventory setters
    public boolean purchaseItem(String itemType, String equipSlot, String itemName, int goldCost, int count){
        if(gold >= goldCost){
            gold -= goldCost;
            if(itemType.equalsIgnoreCase("consumable")){
                addUseConsumable(true, itemName, count);
                return true;
            } else if(itemType.equalsIgnoreCase("equip")){
                equipItem(itemName, equipSlot);
                return true;
            }
        } return false;
    }
    public void addUseConsumable(boolean addUse, String itemName, int count){
        if(addUse){
            consumables.put(itemName, consumables.get(itemName) + 1);
        } else {
            if(consumables.get(itemName) > 0){
                consumables.put(itemName, consumables.get(itemName) - 1);
            } else {
                println("You are out of " + itemName);
            }
        }
    }
    public void equipItem(String itemName, String itemType){
        equippedGear.put(itemType, itemName);
    }
    public int getGold(){
        return gold;
    }
    public String getCharClass(){
        return charClass;
    }

    public int getCurHealth(){
        return curHealth;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    // utility functions
    static void println(String input){
        System.out.println(input);
    }
    // print without new line so I dont have to type System.out.print everytime
    static void print(String input){
        System.out.print(input);
    }

}