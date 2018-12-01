import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class NaiveFilter {

  //train: all training regular email data
  //trainSpam: all training spam email data
  //words: all distinct training regular email data
  //wordsSpam: all distinct training spam email data
  public static ArrayList<String> train = new ArrayList<String>();
  public static ArrayList<String> trainSpam = new ArrayList<String>();
  public static Map<String, Integer> words = new HashMap<String, Integer>();
  public static Map<String, Integer> wordsSpam = new HashMap<String, Integer>();

  public String accuracy() throws IOException {

    // getting train files
    final File trainFolder = new File("./train");
    for (final File trainFile : trainFolder.listFiles()) {
      @SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(trainFile));
      String sCurrentLine, s = "";
      while ((sCurrentLine = br.readLine()) != null)
        s += sCurrentLine;// s = each file content

      if (trainFile.getName().startsWith("sp"))
        trainSpam.add(s); // add all spam email into trainSpam String ArrayList
      else
        train.add(s); // add all normal email into train String ArrayList
    }

    int dWords = count(train, words);// distinct words in regular email
    int dSpamWords = count(trainSpam, wordsSpam);// distinct words in spam

    // calculate total class possibility
    double probEmail = (double) dWords / (dWords + dSpamWords);
    double probSpam = 1 - probEmail;
    double prob = 0, probS = 0;
    double regularP, spamP;
    double totalTestFile = 0;
    double isCorrect = 0;

    // getting test files
    final File testFolder = new File("./test");
    for (final File testFile : testFolder.listFiles()) {
      @SuppressWarnings("resource")
	BufferedReader br2 = new BufferedReader(new FileReader(testFile));
      Map<String, Integer> testWords = new HashMap<String, Integer>();

      double probSum = 0, probSpamSum = 0;
      String sCurrentLine, s = "";
      while ((sCurrentLine = br2.readLine()) != null)
        s += sCurrentLine;// test = each test file content

      String[] arr = s.split(" ");// split each word in a string array
      for (int i = 0; i < arr.length; i++)
        testWords.put(arr[i], i); // remove duplicate words

      // loop through each word in test files
      for (Map.Entry<String, Integer> eTest : testWords.entrySet()) {
        double v1 = 0; // word count value in regular email
        double v2 = 0; // word count value in spam email

        // get number of times the word appears in normal email and spam email
        if (words.containsKey(eTest.getKey()))
          if (words.get(eTest.getKey()) > 50)// filter word frequency < 50 times
            v1 = words.get(eTest.getKey());

        if (wordsSpam.containsKey(eTest.getKey()))
          if (wordsSpam.get(eTest.getKey()) > 50)// filter frequency < 50 times
            v2 = wordsSpam.get(eTest.getKey());

        // probability of each word appears in normal email
        prob = (double) (v1 + 1) / (dWords + 3); // m estimate
        probSum += Math.log(prob);// sum of log

        // probability of each word appears in spam email
        probS = (double) (v2 + 1) / (dSpamWords + 3); // m estimate
        probSpamSum += Math.log(probS);// sum of log
      }

      regularP = Math.log(probEmail) + probSum;// total positive possibility
      spamP = Math.log(probSpam) + probSpamSum;// total spam possibility
      if (regularP < spamP && testFile.getName().startsWith("sp"))
        isCorrect++;
      else if (regularP > spamP && !testFile.getName().startsWith("sp"))
        isCorrect++;

      totalTestFile++;
    }// end for

    // return accuracy
    double acc = (double) (isCorrect * 100 / totalTestFile);
    return acc + "%";
  }// end accuracy method

  public static int count(ArrayList<String> row, Map<String, Integer> words) {
    int totalDistinct = 0;
    for (int j = 0; j < row.size(); j++) {
      String str = row.get(j);
      String[] arr = str.split(" ");// split each word in a string array

      for (int i = 0; i < arr.length; i++) {
        if (Pattern.matches("[a-zA-Z]+", arr[i])) {// filter out symbols
          if (!words.containsKey(arr[i])) // if new word, set value to 1
            words.put(arr[i], 1);
          else if (words.containsKey(arr[i])) // if old word, add 1 to value
            words.put(arr[i], words.get(arr[i]) + 1);
        }
      }
      totalDistinct += arr.length;
    }
    return totalDistinct;
  }// end count method
}
