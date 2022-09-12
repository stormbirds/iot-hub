package cn.stormbirds.iothub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
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
@TableName("t_channel")
@Schema(name = "Channel对象", description = "")
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @NotEmpty(message = "通道名字不能为空")
    @TableField("channel_name")
    private String channelName;

    @TableField("driver")
    private String driver;

    @TableField("desc")
    private String desc;

    @TableField("driver_id")
    private Integer driverId;

    @TableField("last_status")
    private String lastStatus;
}
