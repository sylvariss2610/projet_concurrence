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
        if (!this.index.Verification(this.url)) {
            try {
                this.index.Ecriture(this.url);
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
                            //Utilisation de Jsoup qui est une librairie Java qui permet de "parse" du HTML
                            Document doc = Jsoup.connect(this.url.toString()).get();
                            if(doc.hasText())
                            {
                                Elements elements = doc.select("a");
                                for (Element element : elements) {
                                    if(element.hasAttr("href"))
                                    {
                                        if (!element.attr("href").startsWith("#")) {
                                            if (element.attr("href").startsWith("/")) {
                                                if (this.url.toString().contains("wikipedia.org")) {
                                                    this.pool.execute(new SearchRunnable(this.pool,this.index,new URL(("https://fr.wikipedia.org" + element.attr("href"))),this.word));
                                                }
                                            } else {
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
