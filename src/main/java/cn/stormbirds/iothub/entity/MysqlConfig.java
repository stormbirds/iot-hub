package cn.stormbirds.iothub.entity;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_database_config")
@Schema(name = "ODBC数据库Config对象", description = "")
public class MysqlConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @NotEmpty(message = "数据库名称不能为空")
    @TableField("name")
    private String name;

    @NotEmpty(message = "数据库地址不能为空")
    @TableField("host")
    private String host;

    @NotNull(message = "数据库端口不能为空")
    @TableField("port")
    private Integer port;

    @TableField("scheme")
    private String scheme;

    @TableField("params")
    private String params;

    @NotEmpty(message = "数据库用户名不能为空")
    @TableField("username")
    private String username;

    @NotEmpty(message = "数据库密码不能为空")
    @TableField("password")
    private String password;

    @TableField("status")
    private String status;

    @TableField("create_at")
    private String createAt = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER);

    @TableField("update_at")
    private String updateAt = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_FORMATTER);

    @TableField("enable")
    private Boolean enable = false;
}
