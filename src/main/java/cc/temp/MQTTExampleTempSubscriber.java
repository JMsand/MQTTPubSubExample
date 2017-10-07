package cc.temp;

import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MQTTExampleTempSubscriber {

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private MqttClient client;

    public MQTTExampleTempSubscriber(String serverUrl) throws MqttException {
        client = new MqttClient(serverUrl, CLIENT_ID);
        client.setCallback(new MQTTCallback());
    }

    public void subscribe(String topic) throws MqttException {
        client.connect();
        client.subscribe(topic);
    }

    public static void main(String[] args) throws MqttException {
        String brokerUrl = Constants.DEFAULT_BROKER_URL;
        String topic = Constants.DEFAULT_TEMP_TOPIC;
        if (args.length > 0) {
            brokerUrl = args[0];
        }
        if (args.length > 1) {
            topic = args[1];
        }
        MQTTExampleTempSubscriber mqttLocalSubscriber = new MQTTExampleTempSubscriber(brokerUrl);
        mqttLocalSubscriber.subscribe(topic);
    }

    class MQTTCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable throwable) {

        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            System.out.println("message arrived :" + new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }

}



