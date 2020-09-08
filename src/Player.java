import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Player{
	//Holds The Player's X , Y And Their Order Number
	private int x, y;
	public int orderNum;

	//Holds The Player Name/Display String, Their Description, And The Room They Are Currently In
	private String name,description,room;

	//Holds The Player's hand
	private Hand hand;

	//Holds A List Of Strings For The Options The Player Has
	private ArrayList<String> adjacentTiles = new ArrayList<>();
	public boolean active = true;

	/**
	 * Constructor
	 * @param name
	 * @param description
	 * @param x
	 * @param y
	 * @param i
	 */
	public Player(String name,String description,int x, int y,int i) {
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
		this.orderNum = i;
	}

	// SETTERS
	// Sets players current x location
	public void setX(int x) {
		this.x = x;
	}

	// Sets players current y location
	public void setY(int y) {
		this.y = y;
	}

	// Sets players hand
	public void setHand(Hand h) {
		this.hand = h;
	}

	// GETTERS
	// Gets players current x location
	public int getX() {
		return x;
	}

	// Gets players current y location
	public int getY() {
		return y;
	}

	// Gets players name
	public String getName() {
		return name;
	}

	// Gets players description
	public String getDescription() { return description; }

	// Gets current Hand;
	public Hand getHand() {
		return hand;
	}


	/**
	 * Moves The Current Player By The Given X And Y While Testing If They Enter A Room
	 * @param X
	 * @param Y
	 * @param b
	 */
	private void move(int X, int Y, Board b) {
		this.y += Y;
		this.x += X;

		if(b.board[this.y][this.x] == "R"){
		if (this.x < 8 && this.y < 8) {
			this.room = "Kitchen";
		} else if (this.x < 11 && this.y < 18 && this.y > 11) {
			this.room = "Dining Room";
		} else if (this.x < 10 && this.y < 36 && this.y > 22) {
			this.room = "Lounge";
		} else if (this.x < 19 && this.x > 9 && this.y < 9) {
			this.room = "Ball Room";
		} else if (this.x < 28 && this.x > 20 && this.y < 7) {
			this.room = "Conservatory";
		} else if (this.x < 28 && this.x > 18 && this.y < 14 && this.y > 8) {
			this.room = "Billiard";
		} else if (this.x < 28 && this.x > 16 && this.y < 24 && this.y > 17) {
			this.room = "Library";
		} else if (this.x < 28 && this.x > 17 && this.y < 36 && this.y > 26) {
			this.room = "Study";
		} else if (this.x < 18 && this.x > 10 && this.y < 36 && this.y > 26) {
			this.room = "Hall";
		}}else {
			this.room = null;
		}
	}

	/**
	 * Causes The Current Player To Execute A Given Action
	 * @param act
	 * @param game
	 */
	public void action(String act, Game game) {
		switch (act) {
		case "UP":
			this.move(0, -1, game.board);
			break;
		case "DOWN":
			this.move(0, 1, game.board);
			break;
		case "LEFT":
			this.move(-1, 0, game.board);
			break;
		case "RIGHT":
			this.move(1, 0, game.board);
			break;
		case "SUGGEST":
			this.makeSuggestion(game);
			break;
		case "ACCUSE":
			this.makeAccusation(game);
			break;
		}
	}

	/**
	 * Tests The Adjacent Tiles To The Current Player To See What Actions They Can Do
	 * @param playerB
	 * @return
	 */
	public ArrayList<String> testAdjacent(Board playerB) {

		if (!playerB.wall.pattern().contains(playerB.board[this.y - 1][this.x])) {
			adjacentTiles.add("UP");
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y + 1][this.x])) {
			adjacentTiles.add("DOWN");
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y][this.x - 1])) {
			adjacentTiles.add("LEFT");
		}
		if (!playerB.wall.pattern().contains(playerB.board[this.y][this.x + 1])) {
			adjacentTiles.add("RIGHT");
		}
		if (playerB.board[this.y][this.x] == "R") {
			adjacentTiles.add("SUGGEST");
		}
		adjacentTiles.add("ACCUSE");
		return adjacentTiles;
	}

	/**
	 * Lets The Player Choose A Card From A List Of Cards While Displaying A Given String
	 * @param cards
	 * @param str
	 * @return
	 */
	private String chooseCard(ArrayList<Card> cards,String str){
		int count = 0;
		// player suggestion
		while(true) {
			count = 0;
			System.out.println(str);
			for (Card a : cards) {
				System.out.println("Press " + count + " For " + a.getName());
				count++;
			}
			Scanner playerInput = new Scanner(System.in);
			try{int input = playerInput.nextInt();
				if(input >-1 && input < cards.size()){
					return cards.get(input).getName();
				}}catch(Exception e){}
			System.out.println("Please Choose An Option From The List");
		}
	}

	/**
	 *
	 * @param game
	 */
	private void makeAccusation(Game game){
		String room = chooseCard(game.allRooms,"What Room Are You Suggesting?");
		String weapon = chooseCard(game.allWeapons,"What Weapon Are You Suggesting?");
		String character = chooseCard(game.allCharacters,"Who Are You Accusing?");
		if(weapon == game.murder_weapon.getName() && room == game.murder_room.getName() && character == game.murderer.getName()){
			game.GameOver = true;
		}else{
		this.active = false;
	}
	}

	/**
	 *
	 * @param game
	 */
	private void makeSuggestion(Game game) {
		String room = this.room;
		String character = chooseCard(game.allCharacters,"What Character Are You Suggesting?");
		String weapon = chooseCard(game.allWeapons,"What Weapon Are You Suggesting?");
		System.out.println("Room: "+room+"\n"+"Character: "+character+"\n"+"Weapon: "+weapon);

		ArrayList<Card> cards = new ArrayList<Card>();
		Player refute = null;
		String card = "";

		for(Weapon w : game.weaponDraw){
			if(w.description == weapon){
				w.y = this.y;
				w.x = this.x;
			}

		}

		for (Player p : game.activePlayers) {
			if(p.getDescription() == character){
				p.setX(this.x);
				p.setY(this.y);
			}

			// check if player is itself
			if (!p.equals(this)) {
				cards = p.compareHand(room, character, weapon);
			}
			if (cards.size() > 0) {
				refute = p;
				break;
			}
		}
			if(cards.size() > 1){
					System.out.println(refute.getDescription());
					card = chooseCard(cards,"Choose Which Card To Reveal?");
			}else if(cards.size() > 0){
				card = cards.get(0).getName();
			}

			try{
		System.out.println(refute.getDescription()+" Reveals: "+card);}catch(Exception e){
				System.out.println("No One Could Reveal A Card");
			}
	}

	/**
	 * Compares The Hand With The Selected Cards For Similarities
	 * @param room
	 * @param player
	 * @param weapon
	 * @return
	 */
	private ArrayList<Card> compareHand(String room, String player, String weapon) {
		ArrayList<Card> cards = new ArrayList<>();
		for (Card a : this.hand.cards) {
			if (a.getName() == room || a.getName() == player || a.getName() == weapon) {
				cards.add(a);
			}
		}
		return cards;
	}
}

//Used For Sorting The List Of Players
class SortbyOrder implements Comparator<Player>
{
	// Used for sorting in ascending order of
	// roll number
	public int compare(Player a, Player b)
	{
		return a.orderNum - b.orderNum;
	}

}
