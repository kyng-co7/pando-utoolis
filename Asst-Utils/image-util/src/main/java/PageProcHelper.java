import com.gargoylesoftware.htmlunit.html.DomElement;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageProcHelper {

    public static ArrayList<String> getDataSrcUrl(List<DomElement> elementList){

        /**extracting "data-src" url content only**/
        List<String> dsValUrls = new ArrayList<>();
        String dsVal;

        for(int nodeItr = 0; nodeItr < elementList.size(); nodeItr ++){

            if(elementList.get(nodeItr).toString().contains("data-src")){
                dsVal = elementList.get(nodeItr).toString().substring(
                        elementList.get(nodeItr).toString().indexOf("data-src") + 10);

                dsValUrls.add(dsVal.substring(0, StringUtils.ordinalIndexOf(dsVal, "\"", 1)));
            }
        }

        return (ArrayList<String>) dsValUrls;
    }

    /**Retrieving content type of image from data-url**/
    public static String getContentTypeFromDataSrcUrl(String dataSrcUrl) throws IOException {
        URL imgUrl = new URL(dataSrcUrl);
        URLConnection urlConn = imgUrl.openConnection();
        String contentType = urlConn.getContentType();

        String suffix = null;
        Iterator<ImageReader> readers =
                ImageIO.getImageReadersByMIMEType(contentType);
        while (suffix == null && readers.hasNext()) {
            ImageReaderSpi provider = readers.next().getOriginatingProvider();
            if (provider != null) {
                String[] suffixes = provider.getFileSuffixes();
                if (suffixes != null) {
                    suffix = suffixes[0];
                }
            }
        }

        return suffix;
    }//
}
