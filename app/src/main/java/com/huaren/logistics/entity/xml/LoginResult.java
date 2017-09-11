package com.huaren.logistics.entity.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by bj on 2017/8/21.
 */
public class LoginResult {
    @XStreamAlias("Code")
    private String code;
    @XStreamAlias("Msg")
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
