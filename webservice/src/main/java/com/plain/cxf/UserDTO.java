package com.plain.cxf;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Web Service传输User信息的DTO.*
 * 只传输外部接口需要的属性.使用JAXB 2.0的annotation标注JAVA-XML映射,尽量使用默认约定.*
 *
 * @XmlRootElement指定User为XML的根元素。User类的属性默认指定映射为@XmlElement。
 * @XmlElement用来定义XML中的子元素。
 * @XmlType-映射一个类或一个枚举类型成一个XML Schema类型
 */
@XmlRootElement
@XmlType(name = "User", namespace = WsConstants.NS)
public class UserDTO {
    private Long id;
    private String loginName;
    private String name;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}