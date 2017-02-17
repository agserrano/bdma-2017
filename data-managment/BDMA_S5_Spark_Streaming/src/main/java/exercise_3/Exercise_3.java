package exercise_3;

import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import twitter4j.Status;
import exercise_3.LanguageDetector;
import exercise_3.StopWords;
import exercise_3.PositiveWords;
import exercise_3.NegativeWords;
import scala.Tuple2;
import scala.Tuple4;
import scala.Tuple5;

public class Exercise_3 {

	public static void sentimentAnalysis(JavaDStream<Status> statuses) {
		// 4.3.1
		JavaDStream<Status> englishTweets = statuses.
				filter(x -> LanguageDetector.isEnglish(x.getText())).
				filter(x -> x.getText().trim().length() > 0)
				;
		
		// 4.3.2
		JavaPairDStream<Long, String> tweetsCleaned = englishTweets.
				mapToPair(x -> new Tuple2<Long, String>(x.getId(), x.getText().
						replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase()));
		
		JavaPairDStream<Long, String> tweetsStemmed = tweetsCleaned.mapToPair(
			new PairFunction <Tuple2<Long, String>, Long, String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple2<Long, String> call(Tuple2<Long, String> t) throws Exception {
					String stemText = t._2;
					
					List<String> stopWords = StopWords.getWords();
			        for (String word : stopWords) {
			        	stemText = stemText.replaceAll("\\b" + word + "\\b", "");
			        }
			        
					return new Tuple2<Long, String>(t._1, stemText);
				}
				
			});
		//tweetsStemmed.print();
		
		// 4.3.3
		// Get the Stream with the positive score

		JavaPairDStream<Tuple2<Long, String>, Float> tweetsPositive = tweetsStemmed.mapToPair(
			new PairFunction <Tuple2<Long, String>, Tuple2<Long, String>, Float>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple2<Tuple2<Long, String>, Float> call(Tuple2<Long, String> t) throws Exception {
					String[] words = t._2.split(" ");
			        Set<String> positives = PositiveWords.getWords();
					Integer positive_words = 0;
			        for (String word : words) {
			            if (positives.contains(word))
			            	positive_words++;
			        }			        
					//return new Tuple2<Long, Float>(t._1, (float)negative_words/words.length);
                    return new Tuple2<Tuple2<Long, String>, Float>(
                            new Tuple2<Long, String>(t._1(), t._2()),
                            (float) positive_words / words.length
                    );
				}
				
			}).filter(x -> x._2 > 0);
		
		// Get the Stream with the negative score
		JavaPairDStream<Tuple2<Long, String>, Float> tweetsNegative = tweetsStemmed.mapToPair(
			new PairFunction <Tuple2<Long, String>, Tuple2<Long, String>, Float>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Tuple2<Tuple2<Long, String>, Float> call(Tuple2<Long, String> t) throws Exception {
					String[] words = t._2.split(" ");
			        Set<String> negative = NegativeWords.getWords();
					Integer negative_words = 0;
			        for (String word : words) {
			            if (negative.contains(word))
			            	negative_words++;
			        }			        
					//return new Tuple2<Long, Float>(t._1, (float)negative_words/words.length);
                    return new Tuple2<Tuple2<Long, String>, Float>(
                            new Tuple2<Long, String>(t._1(), t._2()),
                            (float) negative_words / words.length
                    );
				}
				
			}).filter(x -> x._2 > 0);
			
		// tweetsPositive.print();
		
		// 4.3.4
        JavaPairDStream<Tuple2<Long, String>, Tuple2<Float, Float>> joined =
        		tweetsPositive.join(tweetsNegative);
		
        JavaDStream<Tuple4<Long, String, Float, Float>> scoredTweets = joined.map(
            	new Function<Tuple2<Tuple2<Long, String>, Tuple2<Float, Float>>, Tuple4<Long, String, Float, Float>>() {
					private static final long serialVersionUID = 1L;

					@Override
                    public Tuple4<Long, String, Float, Float> call(Tuple2<Tuple2<Long, String>, Tuple2<Float, Float>> t) {
                        return new Tuple4<Long, String, Float, Float>(
                            t._1()._1(), t._1()._2(),
                            t._2()._1(), t._2()._2());
                    }
            	});
        
        JavaDStream<Tuple5<Long, String, Float, Float, String>> final_res = scoredTweets.map(
        		new Function<Tuple4<Long, String, Float, Float>, Tuple5<Long, String, Float, Float, String>>() {
        			private static final long serialVersionUID = 1L;
        			public Tuple5<Long, String, Float, Float, String> call(Tuple4<Long, String, Float, Float> t) {
        				String result = "neutral";
        				if (t._3() < t._4()) {
        					result = "negative";
        				} else if (t._3() > t._4()) {
        					result = "positive";
        				}
        				return new Tuple5<Long, String, Float, Float, String> (
        						t._1(), t._2(), t._3(), t._4(), result
        					);
        			}
        		}
        	);
        final_res.print();
	}
}
	