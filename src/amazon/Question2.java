package amazon;

public class Question2 {
    
    private static boolean updateServers(int[][] matrix, int nbDay) {
	boolean modified = false;
        for (int row=0; row<matrix.length; row++) {
            for (int col=0; col<matrix[row].length; col++) {
        	if(matrix[row][col] == nbDay) {
        	    if (row>0 && matrix[row-1][col]==0) {
        		matrix[row-1][col] = nbDay+1;
        		modified = true;
        	    }
        	    if (row<matrix.length-1 && matrix[row+1][col]==0) {
        		matrix[row+1][col] = nbDay+1;
        		modified = true;
        	    }
        	    if (col>0 && matrix[row][col-1]==0) {
        		matrix[row][col-1] = nbDay+1;
        		modified = true;
        	    }
        	    if (col<matrix[row].length-1 && matrix[row][col+1]==0) {
        		matrix[row][col+1] = nbDay+1;
        		modified = true;
        	    }
        	    // set the cells updated the previous day to 1
        	    matrix[row][col] = 1;
        	}
            }
        }
        return modified;
    }
    
    private static void display(int[][] matrix) {
        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {
        	System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
	int[][] matrix = {{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1}};
	run(matrix);
	
	System.out.println();
	
	matrix = new int[][]{{1,1,0,1,0},{0,1,0,0,1},{0,0,0,0,0},{0,0,1,0,0},{1,1,1,1,0}};
	run(matrix);
    }

    public static void run(int[][] matrix) {
	int nbDays=1;
	
	display(matrix);
	System.out.println();
	
	while (updateServers(matrix, nbDays)) {
	    nbDays++;
	}
	
	display(matrix);
	System.out.println("nbDays: " + nbDays);
    }

}
