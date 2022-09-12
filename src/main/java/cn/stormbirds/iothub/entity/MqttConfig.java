package cn.stormbirds.iothub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_mqtt_config")
@Schema(name = "MqttConfig对象", description = "")
public class MqttConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("host")
    private String host;

    @TableField("port")
    private Integer port;

    @TableField("protocol")
    private String protocol;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("client_id")
    private String clientId;

    @TableField("clean_session")
    private Boolean cleanSession;

    @TableField("session_timeout")
    private Integer sessionTimeout;

    @TableField("reconnect")
    private Boolean reconnect;

    @TableField("timeout")
    private Integer timeout;

    @TableField("keepalive")
    private Integer keepalive;

    @TableField("re_interval")
    private Integer reInterval;

    @TableField("version")
    private String version;

    @TableField("status")
    private String status;

    @TableField("last_connection_time")
    private String lastConnectionTime;

    @TableField("last_disconnection_time")
    private String lastDisconnectionTime;

    @TableField("create_at")
    private String createAt;

    @TableField("update_at")
    private String updateAt;

    @TableField("enable")
    private Boolean enable;

    @TableField("will_topic")
    private String willTopic;

    @TableField("will_qos")
    private Integer willQos;

    @TableField("will_retain")
    private Boolean willRetain;

    @TableField("will_message")
    private String willMessage;

    @TableField("will_delay")
    private Integer willDelay;
    @TableField("qos")
    private Integer qos;
}
