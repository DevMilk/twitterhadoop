package mostReplies;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TReducer extends Reducer<Text, IntWritable, IntWritable, Text> {

    private TreeMap<Integer, String> treeMap;

    @Override
    protected void setup(Context context)  {
        treeMap = new TreeMap<>();
    }

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

        String user = key.toString();
        int sum = 0;
        for (IntWritable value : values) {
            sum = sum + value.get();
        }

        final int topNumber = context.getConfiguration().getInt("top.number", 0);

        if (topNumber > 0) {
            treeMap.put(sum, user);
            if (treeMap.size() > topNumber)
                treeMap.remove(treeMap.firstKey());
        }
        else
            context.write(new IntWritable(sum), key);
    }

    @Override
    public void cleanup(Context context) throws IOException,
            InterruptedException
    {
        final int topNumber = context.getConfiguration().getInt("top.number", 0);
        if (topNumber > 0) {
            for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
                Integer replies = entry.getKey();
                String user = entry.getValue();
                context.write(new IntWritable(replies), new Text(user));
            }
        }
    }
}