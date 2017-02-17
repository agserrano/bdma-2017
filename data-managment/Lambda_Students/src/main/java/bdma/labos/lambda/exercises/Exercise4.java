package bdma.labos.lambda.exercises;

import java.util.Map.Entry;
import java.util.Calendar;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class Exercise4 {

	public static void run(String hashtag) throws Exception {
		Configuration config = HBaseConfiguration.create();
		Connection connection = ConnectionFactory.createConnection(config);
		Table table = connection.getTable(TableName.valueOf("lambda"));
		
		Vector<Integer> intervals = new Vector<Integer>();
		Vector<Long> times = new Vector<Long>();

		Get getHastag = new Get(Bytes.toBytes(hashtag));
		Result row = table.get(getHastag);
		
		NavigableMap<byte[], byte[]> tweetsMap = row.getFamilyMap(Bytes.toBytes("tweets"));
	
		for (Entry<byte[], byte[]> tweet : tweetsMap.entrySet()) {
			times.add(Bytes.toLong(tweet.getValue()));
		}
		Collections.sort(times);
		long anterior=0;
		for (long t :times) {
			if (anterior!=0){
				int val = (int)(t-anterior)/1000;
				System.out.println(val);
			}
			anterior=t;
		}
	}
	
}