package com.plain.time_series.influx_db;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "invoke_monitor")
public class PlainObject {
    @Column(name = "isSuccess", tag = true)
    private boolean isSuccess;
    @Column(name = "invokeTime")
    private int invokeTime;
    @Column(name = "interfaceName")
    private String interfaceName;
    @Column(name = "methodName")
    private String methodName;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(int invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "PlainObject{" +
                "isSuccess=" + isSuccess +
                ", invokeTime=" + invokeTime +
                ", interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
