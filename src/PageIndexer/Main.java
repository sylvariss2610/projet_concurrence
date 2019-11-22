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
 * arg[0] : mot à chercher
 * arg[1] : hyperlien (racine)
 * arg[2] : nombre de thread maximal
 */
public class Main {
    public static void main(String[] args)
    {
        Index index = new Index();
        if (args.length == 3) {
            String toSearch = args[0];
            try {
                int nbeMaxThreads = Integer.parseInt(args[2]);
                URL url = new URL(args[1]);
                ThreadPoolExecutor Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nbeMaxThreads);
                Executor.execute(new SearchRunnable(Executor, index, url, toSearch));
            } catch (MalformedURLException e) {
            	System.out.println("Cet URL n'a pas un format valide");
            } catch (NumberFormatException e) {
                System.out.println("La chaîne de caractère n'a pas le bon format pour être transformé en nombre");
            }
        } else {
        	System.out.println("Nombre d'arguments en entrée inexacte");
        }
    }
}
