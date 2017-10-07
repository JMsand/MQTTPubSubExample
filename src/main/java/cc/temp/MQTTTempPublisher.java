package cc.temp;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

public class MQTTTempPublisher extends MQTTPublisher {
    private String tempScriptPath;

    public MQTTTempPublisher(String tempScriptPath, String serverUrl, String topic) throws MqttException {
        super(serverUrl, topic);
        this.tempScriptPath = tempScriptPath;
    }

    @Override
    public String getPayload() throws IOException {
        String[] cmd = new String[]{"/bin/sh", tempScriptPath};
        Process pr;
        pr = Runtime.getRuntime().exec(cmd);
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String outputTempScript = in.readLine();
        if (outputTempScript != null) {
            return "{\"temperature\": \"" + outputTempScript + "\", \"localTime\": \"" + LocalTime.now().toString() + "\"}";
        }
        else {
            return "{\"temperature\": \"unknown\", \"localTime\": \"" + LocalTime.now().toString() + "\"}";
        }

    }

    public static void main(String[] args) throws MqttException, InterruptedException, IOException {
        String tempScriptPath = Constants.DEFAULT_SCRIPT_LOCATION;
        String brokerUrl = Constants.DEFAULT_BROKER_URL;
        String topic = Constants.DEFAULT_TEMP_TOPIC;
        if (args.length > 0) {
            tempScriptPath = args[0];
        }
        if (args.length > 1) {
            brokerUrl = args[1];
        }
        if (args.length > 1) {
            topic = args[2];
        }

        MQTTPublisher pub = new MQTTTempPublisher(tempScriptPath, brokerUrl, topic);
        while (true) {
            Thread.sleep(1000);
            try {
                pub.publishData();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
