package com.huaren.logistics.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RECYCLE_INPUT.
 */
public class RecycleInput {

    private Long id;
    private int cooperateId;
    private int lPdtgBatch;
    private int recycleNum;
    /** Not-null value. */
    private String status;
    /** Not-null value. */
    private java.util.Date recycleTime;
    private java.util.Date editTime;

    public RecycleInput() {
    }

    public RecycleInput(Long id) {
        this.id = id;
    }

    public RecycleInput(Long id, int cooperateId, int lPdtgBatch, int recycleNum, String status, java.util.Date recycleTime, java.util.Date editTime) {
        this.id = id;
        this.cooperateId = cooperateId;
        this.lPdtgBatch = lPdtgBatch;
        this.recycleNum = recycleNum;
        this.status = status;
        this.recycleTime = recycleTime;
        this.editTime = editTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCooperateId() {
        return cooperateId;
    }

    public void setCooperateId(int cooperateId) {
        this.cooperateId = cooperateId;
    }

    public int getLPdtgBatch() {
        return lPdtgBatch;
    }

    public void setLPdtgBatch(int lPdtgBatch) {
        this.lPdtgBatch = lPdtgBatch;
    }

    public int getRecycleNum() {
        return recycleNum;
    }

    public void setRecycleNum(int recycleNum) {
        this.recycleNum = recycleNum;
    }

    /** Not-null value. */
    public String getStatus() {
        return status;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** Not-null value. */
    public java.util.Date getRecycleTime() {
        return recycleTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setRecycleTime(java.util.Date recycleTime) {
        this.recycleTime = recycleTime;
    }

    public java.util.Date getEditTime() {
        return editTime;
    }

    public void setEditTime(java.util.Date editTime) {
        this.editTime = editTime;
    }

}
