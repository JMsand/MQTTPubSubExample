package cc.temp;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.UUID;

public abstract class MQTTPublisher {

    private static final String CLIENT_ID = UUID.randomUUID().toString();
    private final MqttClient client;
    private String topic;
    private  String serverUrl;

    public MQTTPublisher(String serverUrl, String topic) throws MqttException {
        this.topic = topic;
        this.serverUrl = serverUrl;
        this.client = new MqttClient(this.serverUrl, CLIENT_ID, new MemoryPersistence());
        setupClient();
    }

    private void setupClient() throws MqttException {
        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setWill(this.topic, "client offline".getBytes(), 2, true);
        this.client.connect(mqttConnectOptions);
    }

    public void publishData() throws MqttException, IOException {
        MqttMessage message = new MqttMessage(getPayload().getBytes("UTF-8"));
        message.setQos(1);
        message.setRetained(false);
        System.out.println("message sent:" + message + " published on topic :" + this.topic);
        client.publish(this.topic, message);
    }

    public abstract String getPayload() throws IOException;


}
