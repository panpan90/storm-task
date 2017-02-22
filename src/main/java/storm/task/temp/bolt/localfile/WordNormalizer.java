package storm.task.temp.bolt.localfile;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yonghongli on 2016/7/19.
 */
public class WordNormalizer implements IRichBolt {
    private OutputCollector collector;
    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }
    /**����bolt������Ҫ�ķ�����ÿ�����յ�һ��tupleʱ���˷����㱻����
     * ������������þ��ǰ��ı��ļ��е�ÿһ���зֳ�һ�������ʣ�������Щ���ʷ����ȥ������һ��bolt����
     * **/
    @Override
    public void execute(Tuple input) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                word = word.toLowerCase();
                // Emit the word
                List a = new ArrayList();
                a.add(input);
                collector.emit(a, new Values(word));
            }
        }
        //ȷ�ϳɹ�����һ��tuple
        collector.ack(input);
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));

    }
    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }
    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}