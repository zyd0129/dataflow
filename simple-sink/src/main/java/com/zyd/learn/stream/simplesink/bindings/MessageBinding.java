package com.zyd.learn.stream.simplesink.bindings;

import com.zyd.learn.stream.simplesink.bindings.intf.Transform;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

/**
 * 在rabbitmq生成一个 名为input的topicExchange, 和一个队列input.anonymous.fcy30EnAQj25wT6q3y001Q， routing-key:#
 * anonymous.fcy30EnAQj25wT6q3y001Q 为group属性，没有设置的话，会自动生成一个以anonymous前缀的名字
 * <p>
 * 重要概念： @EnableBinding, @Input @Output @StreamListener(channel名称) @SentTo
 * <p>
 * durability的概念 是指 建立了一个durable queue, consumer group里的应用都stop了，即没有人接受消息了，消息会持久存储在queue里
 * <p>
 * partitioning 在consumer group里，一个消息会被随机的发送到某一个instance上处理且仅被处理一次。
 * 但有时我们希望具有相同特征的消息，被同一个consumer instance消费，比如所有带user_id="zyd"的消息，被某一个实例处理，而不是分发到不同的consumer instance里
 *
 * @EnableBinding, 绑定。创建channel,(input/output), channel和外部消息中间件绑定。
 * 1.Registering MessageChannel input
 * 2.declaring queue for inbound: input.anonymous.SMPfcuDjQ4m7xxBQhhIVKw, bound to: input
 * 3. Created new connection: rabbitConnectionFactory#641856:0/SimpleConnection@178e06a [delegate=amqp://guest@127.0.0.1:5672/, localPort= 57337]
 * @StreamListener Annotation that marks a method to be a listener to inputs declared via {@link EnableBinding} (e.g. channels).
 * Channel 'application.input' has 1 subscriber(s).,可以对同一个channle多次订阅，可用于
 * Channel 'application.transformInput' has 1 subscriber(s).
 * @SendTo Annotation that indicates a method's return value should be converted to a  Message if necessary and sent to the specified destination.
 * Channel 'application.transformOutput' has 1 subscriber(s).
 */

@EnableScheduling
@EnableBinding({Sink.class, Transform.class, Source.class})
public class MessageBinding {
    @Autowired
    Source source;

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private final static String[] names = {
            "a", "b", "c", "d"
    };

    /**
     * 消息处理方法 handle method
     * 消息自动转换 message convert, 如json字符串转换成java对象，{"name":"zyd"}
     *
     * @param person
     */
    @StreamListener(Sink.INPUT)
    public void sink(Person person) {
        System.out.println(person);

        throw new RuntimeException("BOOM");
    }

    @ServiceActivator(inputChannel = Sink.INPUT + ".mygroup.errors") //channel name 'input.myGroup.errors'
    public void error(Message<?> message) {
        System.out.println("Handling ERROR: " + message);
    }

    @StreamListener("errorChannel")
    public void error1(Message<?> message) {
        System.out.println("Handling ERROR1: " + message);
    }

    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='bogey'")
    public void sink2(Person person) {
        System.out.println("sink2");
        System.out.println(person);
    }

    /**
     * transformInput是一个channel,或者说是一个binding
     *
     * @param string
     * @return
     */
    @StreamListener("transformInput")
    @SendTo("transformOutput")
    public String transform(String string) {
        System.out.println(string.toUpperCase());
        return string.toUpperCase();
    }

    @Scheduled(fixedDelay = 1000)
    public void schedule() {
        Person person = new Person();
        person.setName(names[RANDOM.nextInt(names.length)]);
        source.output().send(MessageBuilder.withPayload(person).build());
    }

    @Data
    private static class Person {
        private String name;
    }
}
