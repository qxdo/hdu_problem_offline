import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class Main{

    public static void main(String[] args) throws Exception {
        //题目标号的范围
        Integer num = 1099;
        Integer MaxNum = 6543;
        for(int i=num;i<= MaxNum;i++){
            StringBuffer sb = null;
            try {
                sb = generateMarkdownContent(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String filePath = "D:\\tmp\\HDU\\HDU-"+i+".md";
            toFile(sb,filePath);

        }
    }


    public static void toFile(StringBuffer sb,String filePath) throws Exception {

        File markdownFile = new File(filePath);
        if(markdownFile.exists()){
            if(!markdownFile.delete()){
                System.err.println(filePath +"失败...");
            }
        }
        if(markdownFile.createNewFile()){
            FileWriter fw=new FileWriter(filePath);
            fw.write(sb.toString());
            fw.close();
        }else{
            System.err.println(filePath+ "失败...");
        }
        System.out.println(filePath + "成功...");
    }

    public static StringBuffer generateMarkdownContent(Integer num) throws Exception {
        String title = null;
        String limits = null;
        StringBuffer sb = new StringBuffer();
        Document document =  Jsoup.connect("http://acm.hdu.edu.cn/showproblem.php?pid=" + num)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) '\n" +
                         "    'Chrome/51.0.2704.63 Safari/537.36")
                .get();


        Elements elementList= document.getElementsByTag("h1");

        /**
         * 每个问题的标题
         */
        if(elementList.size() == 1){
            title =  elementList.get(0).html();
        }
        sb.append("## "+title + " " + num + "\n");
        /**
         * Limits
         *
         */
        Elements limites =  document.getElementsByTag("span");
        limits = limites.get(0).html();
        sb.append( limits + "\n");

        /**
         *
         */
        Elements elements = document.getElementsByClass("panel_title");
        Elements elements1 = document.getElementsByClass("panel_content");

        if( elements.size() ==  elements1.size() ){
            for(int i=0;i<elements.size();i++){

                //System.out.println("=====================");
                sb.append( "## " + elements.get(i).html() + "\n");
                //System.out.println("====================");
                Element contentElement =  elements1.get(i);
                if(contentElement.select("img[src$=.gif]") != null){
                    //说明这个tag中有img标签
                    Elements images = contentElement.select("img[src$=.gif]");
                    for(Element e : images){
                        String tmp = e.attr("src");
                        String imgPath = "http://acm.hdu.edu.cn" + tmp;
                        //System.out.println(imgPath);


                        //String localPath =  downloadPicture(imgPath, tmp.substring(tmp.lastIndexOf("/"),tmp.length()));
                        e.attr("src",imgPath);
                    }
                    //tag处理完毕
                }
                sb.append(contentElement.html() + "\n");
            }
        }
        return sb;
    }

//有考虑过离线图片，后来觉得并不是多现实，而且图片下载到的话会很混乱，所以就改成了链接的方式。
    //链接url下载图片
    private static String downloadPicture(String urlList,String path) throws Exception {
        URL url = null;

            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File file =  new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();

        return file.getAbsolutePath();
    }


}
