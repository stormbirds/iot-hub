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
 * @since 2022-09-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_iot_item")
@Schema(name = "IotItem对象", description = "")
public class IotItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("gateway_id")
    private Integer gatewayId;

    @TableField("device_id")
    private Integer deviceId;

    @TableField("scan_rate")
    private Integer scanRate;

    @TableField("enable")
    private Boolean enable;
}
