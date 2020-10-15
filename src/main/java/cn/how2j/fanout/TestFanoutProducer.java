package cn.how2j.fanout;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import cn.how2j.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生成者
 */
public class TestFanoutProducer {
    //交换机名：fanout_exchange
    public final static String EXCHANGE_NAME="fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        //判断服务器是否启动
    	RabbitMQUtil.checkServer();
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        factory.setHost("localhost");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //交换机声明（参数为：交换机名称；交换机类型）
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        
        for (int i = 0; i < 100; i++) {
            //消息
            String message = "direct 消息 " +i;
            //发送消息到队列中
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
			System.out.println("发送消息： " + message);
            
		}
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}