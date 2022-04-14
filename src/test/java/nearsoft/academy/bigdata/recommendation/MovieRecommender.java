package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MovieRecommender {


    long reviews = 0;

    HashMap<String, Long>  usersEncoded = new HashMap<String, Long>();
    HashMap<String, Long> productsEncoded = new HashMap<String, Long>();

    MovieRecommender(String path) throws IOException {
        File rawData = new File(path);
        LineIterator it = FileUtils.lineIterator(rawData, "UTF-8");
        try {

            PrintWriter writer = new PrintWriter("MahoutInput.csv");
            String product = "";
            String user = "";
            String score = "";

            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
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
                    String newLine = storingUsers(user) + "," + storingProducts(product) + "," + score + "\n";
                    writer.write(newLine);
                    //System.out.println(newLine);
                    reviews++;
                }


                //if(reviews==10) break;

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

    private long storingUsers(String user){
        if (!usersEncoded.containsKey(user)) {
            usersEncoded.put(user, (long) (usersEncoded.size()+1));
        }
        return usersEncoded.get(user);
    }

    private long storingProducts(String product){
        if (!productsEncoded.containsKey(product)) {
            productsEncoded.put(product, (long) (productsEncoded.size()));
        }
        return productsEncoded.get(product);
    }


}
