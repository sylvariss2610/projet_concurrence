package PageIndexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

class SearchRunnable implements Runnable {

    private ThreadPoolExecutor pool;
    private Index index;
    private URL url;
    private String word;
    private Boolean found;

    SearchRunnable(ThreadPoolExecutor pool, Index index, URL url, String word) {
        this.pool = pool;
        this.index = index;
        this.url = url;
        this.word = word;
        this.found = false;
    }

    public void run() {
        //TODO :
        // (Ok) verify the page -> index.verify(url)
        // (Ok) -> if not already verified -> add url in index -> index.write(url)
        // (Ok) get the page
        // (Ok) extract the text -> HTTP/RegExp
        // (Ok) verify all words in all lines -> ForEach, lines in page and words in lines
        // (Ok) print url if equals to wanted word
        // ()begin search in related pages (links)
        if (!this.index.verify(this.url)) {
            try {
                this.index.write(this.url);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(this.url.openStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    for (String str : inputLine.split("\\s")) {
                        if (str.equals(this.word)) {
                            if (!this.found)
                                System.out.println("The word has been caught at " + this.url);
                            this.found = true;
                        }
                    }
                    if (this.found) {
                        try {
                            //System.out.println(this.url.toString());
                            Document doc = Jsoup.connect(this.url.toString()).get();
                            if(doc.hasText())
                            {
                                Elements elements = doc.select("a");
                                //System.out.println(elements);
                                //System.out.println(elements.size());
                                for (Element element : elements) {
                                    if(element.hasAttr("href"))
                                    {
                                        //System.out.println("url : "+element.attr("href"));
                                        if (!element.attr("href").startsWith("#")) {
                                            //System.out.println("url : "+element.attr("href"));
                                            if (element.attr("href").startsWith("/")) {
                                                if (this.url.toString().contains("wikipedia.org")) {
                                                    //System.out.println();
                                                    this.pool.execute(new SearchRunnable(this.pool,this.index,new URL(("https://fr.wikipedia.org" + element.attr("href"))),this.word));
                                                }
                                            } else {
                                                //System.out.println("url : "+element.attr("href"));
                                                this.pool.execute(new SearchRunnable(this.pool,this.index,new URL((element.attr("href"))),this.word));
                                            }

                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
