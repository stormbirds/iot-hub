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
@TableName("t_device")
@Schema(name = "Device对象", description = "")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("device_name")
    private String deviceName;

    @TableField("model")
    private String model;

    @TableField("desc")
    private String desc;

    @TableField("device_atts")
    private String deviceAtts;

    @TableField("last_status")
    private String lastStatus;

    @TableField("scan_rate")
    private Integer scanRate;
}
