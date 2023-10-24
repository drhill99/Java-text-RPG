import java.util.HashMap;

public class PlayerCharacter {
    private String charName;
    private String charClass;
    private int maxHealth;
    private int curHealth;

    private HashMap<String, Integer> classStartingHP = new HashMap<>();

    // default constructor
    public PlayerCharacter(){
    }
    // parametrized constructor
    public PlayerCharacter(String name, String charClass){
        this.charName = name;
        this.charClass = charClass;
        classStartingHP.put("warrior", 20);
        classStartingHP.put("rogue", 16);
        classStartingHP.put("mage", 12);
        initCharacterInfo();

    }
    // setters
    public void initCharacterInfo(){
        maxHealth = classStartingHP.get(charClass);
        curHealth = maxHealth;
    }

    // getters
    public void displayCharacterInfo(){
        System.out.println("Character name: " + charName);
        System.out.println("Character class: " + charClass);
        System.out.println("Character Max HP: " + maxHealth);
        System.out.println("Character Current HP: " + curHealth);
    }
    public String getName(){
        return charName;
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
}