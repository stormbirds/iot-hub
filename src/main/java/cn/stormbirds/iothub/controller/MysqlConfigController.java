package cn.stormbirds.iothub.controller;

import cn.stormbirds.iothub.base.ResultJson;
import cn.stormbirds.iothub.entity.MysqlConfig;
import cn.stormbirds.iothub.exception.BizException;
import cn.stormbirds.iothub.service.IMysqlConfigService;
import org.springframework.http.MediaType;
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

    @GetMapping("/start/{id}")
    public ResultJson start(@PathVariable Integer id){
        return  ResultJson.ok( mysqlConfigService.start(id));
    }

    @GetMapping("/stop/{id}")
    public ResultJson stop(@PathVariable Integer id){
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
