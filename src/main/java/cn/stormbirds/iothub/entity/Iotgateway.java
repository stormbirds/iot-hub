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
@TableName("t_iotgateway")
@Schema(name = "Iotgateway对象", description = "")
public class Iotgateway implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("type")
    private String type;

    @TableField("agent_id")
    private Long agentId;
}
