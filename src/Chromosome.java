import java.util.Collections;
import java.util.Comparator;

public class Chromosome implements Comparable<Chromosome>{
	String seq;
	int fitness;

	public Chromosome(String seq, int fitness) {
		this.seq = seq;
		this.fitness = fitness;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	@Override
	public int compareTo(Chromosome comparechroms) {
        int comparefit=((Chromosome)comparechroms).getFitness();
        /* For Ascending order*/
        return this.fitness-comparefit;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }



}
