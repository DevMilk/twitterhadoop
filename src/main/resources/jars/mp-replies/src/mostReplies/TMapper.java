package mostReplies;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class TMapper extends Mapper<Object, Text, Text, IntWritable> {

    private TreeMap<Integer, String> treeMap;

    @Override
    protected void setup(Context context) {
        treeMap = new TreeMap<>();
    }

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Format per tweet is id;user;fullname;url;timestamp;replies;likes;retweets;text
        String tweets = value.toString();
        final int topNumber = context.getConfiguration().getInt("top.number", 0);
        if (StringUtils.ordinalIndexOf(tweets,";",8)>-1)
        {
            // index starts from 1
            int userStartIndex = StringUtils.ordinalIndexOf(tweets,";",1) + 1;
            int userFinishIndex = StringUtils.ordinalIndexOf(tweets, ";", 2);
            int repliesStartIndex = StringUtils.ordinalIndexOf(tweets, ";", 5) + 1;
            int repliesFinishIndex = StringUtils.ordinalIndexOf(tweets, ";", 6);

            String user = tweets.substring(userStartIndex, userFinishIndex);
            String replies = tweets.substring(repliesStartIndex, repliesFinishIndex);

            int repliesNum;
            try {
                repliesNum = new Integer(replies);
            } catch (NumberFormatException e) { // for the first line
                repliesNum = -1;
            }

            if (topNumber > 0) {
                treeMap.put(repliesNum, user);
                if (treeMap.size() > topNumber)
                    treeMap.remove(treeMap.firstKey());
            }
            else
                context.write(new Text(user), new IntWritable(repliesNum));
        }
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
                context.write(new Text(user), new IntWritable(replies));
            }
        }
    }

}



