package com.huaren.logistics.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RECYCLE_SCAN.
 */
public class RecycleScan {

    private Long id;
    /** Not-null value. */
    private java.util.Date recycleScanTime;
    /** Not-null value. */
    private String scanCode;
    private java.util.Date editTime;

    public RecycleScan() {
    }

    public RecycleScan(Long id) {
        this.id = id;
    }

    public RecycleScan(Long id, java.util.Date recycleScanTime, String scanCode, java.util.Date editTime) {
        this.id = id;
        this.recycleScanTime = recycleScanTime;
        this.scanCode = scanCode;
        this.editTime = editTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public java.util.Date getRecycleScanTime() {
        return recycleScanTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setRecycleScanTime(java.util.Date recycleScanTime) {
        this.recycleScanTime = recycleScanTime;
    }

    /** Not-null value. */
    public String getScanCode() {
        return scanCode;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public java.util.Date getEditTime() {
        return editTime;
    }

    public void setEditTime(java.util.Date editTime) {
        this.editTime = editTime;
    }

}
