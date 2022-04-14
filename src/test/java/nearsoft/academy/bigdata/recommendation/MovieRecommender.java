package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;

public class MovieRecommender {


    long reviews = 0;

    MovieRecommender(String path) throws IOException {
        File rawData = new File(path);
        LineIterator it = FileUtils.lineIterator(rawData, "UTF-8");
        try {

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
                    System.out.println(product);
                }

                if (line.contains(sUser)) {
                    user = line.replace(sUser, "");
                    System.out.println(user);
                }
                if (line.contains(sScore)) {
                    score = line.replace(sScore, "");
                    System.out.println(score);
                    reviews++;
                }


                if(reviews==5) break;
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    public long getTotalReviews(){
        return reviews;
    }


}
