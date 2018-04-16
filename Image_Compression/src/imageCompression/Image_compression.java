package imageCompression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import Jama.Matrix;

public class Image_compression {

	private static Scanner scanImg;
	private static int image_height;
	private static int image_width;
	private static int max_gray_value;
	private static ArrayList<ArrayList<Integer>> values;
	private static DataInputStream input;
	private static String filename;
	private static boolean transpose;
	private static Matrix U;
	private static Matrix S;
	private static Matrix V;
	private static int rank;
	private static DataOutputStream output;
	private static Matrix A;

	public static void main(String args[]) throws IOException {
		int option =Integer.parseInt(args[0]);
		File file = new File(args[1]);
		if(args!=null && args.length>0) {
			if(option==1) {
				ASCIIToBinary(file);
			}else if(option ==2) {
				binaryToASCII(file,args[1]);
			}else if(option==3) {
				makeSVD(args[1],args[2],args[3]);
			}else if(option == 4) {
				readSVD(args[1]);
			}
		}
	}

	private static void readSVD(String args) throws IOException {
	try
	      {
	         input = new DataInputStream(new FileInputStream(args));
	         if (input.readBoolean() == true)
	         {
	            transpose = false;
	         }
	         else
	         {
	            transpose = true;
	         }
	         int uRowDimension = input.readShort(); 
	         int vRowDimension = input.readShort();
	         rank = input.readShort();              
	         U = new Matrix(uRowDimension, rank);
	         S = new Matrix(rank, rank);
	         V = new Matrix(vRowDimension, rank);
	         	
	         for (int i = 0; i < uRowDimension; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               U.set(i, j, input.readShort() / 32768d);
	               System.out.println("u matrix elements"+U.get(i, j));
	            }
	         }
	         for (int i = 0; i < rank; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               if (i == j)
	               {
	                  S.set(i, j, input.readFloat());
	               }
	               else
	               {
	                  S.set(i, j, 0.0);
	               }
	            }
	         }
	         for (int i = 0; i < vRowDimension; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               V.set(i, j, input.readShort() / 32768d);
	            }
	         }
	         input.close();
	         
	         A=U.times(S.times(V.transpose()));
	         if(transpose)
	         {
	        	 A=A.transpose();
	         }
	         
	         
	         double[][] arr = A.getArray();
	         int count=0;
	         
	         int[][] grays =
		               new int[A.getRowDimension()][A.getColumnDimension()];
		         int n = 0;
		       //  System.out.println("length"+grays.length);
		         for (int i = 0; i < grays.length; i++)
		         {
		              System.out.println("width"+grays[i].length);

		        	 for (int j = 0; j < grays[i].length; j++)
		            {
		        		// System.out.println("A matrix is"+A.get(i, j));
		            	n = (int) A.get(i, j);
		            	
		               if (n < 0)
		               {
		                   count++;
		            	   grays[i][j] = 0;
		               }
		               else if (n > 255)
		               {
		                  grays[i][j] = 255;
		               }
		               else
		               {
		                  grays[i][j] = n;
		               }
		            }
		         }
		         
		         System.out.println("negative values"+count);
		         String filename = args.split("_b\\.pgm\\.")[0];
		         String target = filename + "_k.pgm";
		         PrintWriter output = new PrintWriter(target);
		         output.println("P2");
		         output.println("#Final Image");
		         output.println(grays[0].length + " " + grays.length);
		         output.println(255);
		         for (int i = 0; i < grays.length; i++){
		            for (int j = 0; j < grays[i].length; j++){
		               output.print(grays[i][j] + " ");
		             }
		            output.println();
		         }
		         output.close();
		   
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }

	}

	private static void makeSVD(String header, String svd, String k) throws FileNotFoundException {
		// TODO Auto-generated method stub
		try {
		Scanner head = new Scanner(new File(header));
		Scanner svdecom = new Scanner(new File(svd));
		rank = Integer.parseInt(k);
		int height = head.nextInt();
		int width = head.nextInt();
		if(width>height) {
			height = width + height - (width = height);
			transpose = true;
		}else transpose = false;
		U = new Matrix(height, width);
        S = new Matrix(width, width);
        V = new Matrix(width, width);
        head.close();
        for (int i = 0; i < U.getRowDimension(); i++){
           for (int j = 0; j < U.getColumnDimension(); j++){
              U.set(i, j, svdecom.nextDouble());
           }
        }
        for (int i = 0; i < S.getRowDimension(); i++){
           for (int j = 0; j < S.getColumnDimension(); j++){
              S.set(i, j, svdecom.nextDouble());
           }
        }
        for (int i = 0; i < V.getRowDimension(); i++){
           for (int j = 0; j < V.getColumnDimension(); j++){
              V.set(i, j, svdecom.nextDouble());
           }
        }
        svdecom.close();

        output = new DataOutputStream(new FileOutputStream("Image_b.pgm.SVD"));
        if (transpose)output.writeBoolean(true);
        else output.writeBoolean(false);
        output.writeShort(U.getRowDimension());     
        output.writeShort(V.getColumnDimension());  
        output.writeShort(rank);                    
        for (int i = 0; i < U.getRowDimension(); i++)
           for (int j = 0; j < rank; j++)
        	   output.writeShort((short) (U.get(i, j) * 32768d));
        for (int i = 0; i < rank; i++)
        	output.writeFloat((float) (S.get(i, i)));
        for (int i = 0; i < V.getRowDimension(); i++)
           for (int j = 0; j < rank; j++)
        	   output.writeShort((short) (V.get(i, j) * 32768d));
        output.close();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private static void binaryToASCII(File file, String args) throws IOException {
		// TODO Auto-generated method stub
		input = new DataInputStream(new BufferedInputStream(new FileInputStream(args)));
		filename = file.getName();
		byte[] imgdimen = new byte[5];
		input.read(imgdimen);
		image_height =  integralValue(byteToInt(imgdimen[2])+byteToInt(imgdimen[3]));
		image_width =  integralValue(byteToInt(imgdimen[0])+byteToInt(imgdimen[1]));
		System.out.println("print width :"+image_width);
        System.out.println("print height :"+image_height);
		Byte b = new Byte(imgdimen[4]);
		if(b.intValue()<0) max_gray_value = 256+b.intValue();
		else max_gray_value = b.intValue();
		PrintWriter wr = new PrintWriter(filename.substring(0,filename.indexOf("."))+ "2" + ".pgm");
		wr.println("P2");
		wr.write(image_width+" ");
		wr.write(image_height+"\n");
		wr.write(max_gray_value+"\n");
		byte img_values[]=new byte[image_width*image_height];
		input.read(img_values);
		int index=0;
		int pixel_count=0;
		for(int i=0;i<image_height;i++)
		{
			for(int j=0;j<image_width;j++)
		    {
			Byte c = new Byte(img_values[index]);
			int pixel_val=0;
			if((new Byte(c)).intValue()<0)
			{
				pixel_val=256+new Byte(c).intValue();
				pixel_count++;
			}
			else
				pixel_val=(new Byte(c)).intValue();
			wr.write(pixel_val+" ");
			index++;
		    }
		    wr.write("\n");
		}
		wr.close();
		System.out.println("count :"+pixel_count);
		
	}
	public static String toBinary(int a)
	{
		String x=Integer.toBinaryString(a);
		if(x.length()<=8)
		{
			for(int i=x.length();i<8;i++) {
				x="0"+x;
			}
		}
		return x;
	}
	static String byteToInt(Byte b) {
		// TODO Auto-generated method stub
		if(b.intValue()<0)
		{
			return toBinary(256+b.intValue());
		}
		else
			return toBinary(b.intValue());
	}
	static int integralValue(String str) {
		// TODO Auto-generated method stub
		int x=0;
		StringBuffer sb=new StringBuffer(str);
		sb.reverse();
		for(int i=0;i<sb.length();i++) {
			if(sb.charAt(i)=='1') x+=Math.pow(2, i);
		}
	    return x;	
	}

	private static void ASCIIToBinary(File file) throws IOException {
		// TODO Auto-generated method stub
		values = new ArrayList<ArrayList<Integer>>();
		scanImg = new Scanner(file);
		filename = file.getName();
		//Removing comment lines
		scanImg.nextLine();
		if (scanImg.hasNext("#"))
			scanImg.nextLine();
		//Get the details of the image
		image_height = scanImg.nextInt();
		image_width = scanImg.nextInt();
		max_gray_value = scanImg.nextInt();
		//store values in an array
		for(int i=0;i<image_height;i++) {
			ArrayList<Integer> ar = new ArrayList<Integer>();
			for(int j=0;j<image_width;j++) {
				ar.add(scanImg.nextInt());
			}
			values.add(ar);
		}
		
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new
                FileOutputStream(filename.substring(0,filename.indexOf("."))+ "_b" + ".pgm")));
		//using two bytes for saving image height and image width
		out.writeShort(image_height);
		out.writeShort(image_width);
		//using 1 byte to save the gray scale values
	    out.writeByte(max_gray_value);
	    
	    for(ArrayList<Integer> a:values) {
	    	for(int i=0;i<a.size();i++) {
	    		out.writeByte(a.get(i));
	    	}
	    }
	    out.flush();
	    out.close();
	    System.out.println("Created Binary File!");
	}
}
