package cn.stormbirds.iothub.mqtt;

import cn.stormbirds.iothub.entity.MqttConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class MqttProperties {

    private String name;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接地址
     */
    @NotBlank(message = "连接地址不能为空")
    private String host;
    @NotNull(message = "连接地址端口不能为空")
    private Integer port;
    @NotBlank(message = "连接协议不能为空")
    private String protocol;
    /**
     * 客户端Id，同一台服务器下，不允许出现重复的客户端id
     */
    private String clientId;

    /**
     * 遗嘱主题
     */
    private String willTopic;

    private Integer willQos;

    private Boolean willRetain;

    private String willMessage;

    private Integer willDelay;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端
     * 发送个消息判断客户端是否在线，但这个方法并没有重连的机制
     */
    private int keepAlive;

    /**
     * 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连
     * 接记录，这里设置为true表示每次连接到服务器都以新的身份连接
     */
    private Boolean cleanSession;

    /**
     * 是否断线重连
     */
    private Boolean reconnect;

    /**
     * 连接方式
     */
    private Integer qos;

    private String version;

    private Integer sessionTimeout;

    private String defaultTopic;

    public MqttProperties(MqttConfig mqttConfig) {
        this.name = mqttConfig.getName();
        this.username = mqttConfig.getUsername();
        this.password = mqttConfig.getPassword();
        this.host = mqttConfig.getHost();
        this.port = mqttConfig.getPort();
        this.protocol = mqttConfig.getProtocol();
        this.clientId = mqttConfig.getClientId();
        this.willTopic = mqttConfig.getWillTopic();
        this.willDelay = mqttConfig.getWillDelay();
        this.willMessage = mqttConfig.getWillMessage();
        this.willQos = mqttConfig.getWillQos();
        this.willRetain = mqttConfig.getWillRetain();
        this.timeout = mqttConfig.getTimeout();
        this.keepAlive = mqttConfig.getKeepalive();
        this.cleanSession = mqttConfig.getCleanSession();
        this.reconnect = mqttConfig.getReconnect();
        this.qos = mqttConfig.getQos();
        this.version = mqttConfig.getVersion();
        this.sessionTimeout = mqttConfig.getSessionTimeout();
        this.defaultTopic = mqttConfig.getDefaultTopic();
    }
}