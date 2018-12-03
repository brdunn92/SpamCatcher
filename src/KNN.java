import java.io.*;
import java.util.*;

public class KNN {

  // all training files
  public static ArrayList<String> train = new ArrayList<String>();
  // all testing files
  public static ArrayList<String> test = new ArrayList<String>();
  // Hashmap of all words in training data
  public static Map<String, Double> words = new HashMap<String, Double>();

  // List of each document and its respective word vector
  public static List<List<Double>> listOfMapsTest = new ArrayList<List<Double>>();
  public static List<List<Double>> listOfMapsTrain = new ArrayList<List<Double>>();

  // List of cosine similarity values
  public static ArrayList<Double> finalList = new ArrayList<Double>();
  // Arraylist of predictions 0 = nonspam 1 = spam
  public static ArrayList<Integer> accuracy = new ArrayList<Integer>();

  // Counts number of spam emails
  public static int spamCountTrain = 0;
  public static int spamCountTest = 0;
  public static int emailCountTest = 0;
  public static int emailCountTrain = 0;
  public static int testCount = 0;
  public static int trainCount = 0;

  public void kNNaccuracy(String filelocation, int k) throws IOException {

    // All files in testing and training data in the form of strings
    train = read(filelocation + "/train");
    test = read(filelocation + "/test");

    emailCountTrain = trainCount - spamCountTrain;
    emailCountTest = testCount - spamCountTest;

    // Creates the hashmap WORDS from the training data
    for (int i = 0; i < train.size(); i++) {

      String str = train.get(i);
      String[] strings = str.split(" ");
      count(strings);

    }

    // Creates list of doubles for training data
    for (int i = 0; i < train.size(); i++) {

      String str = train.get(i);// Retrieves first document
      String[] strings = str.split(" ");// Splits document into array of words
      // Adds list which contains word counts for each document
      listOfMapsTrain.add(countOccurence(strings));

    }

    // Creates list of doubles for testing data
    for (int i = 0; i < test.size(); i++) {

      String str = test.get(i);
      String[] strings = str.split(" ");// Splits string into array of words
      // Adds list which contains word counts for each document
      listOfMapsTest.add(countOccurence(strings));
    }

    // Gets cosine similarity and accuracy for each K value
    cosineSimilarity(1);
  	System.out.print("When k=1, accuracy: ");
  	getAccuracy();
  	cosineSimilarity(3);
  	System.out.print("When k=3, accuracy: ");
  	getAccuracy();
  	cosineSimilarity(5);
  	System.out.print("When k=5, accuracy: ");
  	getAccuracy();
  	cosineSimilarity(19);
  	System.out.print("When k=19, accuracy: ");
  	getAccuracy();


    cosineSimilarity(k);
    System.out.print("When k=" + k + ", accuracy: ");
    getAccuracy();
  }
  
  // Gets accuracy for KNN
  public static void getAccuracy() {

    int correct = 0;// Counts total number of correct predictions
    int counter = 0; // counting each test file index
    final File testFolder = new File("./test");
    for (final File testFile : testFolder.listFiles()) {

      // check if it's a regular file
      if (accuracy.get(counter) == 0 && !testFile.getName().startsWith("sp")) {
        correct++;

      }
      // check if it's a spam file
      if (accuracy.get(counter) == 1 && testFile.getName().startsWith("sp")) {
        correct++;
      }
      counter++;
    }

    double acc = correct / (double) (accuracy.size());// Calculates accuracy
    System.out.println(acc * 100 + "%");// Prints result
    accuracy = new ArrayList<Integer>();// Resets array of true and false

  }

  // Calculates cosine similarity
  // Creates finalList, a list of all cosine similarity values
  // Calls getLength and dotProd for calculation
  // Calls getKNN to find the K nearest neighbors
  public static void cosineSimilarity(int k) {

    // Cycles through each test record and compares with each training record
    for (int i = 0; i < listOfMapsTest.size(); i++) {
      for (int j = 0; j < listOfMapsTrain.size(); j++) {

        // Obtains each test term vector
        List<Double> x = listOfMapsTest.get(i);

        // Obtains each train term vector
        List<Double> y = listOfMapsTrain.get(j);

        // Calculate the cosine similarity
        double cosSim = (dotProd(x, y) / getLength(x, y));
        finalList.add(cosSim);// Adds each cosine value to list

      }
      // Gets K nearest neighbors, by finding greatest values in finalList
      getKNN(k);
      finalList = new ArrayList<Double>();// Resets list

    }

  }

  
  public static void getKNN(int k) {

    // Creates clone of final list
    List<Double> clone = new ArrayList<Double>(finalList);
    // Will hold the indices of the nearest neighbors
    List<Integer> indexes = new ArrayList<Integer>();

    // Assembles list of indexes for closest neighbors
    for (int i = 0; i < k; i++) {
      double temp = Collections.max(clone);// Gets max from clone of finalList
      int index = finalList.indexOf(temp);// Gets index of max from finalList
      indexes.add(index);// Adds to list of indexes
      clone.set(index, 0.0);// Changes top value to 0 to find the next neighbor

    }

    // Determines if spam or not spam
    int spamCount = 0;// Counts number of spam
    for (int i = 0; i < indexes.size(); i++) {

      int x = indexes.get(i);// Gets index.
      if (x <= spamCountTrain) { // Not Spam
      } else {// Spam
        spamCount++;
      }
    }
    int emailCount = k - spamCount;// Total number of non-spam

    // If spam we insert a 1 for that test record. If email we insert a 0
    if (spamCount >= emailCount)
      accuracy.add(1);
    else
      accuracy.add(0);

  }

  // Reads folders for training or testing data.
  // Gets total number of spam and non-spam emails
  // Returns an arrayList of every email in the form of a string.
  public static ArrayList<String> read(String path) throws IOException {

    ArrayList<String> temp = new ArrayList<String>();
    final File folder = new File(path);// Gets folder

    for (final File fileEntry : folder.listFiles()) {
      @SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(fileEntry));

      // If it is training data and if the name contains spam
      if (path.contains("train") && fileEntry.getName().contains("spm")) {

        spamCountTrain++;// Increment spam count for training data

      }

      // If it is testing data and if the file name contains spam
      if (path.contains("test") && fileEntry.getName().contains("spm")) {

        spamCountTest++;

      }

      // Gets total number of testing emails
      if (path.contains("test")) {

        testCount++;

      }
      // Gets total number of training emails
      if (path.contains("train")) {

        trainCount++;

      }

      // Converts each email to string
      String s = "";
      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        s += sCurrentLine;
      }
      temp.add(s);// Adds each string to arrayList
    }
    return temp;// returns every email in the form of a string
  }

  // DOT PRODUCT
  public static double dotProd(List<Double> a, List<Double> b) {
    double sum = 0;// Total dotProd
    for (int i = 0; i < a.size(); i++) {
      // Multiplies each element in Vector by one another
      sum += a.get(i) * b.get(i);
    }
    return sum;
  }

  
  public static double getLength(List<Double> a, List<Double> b) {

    // Length from testing set
    double sum1 = 0.0;
    for (int i = 0; i < a.size(); i++) {
      sum1 += a.get(i) * a.get(i);
    }

    sum1 = Math.pow(sum1, .5);// Sum Raised to .5 power

    // Length from training set
    double sum2 = 0.0;
    for (int i = 0; i < b.size(); i++) {
      sum2 += b.get(i) * b.get(i);
    }

    sum2 = Math.pow(sum2, .5);// Sum Raised to .5 power

    double sum3 = sum1 * sum2;// Multiply sums

    return sum3;// returns denominator

  }

  // Makes every entry in HashMap 0
  public static Map<String, Double> makeZero(Map<String, Double> wordClone) {

    for (Map.Entry<String, Double> entry : wordClone.entrySet()) {
      wordClone.put(entry.getKey(), 0.0);// Changes double to 0
    }

    return wordClone;

  }

  // Creates array list with all words and their counts from training data
  // Creates array list with all words and their counts from training data
  private static void count(String[] arr) {

    for (int i = 0; i < arr.length; i++) {

      if (!words.containsKey(arr[i])) {

        words.put(arr[i], 1.0);// Adds new word to list

      } else if (words.containsKey(arr[i])) {
        // Updates count of already added word
        words.put(arr[i], words.get(arr[i]) + 1);

      }

    }

  }

  // Counts every word from string array. Returns array list of doubles.
  // Used to create ArrayLists of word counts for each test and training record
  private static ArrayList<Double> countOccurence(String[] arr) {
    // Clones hashmap of all words
    Map<String, Double> wordClone = new HashMap<String, Double>(words);
    wordClone = makeZero(wordClone);// Makes all double values zero

    for (int i = 0; i < arr.length; i++) {

      if (words.containsKey(arr[i])) {
        // Counts up word occurrences per document
        wordClone.put(arr[i], wordClone.get(arr[i]) + 1);

      }

    }
    // Returns list of only doubles. Doubles represent word occurrence
    ArrayList<Double> values = new ArrayList<Double>(wordClone.values());
    return values;
  }

}
