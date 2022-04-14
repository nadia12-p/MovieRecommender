package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;

public class MovieRecommender {
    MovieRecommender(String path){
        DataModel model = new FileDataModel(new File("/path/to/dataset.csv"));
    }
}
