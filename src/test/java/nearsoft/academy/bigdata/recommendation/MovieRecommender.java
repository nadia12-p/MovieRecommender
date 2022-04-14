package nearsoft.academy.bigdata.recommendation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;

public class MovieRecommender {
    MovieRecommender(String path) throws IOException {
        File rawData = new File(path);
        LineIterator it = FileUtils.lineIterator(rawData, "UTF-8");
        try {
            int freno = 0;
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
                System.out.println(line);
                freno++;
                if(freno==20) break;
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
