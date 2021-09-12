package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {
	
	private static ArrayList<String> createShortestChain(Graph g, ArrayList<Integer> visitedPeople, int p1Index) {
		ArrayList<String> result = new ArrayList<String>();
		Stack<Integer> friendChain = new Stack<Integer>();
		friendChain.push(visitedPeople.get(visitedPeople.size()-1));
		friendChain.push(visitedPeople.get(visitedPeople.size()-2));
		int visitedPeopleIndex = visitedPeople.indexOf(visitedPeople.get(visitedPeople.size()-2));
		while(true) {
			if(friendChain.peek() == p1Index) {
				break;
			}
			friendChain.push(visitedPeople.get(visitedPeopleIndex-1));
			visitedPeopleIndex = visitedPeople.indexOf(visitedPeople.get(visitedPeopleIndex-1));
		}
		while(!friendChain.isEmpty()) {
			result.add(g.members[friendChain.pop()].name);
		}
		return result;
	}

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		boolean[] visited = new boolean[g.members.length];
		Queue<Integer> q = new Queue<Integer>();
		ArrayList<Integer> visitedPeople = new ArrayList<Integer>();
		boolean targetFound = false;
		int p1Index = g.map.get(p1);
		visited[p1Index] = true;
		q.enqueue(p1Index);
		visitedPeople.add(-1);
		visitedPeople.add(p1Index);
		while(!q.isEmpty()) {
			int n = q.dequeue();
			for(Friend f = g.members[n].first;f != null;f = f.next) {
				if(!visited[f.fnum]) {
					if(g.members[f.fnum].name.equals(p2)) {
						visited[f.fnum]= true;
						visitedPeople.add(n);
						visitedPeople.add(f.fnum);
						targetFound = true;
						break;
					}
					visited[f.fnum] = true;
					q.enqueue(f.fnum);
					visitedPeople.add(n);
					visitedPeople.add(f.fnum);
				}
			}
			if(targetFound) {
				break;
			}
		}
		if(targetFound) {
			return createShortestChain(g, visitedPeople, p1Index);
		} else {
			return null;
		}
	}
	
	private static ArrayList<String> findClique(Graph g, int startIndex, boolean[] visited, String school) {
		ArrayList<String> studentsInClique = new ArrayList<String>();
		Queue<Integer> q = new Queue<Integer>();
		visited[startIndex] = true;
		studentsInClique.add(g.members[startIndex].name);
		q.enqueue(startIndex);
		while(!q.isEmpty()) {
			int index = q.dequeue();
			for(Friend f = g.members[index].first;f != null;f = f.next) {
				// Check if person is a student.
				if(g.members[f.fnum].student) {
					if(!visited[f.fnum] && g.members[f.fnum].school.equals(school)) {
						studentsInClique.add(g.members[f.fnum].name);
						q.enqueue(f.fnum);
					}
				}
				visited[f.fnum] = true;
			}
		}
		return studentsInClique;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		for(int i=0;i < visited.length;i++) {
			if(g.members[i].student) {
				if(!visited[i] && g.members[i].school.equals(school)) {
					result.add(findClique(g, i, visited, school));
				}
			}
		}
		return result;
	}
	
	private static void findConnectors(int index, Graph g, boolean[] visited, int[][]dfsNumBack, int dfsNum, ArrayList<String> result, boolean rootConnector) {
		visited[index] = true;
		dfsNumBack[index][0] = dfsNum;
		dfsNumBack[index][1] = dfsNum;
		for(Friend f = g.members[index].first;f != null;f = f.next) {
			if(!visited[f.fnum]) {
				dfsNum++;
				findConnectors(f.fnum, g, visited, dfsNumBack, dfsNum, result, rootConnector);
				// This is where the backing up happens.
				if(dfsNumBack[index][0] > dfsNumBack[f.fnum][1]) {
					dfsNumBack[index][1] = Math.min(dfsNumBack[index][1],dfsNumBack[f.fnum][1]);
				}
				if(dfsNumBack[index][0] <= dfsNumBack[f.fnum][1]) {
					if(dfsNumBack[index][0] != 1 && !result.contains(g.members[index].name)) {
						result.add(g.members[index].name);
					} else {
						// What to do if the root may be a connector.
						if(rootConnector) {
							result.add(g.members[index].name);
						} else {
							rootConnector = true;
						}
					}
				}
			} else {
				dfsNumBack[index][1] = Math.min(dfsNumBack[index][1],dfsNumBack[f.fnum][0]);
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> result = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		int[][] dfsNumBack = new int[g.members.length][2];
		for(int i=0;i < visited.length;i++) {
			boolean rootConnector = false;
			if(!visited[i]) {
				findConnectors(i, g, visited, dfsNumBack, 1, result, rootConnector);
			}
		}
		return result;
	}
}