import java.io.IOException;

//https://github.com/JunshuaiFeng/EmailSpamChecker

public class DataMining {

  public static void main(String[] args) throws IOException {

    // get Naive Bayes accuracy before filter
    Naive naive = new Naive();
    System.out.println("Naive Bayes algarithm");
    System.out.println("Accuracy before filter: " + naive.accuracy());

    // get Naive Bayes accuracy after filter
    NaiveFilter nf = new NaiveFilter();
    System.out.println("Accuracy after filter: " + nf.accuracy());

    // get kNN accuracy before filter
    KNN knn = new KNN();
    System.out.println("\n\nkNN algarithm");
    System.out.println("Accuracy before filter: ");
    knn.kNNaccuracy();

    // get kNN accuracy after filter
    KNNFilter kf = new KNNFilter();
    System.out.println("\nAccuracy after filter: ");
    kf.kNNaccuracy();

  }// end main

}
