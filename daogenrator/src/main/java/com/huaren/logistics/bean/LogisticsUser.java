package com.huaren.logistics.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LOGISTICS_USER.
 */
public class LogisticsUser {

    private Long id;
    /** Not-null value. */
    private String userName;
    /** Not-null value. */
    private String pwd;

    public LogisticsUser() {
    }

    public LogisticsUser(Long id) {
        this.id = id;
    }

    public LogisticsUser(Long id, String userName, String pwd) {
        this.id = id;
        this.userName = userName;
        this.pwd = pwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getUserName() {
        return userName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Not-null value. */
    public String getPwd() {
        return pwd;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
