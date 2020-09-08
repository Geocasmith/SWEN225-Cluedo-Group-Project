import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Game {
    boolean GameOver = false;
    private  ArrayList<Card> characters = new ArrayList<Card>();
    private  ArrayList<Card> weapons = new ArrayList<Card>();
    public  ArrayList<Card> allCharacters = new ArrayList<Card>();
    public  ArrayList<Card> allWeapons = new ArrayList<Card>();
    private  ArrayList<Card> rooms = new ArrayList<Card>();
    public  ArrayList<Card> allRooms = new ArrayList<Card>();
    private  Stack<Card> allCards = new Stack<Card>();
    public ArrayList<Weapon> weaponDraw = new ArrayList<>();
    public boolean colour;
    public Board board;
    ArrayList<Player> activePlayers = new ArrayList<>();
    ArrayList<Player> allPlayers = new ArrayList<>();
    Card murderer;
    Card murder_weapon;
    Card murder_room;


    /**
     * Constructor
     */
    public Game(){
         this.board = new Board(this);
         this.startGame();
    }



    /**
     * Helper method to create all the players and add them to an ArrayList
     * @return list of all players
     */
    private static ArrayList<Player> createPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("S","Miss Scarlett",9,29,1));
        players.add(new Player("M","Colonel Mustard",1,19,2));
        players.add(new Player("W","Mrs. White",11,1,3));
        players.add(new Player("G","Mr. Green",16,1,4));
        players.add(new Player("C","Mrs. Peacock",26,7,5));
        players.add(new Player("P","Professor Plum",26,24,6));
        return players;
    }

    /**
     * Helper method to create all cards in the game and add them to their respective lists
     */
    private void createCards(){
        //Creates all character cards
        characters.add(new Card("Miss Scarlett"));
        characters.add(new Card("Colonel Mustard"));
        characters.add(new Card("Mrs. White"));
        characters.add(new Card("Mr. Green"));
        characters.add(new Card("Mrs. Peacock"));
        characters.add(new Card("Professor Plum"));

        //Adds cards to list of all Characters
        allCharacters.addAll(characters);

        //Creates all weapon cards
        weapons.add(new Card("Candlestick"));
        weapons.add(new Card("Dagger"));
        weapons.add(new Card("Lead Pipe"));
        weapons.add(new Card("Revolver"));
        weapons.add(new Card("Rope"));
        weapons.add(new Card("Spanner"));

        //Fields For Randomly Placing Weapons
        ArrayList<Integer> coordX = new ArrayList<Integer>(){};
        ArrayList<Integer> coordY = new ArrayList<Integer>(){};
        coordX.add(4); coordY.add(4);
        coordX.add(4); coordY.add(14);
        coordX.add(24); coordY.add(3);
        coordX.add(14); coordY.add(5);
        coordX.add(24); coordY.add(12);
        coordX.add(4); coordY.add(28);
        coordX.add(14); coordY.add(28);
        coordX.add(24); coordY.add(28);

        //Create The Weapon Objects And Get Them Ready To Be Displayed
        createWeaponObject(coordX,coordY,"c","Candlestick" );
        createWeaponObject(coordX,coordY, "d","Dagger" );
        createWeaponObject(coordX,coordY,"l","Lead Pipe");
        createWeaponObject(coordX,coordY,"g","Revolver");
        createWeaponObject(coordX,coordY, "r","Rope" );
        createWeaponObject(coordX,coordY, "s","Spanner");

        //Adds cards to list of all Characters
        allWeapons.addAll(weapons);

        //Creates all room cards
        rooms.add(new Card("Kitchen"));
        rooms.add(new Card("Ball Room"));
        rooms.add(new Card("Conservatory"));
        rooms.add(new Card("Billard Room"));
        rooms.add(new Card("Dining Room"));
        rooms.add(new Card("Library"));
        rooms.add(new Card("Lounge"));
        rooms.add(new Card("Hall"));
        rooms.add(new Card("Study"));

        //Adds cards to a list of all rooms
        allRooms.addAll(rooms);
    }

    /**
     * Method that gets the user to input a valid number of players (from 3-6 players).
     * @return number of players playing
     */
    private int chooseNumPlayers(){
        boolean valid = true;

        int numPlayers = 0;

        //Asks user to type in number of players
        System.out.println("Hi, welcome to minecraft cluedo!");
        System.out.println("Please type the number of players 3-6 : ");

        //Loops until the player inputs a valid number of players
        while(valid) {
            Scanner in = new Scanner(System.in);

            //Checks if user input is valid or not. Loops back until a valid number is entered
            if (in.hasNext("3|4|5|6|")) {
                numPlayers=in.nextInt();
                valid=false;
            } else {//invalid
                System.out.println("Please type a number between 3-6");
            }

        }
        return numPlayers;
    }
    /**
     * Method that gets the user to input a valid number of players (from 3-6 players).
     * @return number of players playing
     */
    private int chooseColour(){
        boolean valid = true;

        int colour = 0;

        //Asks user to type in number of players

        System.out.println("Would you like to play in colour?");
        System.out.println("1. Colour");
        System.out.println("2. Black and White");

        //Loops until the player inputs a valid number of players
        while(valid) {
            Scanner in = new Scanner(System.in);

            //Checks if user input is valid or not. Loops back until a valid number is entered
            if (in.hasNext("1|2|")) {
                colour=in.nextInt();
                valid=false;
            } else {//invalid
                System.out.println("Please type a number between 1 or 2");
            }

        }
        return colour;
    }

    /**
     * Starts The Game
     */
    public void startGame(){
        createCards();


        //sets colour
        int colourInt = chooseColour();
        if(colourInt==1){
            colour = true;
        }else{
            colour = false;
        }

        //choose number of players
        int numPlayers = chooseNumPlayers();

        allPlayers = createPlayers();
        //returns all chosen characters
        activePlayers = choosePlayers(allPlayers,numPlayers);

        this.createMurder();
        this.assignCards();
        while(!GameOver){
            for(Player a : activePlayers){
                if(a.active) {
                    System.out.println("Starting Turn: " + a.getDescription());
                    runTurn(a);
                }
            }
        }
    }

    /**
     * Method that lets you choose what players you want to play as
     * @param allPlayers list of all players
     * @param numPlayers list of number of players chosen
     * @return
     */
    private static ArrayList<Player> choosePlayers(ArrayList<Player> allPlayers, int numPlayers){
        ArrayList<Player> displayPlayers = allPlayers;
        int numChosen = 0;//keeps track of how many characters have been chosen
        ArrayList<Player> chosenPlayers = new ArrayList<Player>();//stores chosen characters in a list

        //Loops until all players have chosen characters
        while(numChosen!=numPlayers){
            System.out.println("Choose a character for player: "+ 0);

            //Displays the character name and the number to type to choose
            for(int i = 0;i<displayPlayers.size();i++){
                System.out.println("Press "+i+" for "+displayPlayers.get(i).getDescription());
            }

            //Opens scanner
            Scanner in = new Scanner(System.in);//gets user input

            //Loops until the user makes a valid input
            while(true) {

                //user input is a number
                if (in.hasNextInt()) {
                    int num = in.nextInt();

                    //If user input is a valid number, the chosen character is added to chosenPlayers list and is no longer displayed for picking.
                    //Breaks the loop so the next player can pick
                    if (num < displayPlayers.size()) {//adds character to chosen chars list and removes it so its not shown again
                        chosenPlayers.add(displayPlayers.get(num));
                        displayPlayers.remove(num);
                        numChosen++;
                        break;
                    }
                    //User input invalid number
                    else{
                        System.out.println("Please choose a valid character");
                    }
                }
                //User input not a number
                else {
                    System.out.println("Please choose a valid character");
                    break;
                }
            }
        }

        //Puts all players back into list of all players (chosen and not chosen) and returns list to original order
        for(Player a : chosenPlayers){
            allPlayers.add(a);
            Collections.sort(allPlayers, new SortbyOrder());
        }

        return chosenPlayers;
    }
    //returns the sum of two random numbers from 1-6 for one dice roll
    private int diceRoll() {
        int r1 = (int)(Math.random() * 6) + 1;
        int r2 = (int)(Math.random() * 6) + 1;
        int sum = r1 + r2;
        System.out.println("You have rolled " + sum + ".");
        return sum;
    }

    //randomly selects a player, weapon, and room

    /**
     *
     */
    private void createMurder() {

        //Generates three random indexes for players, weapons, rooms
        int pIndex = (int)(Math.random() * characters.size() + 0);
        int wIndex = (int)(Math.random() * weapons.size() + 0);
        int rIndex = (int)(Math.random() * rooms.size() + 0);

        //Gets the corresponding names from the indexes
         murderer = characters.get(pIndex);
         murder_weapon = weapons.get(wIndex);
         murder_room = rooms.get(rIndex);
        //Removes each object from array lists
        characters.remove(pIndex);
        weapons.remove(wIndex);
        rooms.remove(rIndex);
    }

    //redo here do run turn which takes a player object and calls print out and cins on it. remains true until chosen
    //create player object and run their turn.

    /**
     *
     * @param player
     */
    private void runTurn(Player player){

            //creates arraylist of current adjacent moves to make
            ArrayList<String> adjacent = new ArrayList<String>();
            //player.testSurroundings(); returns a list of if it
            //first roll dice
            int moves = diceRoll();
            //checks if players room is not null(they are in a room)
            //player is not in a room
            while(moves > 0){
                System.out.println("It is "+player.getDescription()+"'s turn. They have "+moves+" moves left.");
                System.out.println("Current Hand: "+player.getHand().printHand());
            board.printBoard(this);
            adjacent = player.testAdjacent(board);
                for (int i = 0; i < adjacent.size(); i++) {
                    System.out.println("Press " + i + " for " + adjacent.get(i));
                }
                int userNum = getUserInput(adjacent.size());//gets number user types in
                player.action(adjacent.get(userNum),this);
                    moves--;

                    //if the players chooses suggest, makes it so their turn ends after that
                    if(adjacent.get(userNum).equals("SUGGEST")||adjacent.get(userNum).equals("ACCUSE")){
                        moves=0;
                    }

                    adjacent.clear();
                }
        System.out.println("Turn Over");

                }


    /**
     * Helper method to get user input number using scanner. Anything but numbers are invalid, you pass in int i and any numbers
     * larger than i will be invalid
     * @return
     */
    private int getUserInput(int i){
        //gets user input
        Scanner input = new Scanner(System.in);
        int inputNum = 0;
        while(true) {
            //checks if next input is a number
            if (!input.hasNextInt()) {
                System.out.println("Error: Expected Number");
                input.next();
            }else{
                //checks if the number is valid (not larger than the parameter i)
                inputNum=input.nextInt();
                if((inputNum<i) && inputNum>-1){
                    break;
                }
                System.out.println("Error: Invalid Number");

            }
        }

        return inputNum;
    }


    /**
     * assigns cards evenly amongst each player
     */
    private void assignCards() {
        //adds players, weapons, and rooms into a master list of cards
        allCards.addAll(characters);
        allCards.addAll(weapons);
        allCards.addAll(rooms);
        Collections.shuffle(allCards); //shuffles deck
       // System.out.println(allCards);

        //initiates players hands
    	for (Player p : activePlayers) {
			p.setHand(new Hand());
    	}

    	//deals out cards
        while (!allCards.isEmpty()) {
        	for (Player p : activePlayers) {
        		if (!allCards.isEmpty()) {
        			p.getHand().addToHand(allCards.pop());
        		}
        	}
        }
    }

    /**
     * Creates Weapon Object
     */
    private void createWeaponObject(ArrayList<Integer> coordX,ArrayList<Integer> coordY,String name,String description){
        int num =(int)(Math.random()*(coordX.size()));
        weaponDraw.add(new Weapon(coordX.get(num),coordY.get(num),name,description));
        coordX.remove(num);
        coordY.remove(num);

    }
}
