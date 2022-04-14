package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieRecommender {


    long reviews = 0;

    HashMap<String, Long>  usersEncoded = new HashMap<String, Long>();
    HashMap<Long, String> usersDecoded = new HashMap<Long, String>();

    HashMap<String, Long> productsEncoded = new HashMap<String, Long>();
    HashMap<Long, String> productsDecoded = new HashMap<Long, String>();

    DataModel model;
    UserSimilarity similarity;
    UserNeighborhood neighborhood;
    UserBasedRecommender recommender;

    MovieRecommender(String path) throws IOException, TasteException {
        prepareData(path);
        model = new FileDataModel(new File("MahoutInput.csv"));
        similarity = new PearsonCorrelationSimilarity(model);
        neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
    }

    //formats the input data for the Mahout model
    private void prepareData(String path) throws IOException {
        File rawData = new File(path);
        LineIterator it = FileUtils.lineIterator(rawData, "UTF-8");
        try {

            PrintWriter writer = new PrintWriter("MahoutInput.csv");
            String product = "";
            String user = "";
            String score = "";

            while (it.hasNext()) {
                String line = it.nextLine();
                //only looks for the lines that we need for the Mahout model
                String sProduct = "product/productId: ";
                String sUser = "review/userId: ";
                String sScore = "review/score: ";

                if (line.contains(sProduct)) {
                    product = line.replace(sProduct, "");
                }

                if (line.contains(sUser)) {
                    user = line.replace(sUser, "");
                }

                if (line.contains(sScore)) {
                    score = line.replace(sScore, "");
                    //stores the Mahout data in a csv file
                    String newLine = storingUsers(user) + "," + storingProducts(product) + "," + score + "\n";
                    writer.write(newLine);
                    reviews++;
                }

            }
            writer.close();

        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    public long getTotalReviews(){
        return reviews;
    }

    public int getTotalUsers() {return usersEncoded.size();}

    public int getTotalProducts() {return productsEncoded.size();}

    //saves the data for the Mahout model and to recover it for later
    private long storingUsers(String user){
        if (!usersEncoded.containsKey(user)) {
            long userMahout = usersEncoded.size()+1;
            usersEncoded.put(user, userMahout);
            usersDecoded.put(userMahout, user);
        }
        return usersEncoded.get(user);
    }

    private long storingProducts(String product){
        if (!productsEncoded.containsKey(product)) {
            long productMahout = productsEncoded.size();
            productsEncoded.put(product, productMahout);
            productsDecoded.put(productMahout, product);
        }
        return productsEncoded.get(product);
    }

    //Mahout model gets the recommendation for the user
    public List<String> getRecommendationsForUser(String user) throws TasteException {
        List<String> userRecommendations = new ArrayList<String>();
        List<RecommendedItem> recommendations = recommender.recommend(usersEncoded.get(user), 3);
        for (RecommendedItem recommendation : recommendations) {
            long mahoutProduct = recommendation.getItemID();
            userRecommendations.add(productsDecoded.get(mahoutProduct));
        }

        return userRecommendations;
    }
}
