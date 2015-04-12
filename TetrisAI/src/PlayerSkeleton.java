import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


public class PlayerSkeleton {
	
	// Global result for lotus swarm
	static double[] result_particle = null;
	static int result_best = 0;
	
	public String printArrInt(int[] arr){
		String s="{";
		for(int i=0; i<arr.length; i++){
			s+=arr[i]+", ";
		}
		s+="},";
		return s;
	}
	public static String printArrDouble(double[] arr){
		String s="";
		for(int i=0; i<arr.length; i++){
			s+=arr[i]+" ";
		}
		return s;
	}
	public String print2DArr(int[][] arr){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<arr.length; i++){
			for(int j=0; j<arr[i].length; j++){
				sb.append(arr[i][j]+" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	public int[] getColumnHeights (State s){
		int h[] = new int[s.COLS];
		System.arraycopy(s.getTop(), 0, h, 0, s.COLS);
		return h;
	}
	public int[] getHeightDiff (State s){
		int hdiff[] = new int[s.COLS-1];
		int h[] = s.getTop();
		for(int i=0; i<hdiff.length; i++){
			hdiff[i]=Math.abs(h[i+1]-h[i]);
		}
		return hdiff;
	}
	public double getHeightMean (State s){
		int h[] = s.getTop();
		double sum = 0;
		for(int i=0; i<h.length; i++){
			sum += h[i];
		}
		return sum/h.length;
	}
	public double getHeightStd (State s){
		int h[] = s.getTop();
		double sum = 0;
		for(int i=0; i<h.length; i++){
			sum += h[i];
		}
		double mean = sum/h.length;
		double var = 0;
		for(int i=0; i<h.length; i++){
			var += (h[i]-mean)*(h[i]-mean);
		}
		return Math.sqrt(var/h.length);
	}

	public int getMaxHeight (State s){
		int h[] = s.getTop();
		int max = h[0];
		for(int i=1; i<h.length; i++){
			if(max<h[i]) max = h[i];
		}
		return max;
	}
	public int getHoleCount (int[][] field){

		int[][] holes = new int[field.length][field[0].length];
		
		int count =0;
		for(int i=field.length-2; i>=0; i--){
			for(int j=0; j<field[i].length; j++){
				if(field[i][j]==0){
					//check left
					if(!(j==0 || field[i][j-1]!=0 || holes[i][j-1]==1)){
						continue;
					}
					//check up (the field is up-down inverted)
					if(!(field[i+1][j]!=0 || holes[i+1][j]==1)){
						continue;
					}
					//check right
					if(j==field[i].length-1){
						holes[i][j]=1;
						count++;
					}else if(field[i][j+1]!=0){
						holes[i][j]=1;
						count++;
					}else{
						//right is zero, need to check if right is a hole
						for(int k=j+1; k<field[i].length; k++){
							//check up (the field is up-down inverted)
							if(!(field[i+1][k]!=0 || holes[i+1][k]==1)){
								//not a hole
								break;
							}
							//check right
							if(k==field[i].length-1 || field[i][k+1]!=0){
								holes[i][j]=1;
								count++;
								break;
							}
						}
						continue;
					}
				}	
			}
		}
		return count;
	}

	public double evaluate0(State s, State os, double[] weights){
		int[] heights 		= getColumnHeights(s);
		int[] heightDiff 	= getHeightDiff(s);
		int maxHeight 		= getMaxHeight(s);
		int holeIncreased 	= getHoleCount(s.getField());
		
		int numFeatures = 22;
		int[] features = new int[numFeatures];
		System.arraycopy(heights, 0, features, 0, heights.length);
		System.arraycopy(heightDiff, 0, features, heights.length, heightDiff.length);
		features[features.length-3]=maxHeight;
		features[features.length-2]=holeIncreased;
		features[features.length-1]=1;
		
		double V = 0;
		for(int i=0; i<heights.length; i++){
			V+=features[i]*weights[0];
		}
		for(int i=heights.length; i<heights.length+heightDiff.length; i++){
			V+=features[i]*weights[1];
		}
		V+=features[features.length-3]*weights[weights.length-3];
		V+=features[features.length-2]*weights[weights.length-2];
		V+=features[features.length-1]*weights[weights.length-1];
		return V;
	}
	public double evaluate1(State s, State os, double[] weights){
		int[] heights 		= getColumnHeights(s);
		int[] heightDiff 	= getHeightDiff(s);
		int maxHeight 		= getMaxHeight(s);
		int holeIncreased 	= getHoleCount(s.getField())-getHoleCount(os.getField());
		
		int numFeatures = 22;
		int[] features = new int[numFeatures];
		System.arraycopy(heights, 0, features, 0, heights.length);
		System.arraycopy(heightDiff, 0, features, heights.length, heightDiff.length);
		features[features.length-3]=maxHeight;
		features[features.length-2]=holeIncreased;
		features[features.length-1]=1;
		
		double V = 0;
		for(int i=0; i<heights.length; i++){
			V+=features[i]*weights[0];
		}
		for(int i=heights.length; i<heights.length+heightDiff.length; i++){
			V+=features[i]*weights[1];
		}
		V+=features[features.length-3]*weights[weights.length-3];
		V+=features[features.length-2]*weights[weights.length-2];
		V+=features[features.length-1]*weights[weights.length-1];
		return V;
	}
	public double evaluate2(State s, State os, double[] weights){

		int numFeatures 	= 5;
		double[] features 	= new double[numFeatures];
		features[0]			= getHeightMean(s);
		features[1]			= getHeightStd(s);
		features[2]			= getMaxHeight(s);
		features[3]			= getHoleCount(s.getField())-getHoleCount(os.getField());
		features[4]			= 1;	//bias term
		//System.out.println(printArrDouble(features));
		//double[] weights 	= {-0.0, -0.8, -0.1, -0.1, -0.0};
		
		double V = 0;
		for(int i=0; i<features.length; i++){
			V+=features[i]*weights[i];
		}
		return V;
	}
	public double evaluate3(State s, State os, double[] weights){

		int numFeatures 	= 5;
		double[] features 	= new double[numFeatures];
		features[0]			= getHeightMean(s)-getHeightMean(os);
		features[1]			= getHeightStd(s);
		features[2]			= getMaxHeight(s)-getMaxHeight(os);
		features[3]			= getHoleCount(s.getField())-getHoleCount(os.getField());
		features[4]			= 1;	//bias term
		//System.out.println(printArrDouble(features));
		//double[] weights 	= {-0.0, -0.8, -0.1, -0.1, -0.0};
		
		double V = 0;
		for(int i=0; i<features.length; i++){
			V+=features[i]*weights[i];
		}
		return V;
	}
	
	public int pickMove(State s, int[][] legalMoves, double[] weights) {

		double maxU = -1000000;
		double maxV = 0;
		int maxR =0;
		int maxUAction = 0;
		
		for(int i=0; i<legalMoves.length; i++){
			//simulate a move
			SimState ss = new SimState();
			ss.setField(s.getField());
			ss.setNextPiece(s.getNextPiece());
			ss.setTop(s.getTop());
			ss.makeMove(legalMoves[i]);
			
			//avoid lost move
			if(ss.hasLost()){
				//System.out.println("lost move idx = "+i);
				continue;
			}
			
			//evaluate move
			double V = evaluate0(ss, s, weights);
			int R = ss.getRowsCleared();
			double U = R+V;
			
			//update selection
			if(U>maxU){
				maxU = U;
				maxUAction = i;
				maxV = V;
				maxR = R;
			}
		}
		//System.out.println("Action = "+ printArrInt(legalMoves[maxUAction]));
		//System.out.println ("Action="+maxUAction+" U="+maxU+" R="+maxR+" V="+maxV);
		
		return maxUAction;
	}
	
	public static int runOnce(double[] weights, boolean visualOn){
		if(visualOn){
			State s = new State();
			TFrame t = new TFrame(s);
			PlayerSkeleton p = new PlayerSkeleton();
			int move =0;
			while(!s.hasLost()) {
				move++;
				s.makeMove(p.pickMove(s,s.legalMoves(),weights));
				
				s.draw();
				s.drawNext(0,0);
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int score = s.getRowsCleared();
			//System.out.println("moves made = "+move);
			//System.out.println("You have completed "+score+" rows.");
			
			t.dispose();
			return score;
		}else{
			State s = new State();
			PlayerSkeleton p = new PlayerSkeleton();
			int move =0;
			while(!s.hasLost()) {
				move++;
				s.makeMove(p.pickMove(s,s.legalMoves(),weights));
			}
			int score = s.getRowsCleared();
			//System.out.println("moves made = "+move);
			//System.out.println("You have completed "+score+" rows.");
			return score;
		}
	}
	public static double[] mutate(double[] arr){
		double[] mutated = new double[arr.length];
		System.arraycopy(arr, 0, mutated, 0, arr.length);
		Random r = new Random();
		int 	randIdx 	= r.nextInt(arr.length-0) + 0;
		double 	randValue 	= -(r.nextInt(101-0) + 0)/100.0;
		mutated[randIdx] = randValue;
		return mutated;
		
	}
	
	// 
	public static double[] lotusSwarm(double[] arr,int level,double[] roof,double[] floor){
		ArrayList <double[]> particle_list = new ArrayList<double[]>();
		int num_particles = 8;
		int depth_limit = 3;
		double small_radius = (roof[0] - floor[0])/100;
		
		if(level >= depth_limit){
			// if achieved maximum depth, stop
		    return null;
		}
		
		// generate child particles to continue search
		if(result_particle == null){
			// if original particle, initialize
			System.out.println("Initialize");
			for(int k=0;k<num_particles;k++){
				Random random = new Random();
				double[] new_particle = {random.nextDouble()*(roof[0]-floor[0])+floor[0],
						random.nextDouble()*(roof[1]-floor[1])+floor[1],
						random.nextDouble()*(roof[2]-floor[2])+floor[2],
						random.nextDouble()*(roof[3]-floor[3])+floor[3],
						random.nextDouble()*(roof[4]-floor[4])+floor[4]};
				particle_list.add(new_particle);
			}
		}else if(Arrays.equals(arr, result_particle)){
			// if self is best particle, randomly search near regions
			System.out.println("Search around best");
			for(int k=0;k<num_particles;k++){
				Random random = new Random();
				Random sign = new Random();
				if((sign.nextInt() & 1) == 0) small_radius = -small_radius;
				double[] new_particle = {random.nextDouble()*(small_radius)+arr[0],
						random.nextDouble()*(small_radius)+arr[1],
						random.nextDouble()*(small_radius)+arr[2],
						random.nextDouble()*(small_radius)+arr[3],
						random.nextDouble()*(small_radius)+arr[4]};
				particle_list.add(new_particle);
			}
		}else{
			// if self is not the best particle, in high possibility we random search towards the the best particle
			// and in low possibility give random search
			for(int k=0;k<num_particles;k++){
				Random possibility = new Random();
				if(possibility.nextDouble()>0.3){
					System.out.println("Search towards best");
					Random random = new Random();
					double[] new_particle = {random.nextDouble()*(result_particle[0]-arr[0])+arr[0],
						random.nextDouble()*(result_particle[1]-arr[1])+arr[1],
						random.nextDouble()*(result_particle[2]-arr[2])+arr[2],
						random.nextDouble()*(result_particle[3]-arr[3])+arr[3],
						random.nextDouble()*(result_particle[4]-arr[4])+arr[4]};
					particle_list.add(new_particle);
				}else{
					System.out.println("Random behavior");
					Random random = new Random();
					double[] new_particle = {random.nextDouble()*(roof[0]-floor[0])+floor[0],
							random.nextDouble()*(roof[1]-floor[1])+floor[1],
							random.nextDouble()*(roof[2]-floor[2])+floor[2],
							random.nextDouble()*(roof[3]-floor[3])+floor[3],
							random.nextDouble()*(roof[4]-floor[4])+floor[4]};
					particle_list.add(new_particle);
				}
			}
		}
		
		// Compare all childen's performance
		for(Iterator<double[]> child = particle_list.iterator(); child.hasNext(); ) {
			double[] current = child.next();
			// run 5 time per weight, to get average
			int sum = 0;
			int counter = 5;
			while(counter>0){
				int result = runOnce(current,false);
				sum += result;
				counter--;
			}
			
			int result = sum/5;
			System.out.print("score = "+result + "  ");
			System.out.println("best = "+result_best + "  ");
			
		    if (result>result_best) {
		    	result_best = result;
		    	result_particle = current;
		    }
		}
		
		// let sub-swarm continue sub-region search
		for(Iterator<double[]> child = particle_list.iterator(); child.hasNext(); ) {
			double[] current = child.next();
			lotusSwarm(current,(level+1),roof,floor);
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {	
		
		
		//so far best weights for evaluate2
		//double[] bestWeights= {-0.81, -0.35, -0.97, -0.04, -0.13};
		
		//so far best weights for evaluate1
		//double[] bestWeights= {-0.79, -0.15, -0.14, -0.1, -0.75};
		
		//so far best weights for evaluate3
		//double[] bestWeights= {-0.99, -0.61, 0.0, -0.1, -0.23};
		
		//so far best weights for evaluate0
		
		/* Original before Wen Xia try lotus swarm
		
		double[] bestWeights= {-0.8, -0.15, -0.14, -0.1, -0.39};
		
		int best=0;
		while(true){
			//Run once
			int result = runOnce(bestWeights,true);
			System.out.print("score = "+result);
			if(result>best) best =result;
			System.out.println("best = "+best);
		}
		*/
		
		// Test for lotus swarm
		int best=0;
		double[] roof = {0,0,0,0,0};
		double[] floor = {-1,-1,-1,-1,-1};
		lotusSwarm(roof,0,roof,floor);
		double[] bestWeights= result_particle;
		int threshold_test = 10;
		
		System.out.println("best particle: ");
		for (double speed : result_particle) {
            System.out.println(speed);
        }	
		

		for(int i = 0;i<threshold_test;i++){
			int result = runOnce(bestWeights,false);
			System.out.print("result score = "+result + "  ");
			if(result>best) best =result;
			System.out.println("result best = "+best);
		}		
		
		/*
		//test
	 	Scanner sc = new Scanner(System.in);
		int testRunSize = sc.nextInt();
		int runTimes = 50;
		int bestScore= 0;
		for(int i=0; i<runTimes; i++){
			bestScore += runOnce(bestWeights,false)*1.0/runTimes;
		}
		System.out.println("mean score = "+bestScore);
		while(testRunSize-->0){
			double[] weights = mutate(bestWeights);
			System.out.println("try = "+printArrDouble(weights));
			int meanScore = 0;
			
			for(int i=0; i<runTimes; i++){
				meanScore += runOnce(weights,false)*1.0/runTimes;
			}
			System.out.println("mean score = "+meanScore);
			if(meanScore>bestScore) {
				bestScore = meanScore;
				bestWeights = weights;
				System.out.println("updated weight = "+printArrDouble(bestWeights));
			}
		
		}
		System.out.println("best score = "+bestScore);
		System.out.println("best weight = "+printArrDouble(bestWeights));*/
	}
	
}
