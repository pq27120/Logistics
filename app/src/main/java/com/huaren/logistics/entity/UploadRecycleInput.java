package com.huaren.logistics.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 回收录入上传信息
 */
public class UploadRecycleInput implements Serializable{
    /** Not-null value. */
    private String orderBatchId;
    /** Not-null value. */
    private String cooperateId;
    private int lPdtgBatch;
    /** Not-null value. */
    private String driversID;
    private int recycleNum;
    private long recycleType;
    /** Not-null value. */
    private String recycleTypeValue;
    /** Not-null value. */
    private String status;
    /** Not-null value. */
    private java.util.Date recycleTime;
    private java.util.Date editTime;
    /** Not-null value. */
    private String userName;

    private int piece;//周转箱

    private int piece1;//退货箱

    private int piece2;//调剂箱

    private int piece3;//冷藏箱

    public String getOrderBatchId() {
        return orderBatchId;
    }

    public void setOrderBatchId(String orderBatchId) {
        this.orderBatchId = orderBatchId;
    }

    public String getCooperateId() {
        return cooperateId;
    }

    public void setCooperateId(String cooperateId) {
        this.cooperateId = cooperateId;
    }

    public int getlPdtgBatch() {
        return lPdtgBatch;
    }

    public void setlPdtgBatch(int lPdtgBatch) {
        this.lPdtgBatch = lPdtgBatch;
    }

    public String getDriversID() {
        return driversID;
    }

    public void setDriversID(String driversID) {
        this.driversID = driversID;
    }

    public int getRecycleNum() {
        return recycleNum;
    }

    public void setRecycleNum(int recycleNum) {
        this.recycleNum = recycleNum;
    }

    public long getRecycleType() {
        return recycleType;
    }

    public void setRecycleType(long recycleType) {
        this.recycleType = recycleType;
    }

    public String getRecycleTypeValue() {
        return recycleTypeValue;
    }

    public void setRecycleTypeValue(String recycleTypeValue) {
        this.recycleTypeValue = recycleTypeValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRecycleTime() {
        return recycleTime;
    }

    public void setRecycleTime(Date recycleTime) {
        this.recycleTime = recycleTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public int getPiece1() {
        return piece1;
    }

    public void setPiece1(int piece1) {
        this.piece1 = piece1;
    }

    public int getPiece2() {
        return piece2;
    }

    public void setPiece2(int piece2) {
        this.piece2 = piece2;
    }

    public int getPiece3() {
        return piece3;
    }

    public void setPiece3(int piece3) {
        this.piece3 = piece3;
    }
}
