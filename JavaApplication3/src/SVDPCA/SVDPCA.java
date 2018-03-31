/*
 * SVD libraries found from https://math.nist.gov/javanumerics/jama/ and inludes the files:
 * CholeskyDecomposition, EigenvalueDecomposition, LUDecomposition, Maths, Matrix, QRDecomposition, 
 * and SingularValueDecomposition
 */
package SVDPCA;

/**
 *
 * @author Cam
 */
public class SVDPCA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[1] == "1"){
        	//Int to Binary Conversion
        		
        }
        else if (args[2] == "2") {
        	//Binary to Int conversion
        }
        else if (args[3] == "3") {
        	//SVD 
        }
        else if (args[4] == "4") {
        	//Image reconstruction 
        }
        else {
        	System.out.println("Incorrect input parameters");
        }
    }
    
}
