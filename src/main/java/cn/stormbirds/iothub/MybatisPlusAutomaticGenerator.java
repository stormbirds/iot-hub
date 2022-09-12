package cn.stormbirds.iothub;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @ Description cn.stormbirds.iothub
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/10 9:14
 */
public class MybatisPlusAutomaticGenerator {
    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:sqlite::resource:db/iothub.db", "", "")
                .globalConfig(builder -> {
                    builder.author("stormbirds") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride()
                            .outputDir("E:\\dev\\iot-hub\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.stormbirds") // 设置父包名
                            .moduleName("iothub") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\dev\\iot-hub\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
//                            .likeTable(new LikeTable("t_")) // 设置需要生成的表名
                            .addInclude("t_iot_item")
                            .addTablePrefix("t_", "c_")// 设置过滤表前缀
                            .controllerBuilder()
                            .enableRestStyle().enableHyphenStyle()
                    ;
                    builder.entityBuilder().enableFileOverride()
                            .enableChainModel()
                            .enableLombok()
                            .enableTableFieldAnnotation();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
