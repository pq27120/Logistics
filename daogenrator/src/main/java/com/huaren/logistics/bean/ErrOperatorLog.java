package com.huaren.logistics.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ERR_OPERATOR_LOG.
 */
public class ErrOperatorLog {

    private Long id;
    /** Not-null value. */
    private String customerId;
    /** Not-null value. */
    private String userName;
    private int lPdtgBatch;
    /** Not-null value. */
    private String driverId;
    /** Not-null value. */
    private String cooperateID;
    private String lpn;
    private java.util.Date addTime;

    public ErrOperatorLog() {
    }

    public ErrOperatorLog(Long id) {
        this.id = id;
    }

    public ErrOperatorLog(Long id, String customerId, String userName, int lPdtgBatch, String driverId, String cooperateID, String lpn, java.util.Date addTime) {
        this.id = id;
        this.customerId = customerId;
        this.userName = userName;
        this.lPdtgBatch = lPdtgBatch;
        this.driverId = driverId;
        this.cooperateID = cooperateID;
        this.lpn = lpn;
        this.addTime = addTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getCustomerId() {
        return customerId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /** Not-null value. */
    public String getUserName() {
        return userName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLPdtgBatch() {
        return lPdtgBatch;
    }

    public void setLPdtgBatch(int lPdtgBatch) {
        this.lPdtgBatch = lPdtgBatch;
    }

    /** Not-null value. */
    public String getDriverId() {
        return driverId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /** Not-null value. */
    public String getCooperateID() {
        return cooperateID;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCooperateID(String cooperateID) {
        this.cooperateID = cooperateID;
    }

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    public java.util.Date getAddTime() {
        return addTime;
    }

    public void setAddTime(java.util.Date addTime) {
        this.addTime = addTime;
    }

}
