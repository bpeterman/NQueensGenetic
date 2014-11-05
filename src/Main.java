// Blake Peterman
// N-Queens Genetic Algorithm
// 10-16-2014
// Artificial Intelligence
// Course: CS4523
//
//
//
//
//

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {

	private static final int HashMap = 0;
	private static final int String = 0;
	static int boardSize = 0;
	public static int[][] board = null;
	public static int printCount = 0;
	public static int restartCount = 0;
	public static int popSize = 0;
	public static final int MAX_VALUE = Integer.MAX_VALUE;
	public static double mutateRate = .01;

	public static void main(String[] args) {
		System.out.println("N Queens using a Genetic Algorithm");
		System.out.println("Enter Board Size:");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		boardSize = Integer.parseInt(s);

		System.out.println("Enter Population Size:");
		s = in.nextLine();
		popSize = Integer.parseInt(s);
		
		System.out.println("Enter Mutate Rate:");
		s = in.nextLine();
		mutateRate = Double.parseDouble(s);
		
		in.close();
		board = new int[boardSize][boardSize];

		start();

	}

	public static void start() {
		ArrayList<Chromosome> population = new ArrayList<Chromosome>();
		String[] randStrings = genRandStrings();
		String[] strings = randStrings;
		// evaluate(determine fitness of each Chromosome)
		population = evaluate(strings);
		while (true) {
			// sort the list based on the fitness function.
			Collections.sort(population);
			// check to see if any meet criteria
			if (population.get(0).getFitness() == 0) {
				System.out.println("Solution found!");
				stringToMat(population.get(0).getSeq());
				printBoard();
				break;
			}
			// remove weakest 50%
//			int size = population.size();
//			for (int i = size - 1; i > (size / 2) - 1; i--) {
//				population.remove(i);
//			}
			// perform crossover on upper 50%
			population = crossOp(population);

			if (population.size() == 0) {
				System.out.println("No solution found");
				break;
			}
			// random mutate(pick a random amount of strings to mutate, swap
			// random indexes)
			population=mutate(population);

			// evaluate(determine fitness of each Chromosome)
			population = evaluate(getStrings(population));
			if (population.size() == 0) {
				System.out.println("No solution found");
				break;
			}

		}

	}
	
	public static ArrayList<Chromosome> mutate(ArrayList<Chromosome> population) {
		Random rand = new Random();
		int iter = (int) (mutateRate*population.size());
		int n;
		for (int i=0; i<= iter; i++){
			n = rand.nextInt(population.size());
			population.get(n).setSeq(randMutate(population.get(n).getSeq()));
		}

		return population;
	}
	
	public static String randMutate(String s1){
		Random rand = new Random();
		int n1 = rand.nextInt(s1.length());
		int n2 = rand.nextInt(s1.length());
		while(n2==n1){
			n2 = rand.nextInt(s1.length());
		}
		StringBuilder outString = new StringBuilder(s1);
		char temp = s1.charAt(n1);
		outString.setCharAt(n1, s1.charAt(n2));
		outString.setCharAt(n2, temp);
		outString.trimToSize();
		return outString.toString();
	}

	public static ArrayList<Chromosome> crossOp(ArrayList<Chromosome> population) {
		String[] temp = null;
		Chromosome c1 = new Chromosome(null, MAX_VALUE);
		Chromosome c2 = new Chromosome(null, MAX_VALUE);
		for (int i = 0; i < population.size(); i++) {
			if (population.size()-2>=(i+1)) {
				temp = crossOver(population.get(i).getSeq(),
						population.get(i + 1).getSeq(), 4);
				population.remove(i);
				population.remove(i + 1);
				c1.setSeq(temp[0]);
				c2.setSeq(temp[1]);
				population.add(c1);
				population.add(c2);
				c1 = new Chromosome(null, MAX_VALUE);
				c2 = new Chromosome(null, MAX_VALUE);
				i++;
			} else{
				break;
			}
		}
		return population;
	}

	public static String[] crossOver(String s1, String s2, int crossPoint) {
		String[] crossed = new String[2];
		String str1p1 = s1.substring(0, crossPoint);
		String str1p2 = s1.substring(crossPoint, s1.length());
		String str2p1 = s2.substring(0, crossPoint);
		String str2p2 = s2.substring(crossPoint, s1.length());

		crossed[0] = str1p1 + str2p2;
		crossed[1] = str2p1 + str1p2;
		return crossed;
	}

	public static String[] getStrings(ArrayList<Chromosome> population) {
		String[] output = new String[population.size()];
		for (int i = 0; i < population.size(); i++) {
			output[i] = population.get(i).getSeq();
		}
		return output;
	}

	// do evaluate
	public static ArrayList<Chromosome> evaluate(String[] randStrings) {
		ArrayList<Chromosome> population = new ArrayList<Chromosome>();
		Chromosome chrom = new Chromosome(null, boardSize * boardSize);
		for (int i = 0; i < randStrings.length; i++) {
			stringToMat(randStrings[i]);
			chrom.setSeq(randStrings[i]);
			chrom.setFitness(getBoardConflicts());
			population.add(chrom);
			chrom = new Chromosome(null, boardSize * boardSize);
		}
		return population;
	}

	public static String[] genRandStrings() {
		Random rand = new Random();
		String[] randStrings = new String[popSize];
		int n;
		String myString = "";
		for (int j = 0; j < popSize; j++) {
			for (int i = 0; i < boardSize; i++) {
				n = rand.nextInt(boardSize);
				myString += n;
			}
			randStrings[j] = myString;
			myString = "";
		}
		return randStrings;
	}

	public static boolean solve() {
		return true;
	}

	public static void stringToMat(String chrom) {
		initBoard();
		int pos = 0;
		for (int i = 0; i < chrom.length(); i++) {
			pos = Character.getNumericValue(chrom.charAt(i));
			board[i][pos] = i + 1;
		}
		System.out.println(chrom);
	}

	public static int getBoardConflicts() {
		int conflicts = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] > 0) {
					conflicts += getConflicts(i, j, i, j);
					break;
				}
			}
		}

		return conflicts;
	}

	public static void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 0;
			} // j for end
		}// i end
	}

	public static void printBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				System.out.print(board[j][i] + "|");
			} // j for end
			System.out.println();
		} // i for end
		System.out.println("----------------");
		printCount++;
	}

	public static void initQueens() {
		Random rand = new Random();
		for (int i = 0; i < boardSize; i++) {
			placeQueen(i + 1, i, rand.nextInt(boardSize));
		}

	}

	public static void placeQueen(int queenNum, int x, int y) {
		board[x][y] = queenNum;
	}

	public static void pickUpQueen(int queenNum, int x, int y) {
		board[x][y] = 0;
	}

	public static int[] findQueen(int queenNum) {
		int[] result = new int[2];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] == queenNum) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return null;
	}

	public static int getConflicts(int x, int y, int curX, int curY) {

		int count = 0;
		count += searchUp(x, y, curX, curY);
		count += searchDown(x, y, curX, curY);
		count += searchLeft(x, y, curX, curY);
		count += searchRight(x, y, curX, curY);
		count += searchUpLeft(x, y, curX, curY);
		count += searchUpRight(x, y, curX, curY);
		count += searchDownLeft(x, y, curX, curY);
		count += searchDownRight(x, y, curX, curY);

		return count;
	}

	public static int searchUp(int x, int y, int curX, int curY) {
		for (int i = y - 1; i < boardSize && i >= 0; i--) {
			if (board[x][i] > 0 && i != curY && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDown(int x, int y, int curX, int curY) {
		for (int i = y + 1; i < boardSize && i >= 0; i++) {
			if (board[x][i] > 0 && i != curY && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1; i < boardSize && i >= 0; i--) {
			if (board[i][y] > 0 && i != curX && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchRight(int x, int y, int curX, int curY) {
		for (int i = x + 1; i < boardSize && i >= 0; i++) {
			if (board[i][y] > 0 && i != curX && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchUpLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y - 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i--, j--) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchUpRight(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y - 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i++, j--) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDownLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y + 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i--, j++) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDownRight(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y + 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i++, j++) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

}
