package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendsDriver {

	public static void main(String[] args) throws FileNotFoundException {
		//Scanner term1 = new Scanner(System.in);
		//System.out.println("Enter File Name = ");
		Scanner file = new Scanner(new File("friendsList.txt"));
		Graph g = new Graph(file);
		
		/*
		
		ArrayList<String> shortestChain = Friends.shortestChain(g, "bob", "heather");
		if(shortestChain == null || shortestChain.isEmpty()) {
			System.out.println("No path exists.");
		} else {
			System.out.println(shortestChain.toString());
		}
		
		*/
		
		

		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, "rutgers");
		for(int i=0;i < cliques.size();i++) {
			System.out.println(cliques.get(i).toString());
		}
		
		
		/*
		ArrayList<String> connectors = Friends.connectors(g);
		if(connectors == null || connectors.isEmpty()) {
			System.out.println("There are no connectors.");
		} else {
			System.out.println(connectors.toString());
		}
		*/
	}
}
