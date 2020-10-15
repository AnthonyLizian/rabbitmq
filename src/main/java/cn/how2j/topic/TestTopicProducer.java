package cn.how2j.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import cn.how2j.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生成者
 */
public class    TestTopicProducer {
    //交换机名：topics_exchange
    public final static String EXCHANGE_NAME="topics_exchange";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //路由键
        String[] routing_keys = new String[] { "usa.news", "usa.weather",
                "europe.news", "europe.weather" };
        //消息
        String[] messages = new String[] { "美国新闻", "美国天气",
                "欧洲新闻", "欧洲天气" };

        for (int i = 0; i < routing_keys.length; i++) {
            String routingKey = routing_keys[i];
            String message = messages[i];
            //发送消息到队列中（参数为：队列名称；路由键；消息）
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message
                    .getBytes());
            System.out.printf("发送消息到路由：%s, 内容是: %s%n ", routingKey,message);
        }

        //关闭通道和连接
        channel.close();
        connection.close();
    }
}