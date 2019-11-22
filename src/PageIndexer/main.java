package PageIndexer;

import PageIndexer.Index;
import PageIndexer.SearchRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classe principale
 * argument 1 : mot à chercher
 * argument 2 : hyperlien (racine)
 * argument 3 : nombre de thread maximal
 */
public class main {
    public static void main(String[] args)// throws IOException
    {
        Index index = new Index();
        if (args.length != 3) {
            System.out.println("Error : invalid arguments number or arguments type");
        } else {
            String toSearch = args[0];
            try {
                int maxThreadNumber = Integer.parseInt(args[2]);
                URL url = new URL(args[1]);
                ThreadPoolExecutor TPE = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreadNumber);
                TPE.execute(new SearchRunnable(TPE, index, url, toSearch));
            } catch (NumberFormatException e) {
                System.out.println("Invalid max thread number format");
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL");
            }

        }
    }
        /*try {
            URL oracle = new URL("https://fr.wikipedia.org/wiki/Nantes");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
