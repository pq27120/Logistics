package com.huaren.logistics.entity.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by bj on 2017/8/21.
 */
public class CommandResult {
    @XStreamAlias("Code")
    private String code;
    @XStreamAlias("Cmd")
    private String cmd;
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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "code='" + code + '\'' +
                ", cmd='" + cmd + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
