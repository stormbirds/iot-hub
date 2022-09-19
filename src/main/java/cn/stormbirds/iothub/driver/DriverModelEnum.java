package cn.stormbirds.iothub.driver;

/**
 * @ Description cn.stormbirds.iothub.driver
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/19 21:39
 */
public enum  DriverModelEnum {
    /**
     * mqtt 客户端驱动
     */
    MQTT_CLIENT("MQTT Client"),
    /**
     * 数据库连接驱动
     */
    ODBC_CLIENT("ODBC Client"),
    /**
     * 经过mod bus协议对接通道
     */
    MODBUS_RTU_SERIAL("Modbus RTU Serial"),
    /**
     * 模拟设备驱动
     */
    SIMULATOR("Simulator")
    ;

    private String name;
    DriverModelEnum(String model) {
        this.name = model;
    }


    public String getName() {
        return name;
    }
}
