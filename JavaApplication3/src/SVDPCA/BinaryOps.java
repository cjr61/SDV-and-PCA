package SVDPCA;

import java.io.*;
import java.util.*;

public class BinaryOps {
	
	public Vector<Integer> ReadFile(String userFileName) throws FileNotFoundException {
	    File infile = new File("../../../"+ userFileName); //images should be placed at the root of the project
	    Scanner scan = new Scanner(infile);
	    Vector<Integer> fileInfo = new Vector<Integer>();
	    while(scan.hasNextInt()) {
	    	fileInfo.add(scan.nextInt());
	    }
	    scan.close();
	    return fileInfo;
	}
	
	public void IntFileToBinary(String userFileName) {
		Vector<Byte> converted = new Vector<Byte>();
		
	}
	
	public void BinaryFileToInt(String userFileName) {
		
	}
}
