package org.chim.altass.executor.flume.kafka;

import com.google.common.base.Optional;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.instrumentation.kafka.KafkaSinkCounter;
import org.apache.flume.sink.AbstractSink;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import static org.chim.altass.executor.flume.kafka.KafkaSinkConstants.*;

/**
 * Class Name: AltassKafkaSink
 * Create Date: 11/7/18 7:24 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class AltassKafkaSink extends AbstractSink implements Configurable{

    private static final Logger logger = LoggerFactory.getLogger(AltassKafkaSink.class);

    private final Properties kafkaProps = new Properties();
    private KafkaProducer<String, byte[]> producer;

    private String topic;
    private int batchSize;
    private List<Future<RecordMetadata>> kafkaFutures;
    private KafkaSinkCounter counter;
    private boolean useAvroEventFormat;
    private String partitionHeader = null;
    private Integer staticPartitionId = null;
    private boolean allowTopicOverride;
    private String topicHeader = null;

    private Optional<SpecificDatumWriter<AvroFlumeEvent>> writer = Optional.absent();
    private Optional<SpecificDatumReader<AvroFlumeEvent>> reader = Optional.absent();
    private Optional<ByteArrayOutputStream> tempOutStream = Optional.absent();

    //Fine to use null for initial value, Avro will create new ones if this
    // is null
    private BinaryEncoder encoder = null;

    //For testing
    public String getTopic() {
        return topic;
    }

    public long getBatchSize() {
        return batchSize;
    }

    @Override
    public Status process() throws EventDeliveryException {
        Channel ch = getChannel();
        //get the transaction
        Transaction txn = ch.getTransaction();
        Event event =null;
        //begin the transaction
        txn.begin();
        while(true){
            event = ch.take();
            if (event!=null) {
                break;
            }
        }
        try {

            logger.debug("Get event.");

            String body = new String(event.getBody());
            System.out.println("event.getBody()-----" + body);

            String res = body + ":" + System.currentTimeMillis() + "\r\n";
            txn.commit();
            return Status.READY;
        } catch (Throwable th) {
            txn.rollback();

            if (th instanceof Error) {
                throw (Error) th;
            } else {
                throw new EventDeliveryException(th);
            }
        } finally {
            txn.close();
        }
    }

    @Override
    public synchronized void start() {
        // instantiate the producer
//        producer = new KafkaProducer<>(kafkaProps);
//        counter.start();
        super.start();
    }

    @Override
    public synchronized void stop() {
//        producer.close();
//        counter.stop();
        logger.info("Kafka Sink {} stopped. Metrics: {}", getName(), counter);
        super.stop();
    }


    /**
     * We configure the sink and generate properties for the Kafka Producer
     * <p>
     * Kafka producer properties is generated as follows:
     * 1. We generate a properties object with some static defaults that
     * can be overridden by Sink configuration
     * 2. We add the configuration users added for Kafka (parameters starting
     * with .kafka. and must be valid Kafka Producer properties
     * 3. We add the sink's documented parameters which can override other
     * properties
     *
     * @param context
     */
    @Override
    public void configure(Context context) {
        System.out.println("AltassKafkaSink配置中...");
/*
        translateOldProps(context);

        String topicStr = context.getString(TOPIC_CONFIG);
        if (topicStr == null || topicStr.isEmpty()) {
            topicStr = DEFAULT_TOPIC;
            logger.warn("Topic was not specified. Using {} as the topic.", topicStr);
        } else {
            logger.info("Using the static topic {}. This may be overridden by event headers", topicStr);
        }

        topic = topicStr;

        batchSize = context.getInteger(BATCH_SIZE, DEFAULT_BATCH_SIZE);

        if (logger.isDebugEnabled()) {
            logger.debug("Using batch size: {}", batchSize);
        }

        useAvroEventFormat = context.getBoolean(KafkaSinkConstants.AVRO_EVENT,
                KafkaSinkConstants.DEFAULT_AVRO_EVENT);

        partitionHeader = context.getString(KafkaSinkConstants.PARTITION_HEADER_NAME);
        staticPartitionId = context.getInteger(KafkaSinkConstants.STATIC_PARTITION_CONF);

        allowTopicOverride = context.getBoolean(KafkaSinkConstants.ALLOW_TOPIC_OVERRIDE_HEADER,
                KafkaSinkConstants.DEFAULT_ALLOW_TOPIC_OVERRIDE_HEADER);

        topicHeader = context.getString(KafkaSinkConstants.TOPIC_OVERRIDE_HEADER,
                KafkaSinkConstants.DEFAULT_TOPIC_OVERRIDE_HEADER);

        if (logger.isDebugEnabled()) {
            logger.debug(KafkaSinkConstants.AVRO_EVENT + " set to: {}", useAvroEventFormat);
        }

        kafkaFutures = new LinkedList<>();

        String bootStrapServers = context.getString(BOOTSTRAP_SERVERS_CONFIG);
        if (bootStrapServers == null || bootStrapServers.isEmpty()) {
            throw new ConfigurationException("Bootstrap Servers must be specified");
        }

        setProducerProps(context, bootStrapServers);

        if (logger.isDebugEnabled() && LogPrivacyUtil.allowLogPrintConfig()) {
            logger.debug("Kafka producer properties: {}", kafkaProps);
        }

        if (counter == null) {
            counter = new KafkaSinkCounter(getName());
        }*/
    }

    private void translateOldProps(Context ctx) {

        if (!(ctx.containsKey(TOPIC_CONFIG))) {
            ctx.put(TOPIC_CONFIG, ctx.getString("topic"));
            logger.warn("{} is deprecated. Please use the parameter {}", "topic", TOPIC_CONFIG);
        }

        //Broker List
        // If there is no value we need to check and set the old param and log a warning message
        if (!(ctx.containsKey(BOOTSTRAP_SERVERS_CONFIG))) {
            String brokerList = ctx.getString(BROKER_LIST_FLUME_KEY);
            if (brokerList == null || brokerList.isEmpty()) {
                throw new ConfigurationException("Bootstrap Servers must be specified");
            } else {
                ctx.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
                logger.warn("{} is deprecated. Please use the parameter {}",
                        BROKER_LIST_FLUME_KEY, BOOTSTRAP_SERVERS_CONFIG);
            }
        }

        //batch Size
        if (!(ctx.containsKey(BATCH_SIZE))) {
            String oldBatchSize = ctx.getString(OLD_BATCH_SIZE);
            if (oldBatchSize != null && !oldBatchSize.isEmpty()) {
                ctx.put(BATCH_SIZE, oldBatchSize);
                logger.warn("{} is deprecated. Please use the parameter {}", OLD_BATCH_SIZE, BATCH_SIZE);
            }
        }

        // Acks
        if (!(ctx.containsKey(KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG))) {
            String requiredKey = ctx.getString(
                    KafkaSinkConstants.REQUIRED_ACKS_FLUME_KEY);
            if (!(requiredKey == null) && !(requiredKey.isEmpty())) {
                ctx.put(KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG, requiredKey);
                logger.warn("{} is deprecated. Please use the parameter {}", REQUIRED_ACKS_FLUME_KEY,
                        KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG);
            }
        }

        if (ctx.containsKey(KEY_SERIALIZER_KEY)) {
            logger.warn("{} is deprecated. Flume now uses the latest Kafka producer which implements " +
                            "a different interface for serializers. Please use the parameter {}",
                    KEY_SERIALIZER_KEY, KAFKA_PRODUCER_PREFIX + ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG);
        }

        if (ctx.containsKey(MESSAGE_SERIALIZER_KEY)) {
            logger.warn("{} is deprecated. Flume now uses the latest Kafka producer which implements " +
                            "a different interface for serializers. Please use the parameter {}",
                    MESSAGE_SERIALIZER_KEY,
                    KAFKA_PRODUCER_PREFIX + ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);
        }
    }

    private void setProducerProps(Context context, String bootStrapServers) {
        kafkaProps.clear();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, DEFAULT_ACKS);
        //Defaults overridden based on config
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, DEFAULT_KEY_SERIALIZER);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DEFAULT_VALUE_SERIAIZER);
        kafkaProps.putAll(context.getSubProperties(KAFKA_PRODUCER_PREFIX));
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);

//        KafkaSSLUtil.addGlobalSSLParameters(kafkaProps);
    }

    protected Properties getKafkaProps() {
        return kafkaProps;
    }

    private byte[] serializeEvent(Event event, boolean useAvroEventFormat) throws IOException {
        byte[] bytes;
        if (useAvroEventFormat) {
            if (!tempOutStream.isPresent()) {
                tempOutStream = Optional.of(new ByteArrayOutputStream());
            }
            if (!writer.isPresent()) {
                writer = Optional.of(new SpecificDatumWriter<AvroFlumeEvent>(AvroFlumeEvent.class));
            }
            tempOutStream.get().reset();
            AvroFlumeEvent e = new AvroFlumeEvent(toCharSeqMap(event.getHeaders()),
                    ByteBuffer.wrap(event.getBody()));
            encoder = EncoderFactory.get().directBinaryEncoder(tempOutStream.get(), encoder);
            writer.get().write(e, encoder);
            encoder.flush();
            bytes = tempOutStream.get().toByteArray();
        } else {
            bytes = event.getBody();
        }
        return bytes;
    }

    private static Map<CharSequence, CharSequence> toCharSeqMap(Map<String, String> stringMap) {
        Map<CharSequence, CharSequence> charSeqMap = new HashMap<CharSequence, CharSequence>();
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            charSeqMap.put(entry.getKey(), entry.getValue());
        }
        return charSeqMap;
    }

}

class SinkCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(SinkCallback.class);
    private long startTime;

    public SinkCallback(long startTime) {
        this.startTime = startTime;
    }

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            logger.debug("Error sending message to Kafka {} ", exception.getMessage());
        }

        if (logger.isDebugEnabled()) {
            long eventElapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                logger.debug("Acked message partition:{} ofset:{}", metadata.partition(),
                        metadata.offset());
            }
            logger.debug("Elapsed time for send: {}", eventElapsedTime);
        }
    }
}
