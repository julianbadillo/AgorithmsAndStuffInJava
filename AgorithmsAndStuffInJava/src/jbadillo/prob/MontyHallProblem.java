package jbadillo.prob;

import java.util.Random;
import java.util.function.BooleanSupplier;

/**
 * A Monte-carlo simulation of the Monty Hall problem:
 * Suppose you're on a game show, and you're given the choice of three doors: Behind one door is a car;
 * behind the others, donkey. You pick a door, say No.1, and the host, who knows what's behind the doors, 
 * opens another door, say No.3, which has a donkey. He then says to you, 
 * 	"Do you want to pick door No.2?" Is it to your advantage to switch your choice? 
 * (Whitaker 1990)
 * @author jbadillo
 *
 */
public class MontyHallProblem {
	
	
	private static Random rand = new Random(System.currentTimeMillis());
	
	
	
	public static void main(String[] args) throws Exception {
		MontyHallProblem p = new MontyHallProblem();
		
		double ratio1, ratio2;
		int n = 100000;
		ratio1 = p.playGames(n, () -> p.playWinGame() );
		ratio2 = p.playGames(n, () -> p.playLooseGame() );
		
		
		System.out.println("Games played: " + n);
		System.out.println();
		System.out.printf("Winning strategy radio: %.3f%%\n", ratio1*100);
		System.out.printf("Safe strategy radio: %.3f%%\n", ratio2*100);
	}
	
	/***
	 * Plays N games with the winning strategy
	 * @param n
	 * @return the rate win/loose
	 * @throws Exception 
	 */
	public double playGames(int n, BooleanSupplier game) throws Exception{
		
		int w = 0;
		for (int i = 0; i < n; i++) 
			if(game.getAsBoolean()) w++;
		
		return (double)w / (double) n;
	}
	
	
	
	/**
	 * This emulates the winning strategy, which is
	 * changing the door after the host has shown
	 * one of the donkeys
	 * @return
	 */
	public boolean playWinGame(){
		
		// The car is secretly hidden
		int car = rand.nextInt(3);
		// the player picks a door
		int player1 = rand.nextInt(3);
		
		// the host, who knows what's behind the doors
		// opens a door, different than one picked by the player,
		// that has a donkey
		int open = rand.nextInt(3);
		// choose one that was not picked by player and has not the car 
		while(open == car || open == player1)
			open = (open + 1) % 3;
		
		// player changes his choice to the one not shown by the host
		int player2 = (player1 + 1) % 3;
		while(player2 == open || player2 == player1)
			player2 = (player2 + 1) % 3;
		
		// return if the player wins or loose
		return player2 == car;
	}
	
	/**
	 * This emulates the naive strategy, which is
	 * the player keeps the same door after the hosts has shown
	 * one of the donkeys
	 * @return
	 */
	public boolean playLooseGame(){
		// The car is secretly hidden
		int car = rand.nextInt(3);
		// the player picks a door
		int player1 = rand.nextInt(3);
		
		// the host, who knows what's behind the doors
		// opens a door, different than one picked by the player,
		// that has a donkey
		int open = rand.nextInt(3);
		
		// choose one that was not picked by player and has not the car 
		while(open == car || open == player1)
			open = (open + 1) % 3;
		
		// player does nothing
		
		// return if the player wins or loose
		return player1 == car;
	}

}
