package imageCompression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import Jama.SingularValueDecomposition;
import java.util.Scanner;
import Jama.Matrix;





public class SVD_Decomposition {
	static int height = 0, width = 0, maxPixVal;
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File(args[0]);
		Scanner sc = new Scanner(file);
		sc.nextLine();
		if (sc.hasNext("#"))
			sc.nextLine();
		width = Integer.parseInt(sc.next());
		height = Integer.parseInt(sc.next());
		maxPixVal = Integer.parseInt(sc.next());
		  try
	      {
	         PrintWriter output = new PrintWriter("Header.txt");
	         
	         output.println(width + " " + height + " " + maxPixVal);
	         output.close();
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
		
		Matrix mat = new Matrix(height, width);
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
				mat.set(i,j,Integer.parseInt(sc.next()));
        
		if(width>height)
		{
			mat=mat.transpose();
		}
		
		SingularValueDecomposition svd = new SingularValueDecomposition(mat);
        Matrix u = svd.getU();
        Matrix s = svd.getS();
        Matrix v = svd.getV();
        try
	      { 
	         PrintWriter output = new PrintWriter("SVD.txt");
	         System.out.println("u row dim"+u.getRowDimension()+u.getColumnDimension());
	         System.out.println("v row dem"+v.getRowDimension()+""+v.getColumnDimension());
	         System.out.println("s row dem"+s.getRowDimension()+""+s.getColumnDimension());

	         for (int i = 0; i < u.getRowDimension(); i++)
	         {
	            for (int j = 0; j < v.getRowDimension(); j++)
	            {
	               output.print(u.get(i, j) + " ");
	            }
	         }
	         for (int i = 0; i < v.getRowDimension(); i++)
	         {
	            for (int j = 0; j < v.getRowDimension(); j++)
	            {
	               output.print(s.get(i, j) + " ");
	            }
	         }
	         for (int i = 0; i < v.getRowDimension(); i++)
	         {
	            for (int j = 0; j < v.getRowDimension(); j++)
	            {
	               output.print(v.get(i, j) + " ");
	            }
	         }
	         output.close();
	         System.out.println("Completed");
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	   
		
		
	}
	

}
