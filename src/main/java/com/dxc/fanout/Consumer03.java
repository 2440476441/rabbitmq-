package com.dxc.fanout;

import com.dxc.util.RabbitUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 权计超
 * Company DXC.technology
 * @ClassName Consumer01
 * @CreateTime 2021-11-01 13:42
 * @Version 1.0
 * @Description: 消费者03
 */
public class Consumer03 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();
        final Channel channel = connection.createChannel();
        //通道绑定交换机
        channel.exchangeDeclare("regist","fanout");
        //临时队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queue,"regist","");
        //消费消息
        channel.basicQos(1);
        channel.basicConsume(queue,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
                System.out.println("消费者3"+new String(body));
            }
        });
    }
}
