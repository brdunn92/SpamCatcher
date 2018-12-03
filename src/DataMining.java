import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
//https://github.com/JunshuaiFeng/EmailSpamChecker

public class DataMining {

  public static void main(String[] args) throws IOException {
	  Scanner scanner = new Scanner(System.in);
	  System.out.print("Enter the file location of the data: ");
	  String filelocation = scanner.nextLine();
	  File f = new File(filelocation);
	  File ftrain = new File(filelocation + "/train");
	  File ftest = new File(filelocation + "/test");
	  if(f.exists() && f.isDirectory()){
		  if(ftrain.isDirectory() && ftest.isDirectory()){
			  System.out.print("Please Select Algorithm (kNN / NB): ");
			  String algorithm = scanner.nextLine();

		    // get Naive Bayes accuracy before filter
			  if(algorithm.equals("NB") || algorithm.equals("nb") || algorithm.equals("Nb") || algorithm.equals("nB")) {
				  Naive naive = new Naive();
				  System.out.println("Naïve Bayes Algorithm");
				  naive.accuracy(filelocation);

			  }
			  else if(algorithm.equals("kNN") || algorithm.equals("knn") || algorithm.equals("KNN")) {
				    // get kNN accuracy before filter
				  KNN knn = new KNN();
				  System.out.println("\nkNN Algorithm");
				  System.out.print("\nPlease enter a value for k: ");
				  if(scanner.hasNextInt()) {
					  int k = scanner.nextInt();
					  scanner.close();
					  knn.kNNaccuracy(filelocation, k);
				  }
				  else {
					  System.out.print("Invalid k entered.");
				  }
				  
			  }
			  else {
				  System.out.print("Input does not match. You entered: " + algorithm);
			  } 
		  }
		  else {
			  System.out.print("Train/Test file not found. \nMake sure files are named 'train' and 'test'.");
		  }

	  }
	  else {
		  System.out.print("Directory not found.");
	  }

  }// end main

}

