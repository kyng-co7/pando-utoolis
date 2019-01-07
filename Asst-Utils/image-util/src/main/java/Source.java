
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Source {

    private static JFInputDialog urlInputDialog;
    private static boolean isDownloaded;
    private static HtmlPage htmlPage;
    private static List<DomElement> imgNodeList;
    private static int processCounter = 0;
    private static String imagesLoc = "";

    public static void main(String[] args) {

        //StringBuilder urlBldr = new StringBuilder();

        initImageProcessor();

       /* //Opening initial frame for url input from user and getting response page
        String ipStr = getInputDialog();
        htmlPage = getHttpPage(ipStr);

        //Processing for <img> tags from url html page response
        List<DomElement> imgNodeList =  htmlPage.getElementsByTagName("img");*/
        System.out.println("url: " + htmlPage.getUrl());
        System.out.println("Length:" + imgNodeList.size());

        JTextArea textArea = new JTextArea(50, 100);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);

        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(scrollPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        downloadImagesProcessor(imgNodeList, textArea);

    }
    private static void initImageProcessor(){
        //Opening initial frame for url input from user and getting response page
        String ipStr = getInputDialog();
        htmlPage = getHttpPage(ipStr);

        //prepping images location path for set
        if(!imagesLoc.isEmpty()){
            imagesLoc = "";
        }

        //Processing for <img> tags from url html page response
        imgNodeList =  htmlPage.getElementsByTagName("img");
        processCounter++;
    }
    private static void downloadImagesProcessor(List<DomElement> elementList, JTextArea jTextArea){

        PageProcHelper.getDataSrcUrl(elementList).forEach(dataSrcItm -> {
            //urlBldr.append(item + "\n")
            //System.out.println("url: " + item)
            try {
                writeImageToHDD(dataSrcItm,"Image");
                if(isDownloaded) {
                    jTextArea.append("Downloaded from: " + dataSrcItm + "\n");
                    Thread.sleep(300);

                    if(elementList.get(elementList.size()-1).toString().contains(dataSrcItm)){

                        jTextArea.append(
                                "Images located @ " + imagesLoc + "\n" +
                                "***************************************************************************************" + "\n");
                        int confOpt = JOptionPane.showConfirmDialog
                                (null, "Processing complete, would you like to continue?", "Image Processing", JOptionPane.YES_NO_OPTION);

                        if(confOpt == 0){
                            initImageProcessor();
                            downloadImagesProcessor(elementList, jTextArea);
                        }
                        else{
                            urlInputDialog.closeDialog();
                            return;
                            //end application

                        }
                    }
                }
            } catch (InterruptedException ie){
                //print exception
            } catch (IOException ioe){
                System.out.println("IOE on: " + dataSrcItm);
            }
        });
    }
    private static String getInputDialog(){

        if(urlInputDialog != null){
            urlInputDialog.closeDialog();
        }
        urlInputDialog = new JFInputDialog();

        return JOptionPane.showInputDialog(urlInputDialog,
                "Type/Paste URL",
                "https://");
    }
    private static HtmlPage getHttpPage(String text){

        HtmlPage page = null;

        text = (text == null)? (getInputDialog()) : (text); //checking if value exists
        
        try (final WebClient webClient = new WebClient()) {
             page = webClient.getPage(text);
        }
        catch (MalformedURLException mue){
            //asking user for valid url
            text = getInputDialog();
            getHttpPage(text);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        return page;
    }
    private static void writeImageToHDD(String imgUrl, String imageName) throws IOException{

        URL imageURL = new URL(imgUrl);
        imageName = imageName.concat(StringUtils.right(imgUrl,10) + ".");
        String imageContentType = PageProcHelper.getContentTypeFromDataSrcUrl(imgUrl);
        //System.out.println("image: " + imageName+StringUtils.right(imgUrl,10)+"."+imageContentType);

        //creating image object from data-url & writing image to hdd

        try {
            File imgFile = new File(System.getProperty("user.home") + "\\Downloads\\ImageUtil_dll\\package" + String.format("%02d", processCounter));

            if(imagesLoc.isEmpty()){
                imagesLoc = imgFile.getAbsolutePath();
            }

            if (!imgFile.exists()) {
                imgFile.mkdirs();
            }

            //Read in & write image if existing
            BufferedImage image = ImageIO.read(imageURL);

            if (image != null) {
                ImageIO.write(image, imageContentType, new File(imgFile + "\\" + imageName + imageContentType));
                image.flush();
                isDownloaded = true;
            } else {
                isDownloaded = false;
            }
        } catch (FileNotFoundException fnfe){
            //print exception
        }
    }
}