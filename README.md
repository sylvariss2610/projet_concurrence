# projet_concurrence

compile à la base du projet : 
javac -d classes/ -cp lib/jsoup-1.12.1.jar src/pageIndexer/*.java

créer jar dans classes :
jar cmvf ../MANIFEST.MF ../jar/Search.jar PageIndexer/*.class

exécuter le jar à la base du projet (5 correspondant au nombre de threads que j'envoie):
java -classpath lib/jsoup-1.12.1.jar -jar jar/Search.jar Nantes https://fr.wikipedia.org/wiki/Nantes 5