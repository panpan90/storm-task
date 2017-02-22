package storm.task.temp.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class WordReader implements IRichSpout {
    private static final long serialVersionUID = 1L;
    private SpoutOutputCollector collector;
    private FileReader fileReader;
    private boolean completed = false;

    public boolean isDistributed() {
        return false;
    }
    /**
     * ���ǵ�һ�����������������������������һ���Ǵ���Topologyʱ�����ã�
     * �ڶ��������е�Topology���ݣ���������������Spout�����ݷ����bolt
     * **/
    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        try {
            //��ȡ����Topologyʱָ����Ҫ��ȡ���ļ�·��
            this.fileReader = new FileReader(conf.get("wordsFile").toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading file ["
                    + conf.get("wordFile") + "]");
        }
        //��ʼ��������
        this.collector = collector;

    }
    /**
     * ����Spout����Ҫ�ķ��������������Ƕ�ȡ�ı��ļ�����������ÿһ�з����ȥ����bolt��
     * ��������᲻�ϱ����ã�Ϊ�˽�������CPU�����ģ����������ʱ����sleepһ��
     * **/
    @Override
    public void nextTuple() {
        if (completed) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Do nothing
            }
            return;
        }
        String str;
        // Open the reader
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            // Read all lines
            while ((str = reader.readLine()) != null) {
                /**
                 * ����ÿһ�У�Values��һ��ArrayList��ʵ��
                 */
                this.collector.emit(new Values(str), str);
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading tuple", e);
        } finally {
            completed = true;
        }

    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));

    }
    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub

    }
    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

    }
    @Override
    public void ack(Object msgId) {
        System.out.println("OK:" + msgId);
    }
    @Override
    public void fail(Object msgId) {
        System.out.println("FAIL:" + msgId);

    }
    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}