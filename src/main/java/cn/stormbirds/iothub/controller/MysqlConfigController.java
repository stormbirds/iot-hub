package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MysqlConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.mqtt.MqttConstant;
import cn.stormbirds.iothub.service.IMysqlConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author stormbirds
 * @since 2022-09-11
 */
@RestController
@RequestMapping("/api/v1/mysqlConfig")
public class MysqlConfigController {

    @Resource
    private IMysqlConfigService mysqlConfigService;

    @PostMapping("/save")
    public ResultJson save(@RequestBody MysqlConfig mysqlConfig){
        return ResultJson.ok( mysqlConfigService.save(mysqlConfig));
    }

    @PostMapping("/update")
    public ResultJson update(@RequestBody MysqlConfig mysqlConfig){

        return ResultJson.ok( mysqlConfigService.edit(mysqlConfig));
    }

    @PostMapping("/delete/{id}")
    public ResultJson delete(@PathVariable Long id){
        if(MqttConstant.ONLINE.equalsIgnoreCase(mysqlConfigService.getById(id).getStatus())){
            mysqlConfigService.stop(id);
        }
        return ResultJson.ok(mysqlConfigService.removeById(id));
    }

    @GetMapping("/list")
    public ResultJson list(@RequestBody MysqlConfig mysqlConfig){
        QueryWrapper<MysqlConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(mysqlConfig.getId()),"id",mysqlConfig.getId());
        queryWrapper.like(!ObjectUtils.isEmpty(mysqlConfig.getName()),"name",mysqlConfig.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(mysqlConfig.getHost()),"host",mysqlConfig.getHost());
        queryWrapper.eq(!ObjectUtils.isEmpty(mysqlConfig.getPort()),"port",mysqlConfig.getPort());
        queryWrapper.like(!ObjectUtils.isEmpty(mysqlConfig.getScheme()),"scheme",mysqlConfig.getScheme());
        queryWrapper.like(!ObjectUtils.isEmpty(mysqlConfig.getUsername()),"username",mysqlConfig.getUsername());
        queryWrapper.eq(!ObjectUtils.isEmpty(mysqlConfig.getStatus()),"status",mysqlConfig.getStatus());
        queryWrapper.eq(!ObjectUtils.isEmpty(mysqlConfig.getEnable()),"enable",mysqlConfig.getEnable());
        return ResultJson.ok(mysqlConfigService.list(queryWrapper)) ;
    }

    @GetMapping("/start/{id}")
    public ResultJson start(@PathVariable Long id){
        return  ResultJson.ok( mysqlConfigService.start(id));
    }

    @GetMapping("/stop/{id}")
    public ResultJson stop(@PathVariable Long id){
        return  ResultJson.ok( mysqlConfigService.stop(id));
    }

    @PostMapping(value = "/testConnection")
    public ResultJson testConnection(@RequestBody MysqlConfig mysqlConfig) {
        return mysqlConfigService.testConnection(mysqlConfig);
    }

    @PostMapping("/showTables")
    public ResultJson showTables(@RequestBody MysqlConfig mysqlConfig) {
        return mysqlConfigService.showTables(mysqlConfig);
    }

    @PostMapping("/showDatabases")
    public ResultJson showDatabases(@RequestBody MysqlConfig mysqlConfig) {
        return mysqlConfigService.showDatabases(mysqlConfig);
    }
}
