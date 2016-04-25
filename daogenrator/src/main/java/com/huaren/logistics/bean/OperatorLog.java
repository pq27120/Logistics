package com.huaren.logistics.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OPERATOR_LOG.
 */
public class OperatorLog {

    private Long id;
    /** Not-null value. */
    private String userName;
    /** Not-null value. */
    private String myType;
    private float longitude;
    private float latitude;
    private int lPdtgBatch;
    private String myNote;
    /** Not-null value. */
    private String dispatchNumber;
    /** Not-null value. */
    private String orderId;
    private java.util.Date editTime;
    /** Not-null value. */
    private String operType;
    private String pingjianeirong;
    private String detailId;

    public OperatorLog() {
    }

    public OperatorLog(Long id) {
        this.id = id;
    }

    public OperatorLog(Long id, String userName, String myType, float longitude, float latitude, int lPdtgBatch, String myNote, String dispatchNumber, String orderId, java.util.Date editTime, String operType, String pingjianeirong, String detailId) {
        this.id = id;
        this.userName = userName;
        this.myType = myType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.lPdtgBatch = lPdtgBatch;
        this.myNote = myNote;
        this.dispatchNumber = dispatchNumber;
        this.orderId = orderId;
        this.editTime = editTime;
        this.operType = operType;
        this.pingjianeirong = pingjianeirong;
        this.detailId = detailId;
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
    public String getMyType() {
        return myType;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMyType(String myType) {
        this.myType = myType;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getLPdtgBatch() {
        return lPdtgBatch;
    }

    public void setLPdtgBatch(int lPdtgBatch) {
        this.lPdtgBatch = lPdtgBatch;
    }

    public String getMyNote() {
        return myNote;
    }

    public void setMyNote(String myNote) {
        this.myNote = myNote;
    }

    /** Not-null value. */
    public String getDispatchNumber() {
        return dispatchNumber;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDispatchNumber(String dispatchNumber) {
        this.dispatchNumber = dispatchNumber;
    }

    /** Not-null value. */
    public String getOrderId() {
        return orderId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public java.util.Date getEditTime() {
        return editTime;
    }

    public void setEditTime(java.util.Date editTime) {
        this.editTime = editTime;
    }

    /** Not-null value. */
    public String getOperType() {
        return operType;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getPingjianeirong() {
        return pingjianeirong;
    }

    public void setPingjianeirong(String pingjianeirong) {
        this.pingjianeirong = pingjianeirong;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

}