import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JSMConsumer {
    public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.93.129:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        //1 创建连接工场,使用默认用户名密码，编码不再体现
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        //4 创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //5 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        /*异步非阻塞方式(监听器onMessage())
        订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听器，
        当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法。*/
        /*consumer.setMessageListener(message -> {
            if (message!=null){
                TextMessage textMessage =(TextMessage)message;
                try {
                    System.out.println( "****接受到消息"+textMessage.getText());
                    textMessage.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });*/
       /*等待时间
       try {
            TimeUnit.MILLISECONDS.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session.commit();*/

        //6 用来接收消息
        TextMessage textMessage = null;
        while(true)
        {
            textMessage = (TextMessage) consumer.receive();
            if (null != textMessage){
                System.out.println("*****消费者接收到消息："+textMessage.getText());
                textMessage.acknowledge();
            }else{
                break;
            }
        }
        consumer.close();
        session.close();
        connection.close();
    }
}
