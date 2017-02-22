package storm.task.temp.bolt.kafka;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import storm.task.util.HbaseConfig;

import java.util.Map;

/**
 * Created by yonghongli on 2016/7/19.
 */
public class ESBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;

    }

    @Override
    public void execute(Tuple input) {
        try {
            String id = input.getStringByField("id");
            String mesg = input.getStringByField("info");
            if (mesg != null) {
                Table table = HbaseConfig.con.getTable(TableName.valueOf("xyz"));
                Put put = new Put(id.getBytes());// һ��PUT����һ�����ݣ���NEWһ��PUT��ʾ�ڶ�������,ÿ��һ��Ψһ��ROWKEY���˴�rowkeyΪput���췽���д����ֵ
                put.addColumn("cf1".getBytes(), "val".getBytes(),
                        mesg.getBytes());// �������ݵĵ�һ��
                table.put(put);
            }

        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File |
            collector.fail(input);                  // Settings | File Templates.
        }
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
