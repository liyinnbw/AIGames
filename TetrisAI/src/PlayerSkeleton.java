
public class PlayerSkeleton {
	public String printArrInt(int[] arr){
		String s="{";
		for(int i=0; i<arr.length; i++){
			s+=arr[i]+", ";
		}
		s+="},";
		return s;
	}
	public String printArrDouble(double[] arr){
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
	public double getHeightDeviation (State s){
		double avgHeight = 0; 
		double var = 0;
		int h[] = s.getTop();
		for(int i=0; i<h.length; i++){
			avgHeight+=h[i];
		}
		avgHeight/=h.length;
		for(int i=0; i<h.length; i++){
			var+=(h[i]-avgHeight)*(h[i]-avgHeight);
		}
		return var/h.length;
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
/*	public int calcHolesConverted(int[] grid)
	{
	    int gridMask      = (1 << xsize) - 1;
	    int underMask     = 0;
	    int lNeighborMask = 0;
	    int rNeighborMask = 0;
	    int foundHoles    = 0;

	    for (int y = 0; y < ysize; y++) {
	        int line   = grid[y];
	        int filled = ~line & gridMask;

	        underMask     |= filled;
	        lNeighborMask |= (filled << 1);
	        rNeighborMask |= (filled >> 1);

	        foundHoles += setOnes1024[underMask & line] +
	                      setOnes1024[lNeighborMask & line] +
	                      setOnes1024[rNeighborMask & line];
	    }
	    return foundHoles;
	}*/
	//implement this function to have a working system
	
	public double[] getWeights(State s, int seg1Len, int seg2Len){
		double[] weights = new double[seg1Len+seg2Len+2+1];
		//base weight
		weights[0]=0;
		
		//height should be small for good
		for(int i=1; i<=seg1Len; i++){
			weights[i]= -0.1/seg1Len;
		}
		
		//height difference should be small for good
		for(int i=seg1Len+1; i<=seg1Len+seg2Len; i++){
			weights[i]= -0.07/seg2Len;
		}
		
		//when maxHeight is touching ceiling, evaluation function should give value approaching 0 or less
		weights[seg1Len+seg2Len+1]= -0.01;//-0.01/s.ROWS; //(s.ROWS-4);
		
		//num of holes should be small
		weights[seg1Len+seg2Len+2]= -0.01;//-0.1;

		return weights;
	}
	public double evaluate(State s){
		int[] heights = getColumnHeights(s);
		int[] heightDiff = getHeightDiff(s);
		int maxHeight = getMaxHeight(s);
		int numHole = getHoleCount(s.getField());
		
		int numFeatures = 21;
		int[] features = new int[numFeatures];
		double[] weights = getWeights(s, heights.length, heightDiff.length);
		System.arraycopy(heights, 0, features, 0, heights.length);
		System.arraycopy(heightDiff, 0, features, heights.length, heightDiff.length);
		features[features.length-2]=maxHeight;
		features[features.length-1]=numHole;

		double V = weights[0];
		for(int i=0; i<features.length; i++){
			V+=features[i]*weights[i+1];
		}
		return V;
	}
	public double evaluatePrint(State s){
		int[] heights = getColumnHeights(s);
		int[] heightDiff = getHeightDiff(s);
		int maxHeight = getMaxHeight(s);
		int numHole = getHoleCount(s.getField());
		
		int numFeatures = 21;
		int[] features = new int[numFeatures];
		double[] weights = getWeights(s, heights.length, heightDiff.length);
		System.arraycopy(heights, 0, features, 0, heights.length);
		System.arraycopy(heightDiff, 0, features, heights.length, heightDiff.length);
		features[features.length-2]=maxHeight;
		features[features.length-1]=numHole;

		double V = weights[0];
		for(int i=0; i<features.length; i++){
			V+=features[i]*weights[i+1];
		}
		System.out.println("col heights = "+printArrInt(heights));
		System.out.println("height diff = "+printArrInt(heightDiff));
		System.out.println("max height  = "+maxHeight);
		System.out.println("num of hole = "+numHole);
		System.out.println("features    = "+printArrInt(features));
		System.out.println("weights     = "+printArrDouble(weights));
		System.out.println("V = "+V);
		return V;
	}
	public int pickMove(State s, int[][] legalMoves) {

		double maxU = -1000000;
		double maxV = 0;
		int maxR =0;
		int maxUAction = 0;
		for(int i=0; i<legalMoves.length; i++){

			SimState ss = new SimState();
			ss.setField(s.getField());
			ss.setNextPiece(s.getNextPiece());
			ss.setTop(s.getTop());
			ss.makeMove(legalMoves[i]);
			if(ss.hasLost()){
				System.out.println("lost move idx = "+i);
				continue;
			}
			
			double V = evaluate(ss);
			int R = ss.getRowsCleared();
			double U = R+V;
			if(U>maxU){
				maxU = U;
				maxUAction = i;
				maxV = V;
				maxR = R;
			}
		}
		System.out.println("Action = "+ printArrInt(legalMoves[maxUAction]));
		System.out.println ("Action="+maxUAction+" U="+maxU+" R="+maxR+" V="+maxV);
		/*
		SimState ss = new SimState();
		ss.setField(s.getField());
		ss.setNextPiece(s.getNextPiece());
		ss.setTop(s.getTop());
		ss.makeMove(legalMoves[maxUAction]);
		int[][]field = ss.getField();
		//for(int i=0; i<field.length; i++)
		//	System.out.println(printArrInt(field[i]));
		evaluatePrint(ss);*/
		
		return maxUAction;
	}
	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		int score =0;
		while(!s.hasLost()) {
			score++;
			System.out.println("====move "+score+"====");
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
