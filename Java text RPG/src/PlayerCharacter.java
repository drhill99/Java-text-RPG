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
    private HashMap<String, Integer> healthPotionValues = new HashMap<>();
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
            // heatlh potion values
        healthPotionValues.put("health potion", 5);
        healthPotionValues.put("medium health potion", 10);
        healthPotionValues.put("greater health potion", 15);


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
        System.out.println();
        println("Party leader:");
        println("   Name: " + charName + " - Class: " + charClass + " - Level: " + charLevel + " - Exp: " + experience + "/" + nextLevelUp);
        println("   HP: " + curHealth + "/" + maxHealth + " - Gold: " + gold);
        println("   Armor: " + armor);
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
    public String getName(){
        return charName;
    }
    public void setHealth(boolean gainLose, int amount){
        if(gainLose){
            curHealth += amount;
        } else {
            curHealth -= amount;
        }
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
        if(gold >= goldCost){

            setGold(false, goldCost);
            if(itemType.equalsIgnoreCase("consumable")){
                addUseConsumable(true, itemName, count);
                return true;
            } else if(itemType.equalsIgnoreCase("equip")){
                equipItem(itemName, equipSlot);
                return true;
            }
        } return false;
    }
    public void setGold(boolean addGain, int amount){
        if(addGain){
            gold += amount;
        } else {
            gold -= amount;
        }
    }
    public void addUseConsumable(boolean addUse, String itemName, int count){
        if(addUse){
            consumables.put(itemName, consumables.get(itemName) + count);
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
        if("armor".equalsIgnoreCase(itemType)){
            armor = armor + armorValues.get(itemName);
        }
    }

    // character actions
    public double attack(String atkType){

        Random random = new Random();
        String weaponSlot ="";
        if("m".equalsIgnoreCase(atkType)){
            weaponSlot = "melee";

            if(equippedGear.get(weaponSlot).equals("empty")){
                println("You do not have a " + weaponSlot + " weapon equipped.");
            } else {
                int minDmg = weaponDamage.get(equippedGear.get(weaponSlot))[0];
                int maxDmg = weaponDamage.get(equippedGear.get(weaponSlot))[1];
                return random.nextInt(maxDmg) + minDmg;
            }

        } else if("r".equalsIgnoreCase(atkType)){
            weaponSlot = "ranged";
            if(consumables.get("arrow") > 0){
                if(equippedGear.get(weaponSlot).equals("empty")){
                    println("You do not have a " + weaponSlot + " weapon equipped.");
                } else {
                    int minDmg = weaponDamage.get(equippedGear.get(weaponSlot))[0];
                    int maxDmg = weaponDamage.get(equippedGear.get(weaponSlot))[1];
                    addUseConsumable(false, "arrow", 1);
                    return random.nextInt(maxDmg) + minDmg;
                }
            } else {
                println("You are out of arrows!");
            }
        }

        return 0;
    }
    public void useHealthPotion(){
        if(consumables.get("health potion") > 0){
            setHealth(true, 5);
        }
            // set health if greater after using potion
        if(curHealth > maxHealth){
            curHealth = maxHealth;
        }
        addUseConsumable(false, "health potion", 1);
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