package DSA;

public class SetMatrixZero {
    
    public static void setZeroes(int[][] matrix) {
	int rows = matrix.length;
	int cols = matrix[0].length;
	
	// mark the coordinate of the cell
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                if (matrix[r][c] == 0) {
                    matrix[r][0] = 0;
                    matrix[0][c] = 0;
                }
            }
        }
        
        for (int row=0; row<matrix.length; row++) {
            if (matrix[row][0] == 0) {
               for (int col=0; col<matrix[row].length; col++) {
        	   matrix[row][col]=0;
               }
            }
        }
        
        for (int col=0; col<matrix[0].length; col++) {
            if (matrix[0][col] == 0) {
               for (int row=0; row<matrix.length; row++) {
        	   matrix[row][col]=0;
               }
            }
        }
    }
    
    private static void setValue(int[][] matrix, int row, int col, int val) {
        for (int i=0; i<matrix[row].length; i++) {
            if (val != 0 && matrix[row][i] != 0 && i!=col || matrix[row][i] == -1)
                matrix[row][i]=val;
        }
        for (int i=0; i<matrix.length; i++) {
            if (val != 0 && matrix[i][col] != 0 && i!=row || matrix[i][col] == -1)
                matrix[i][col]=val;
        }
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
	int[][] matrix = {{0,1,2,0},{3,4,5,2},{1,3,1,5}};
	
	display(matrix);
	
	setZeroes(matrix);
	
	display(matrix);

    }

}
