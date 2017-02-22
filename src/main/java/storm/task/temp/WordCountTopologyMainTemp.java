package storm.task.temp;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import storm.task.temp.bolt.localfile.WordCounter;
import storm.task.temp.bolt.localfile.WordNormalizer;
import storm.task.temp.spout.WordReader;


/**
 * Created by yonghongli on 2016/7/19.
 */
public class WordCountTopologyMainTemp {
    public static void main(String[] args) throws InterruptedException {
        //����һ��Topology
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReader());
        builder.setBolt("word-normalizer", new WordNormalizer())
                .shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounter(), 2)
                .fieldsGrouping("word-normalizer", new Fields("word"));
        //����
        Config conf = new Config();
        conf.put("wordsFile", "d:/text.txt");
        conf.setDebug(false);
        //�ύTopology
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        //����һ������ģʽcluster
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Getting-Started-Toplogie", conf,
                builder.createTopology());
        Thread.sleep(1000);
        cluster.shutdown();
    }
}