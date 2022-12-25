package org.example;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void tokenizeAndFind(String bodyText) throws IOException {
        URL tokenizerModel = Main.class.getClassLoader().getResource("en-token.bin");
        URL sentenceDetectionModel = Main.class.getClassLoader().getResource("en-sent.bin");
        URL nameFinderModel = Main.class.getClassLoader().getResource("en-ner-person.bin");


        SentenceModel sentenceModell = new SentenceModel(sentenceDetectionModel);
        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModell);
        String[] sentences = sentenceDetector.sentDetect(bodyText);

        TokenizerModel modelTokenizer = new TokenizerModel(tokenizerModel);
        Tokenizer tokenizer = new TokenizerME(modelTokenizer);
        String[] tokens = tokenizer.tokenize(Arrays.toString(sentences));

        TokenNameFinderModel modelNameFinderr = new TokenNameFinderModel(nameFinderModel);
        NameFinderME model = new NameFinderME(modelNameFinderr);
        Span[] nameSpan = model.find(tokens);

        String[] names = Span.spansToStrings(nameSpan, tokens);

        for (String nameSpan1 : names)
            System.out.println(nameSpan1);
    }
    public static boolean isURL(String URL){
        try {
            URL tempURL = new URL(URL);
            return true;
        }
        catch (MalformedURLException e){
            System.out.println("Given URL is not valid.");
            return false;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Name Finder ony works with URL of the page.");
            System.exit(0);
        }
        else{
            if (!isURL(args[0]))
                System.exit(0);
            try {

                String text = Jsoup.connect(args[0]).get().body().toString().replace("\"", "");
                tokenizeAndFind(text);
            }
            catch (IOException e){
                System.out.println("An unknown error has occured during the process.");
            }
        }
    }
}


