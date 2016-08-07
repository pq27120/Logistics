package com.huaren.logistics.uncargo;

import android.content.Context;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoOrderCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.ErrOperatorLog;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.ErrOperatorLogDao;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.evaluation.EvaluationDetailActivity;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;

import de.greenrobot.dao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class UnCargoOrderPresenter {

    private IUnCargoOrderView cargoOrderView;

    public UnCargoOrderPresenter(IUnCargoOrderView cargoOrderView) {
        this.cargoOrderView = cargoOrderView;
    }

    public void initCargoOrder(String customerId) {
        OrderDetailDao logisticsOrderDao = LogisticsApplication.getInstance().getOrderDetailDao();
        String userName = CommonTool.getSharePreference((Context) cargoOrderView, "curUserName");
        List<OrderDetail> logisticsOrderList = logisticsOrderDao.queryBuilder()
                .where(OrderDetailDao.Properties.CustomerId.eq(customerId),
                        OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.CARGO.getStatus()),
                        OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName))
                .list();
        for (int i = 0; i < logisticsOrderList.size(); i++) {
            OrderDetail orderDetail = logisticsOrderList.get(i);
            int drawable = R.drawable.star_do;
            String desc = CommonTool.getDescByStatus(orderDetail.getDetailStatus());
            Card card = new Card.Builder((Context) cargoOrderView).setTag(orderDetail.getDetailId())
                    .withProvider(CargoOrderCardProvider.class)
                    .setTitle(orderDetail.getLpn() + "(" + orderDetail.getMtype() + ")")
                    .setDescription(desc)
                    .setDrawable(drawable)
                    .endConfig()
                    .build();
            cargoOrderView.addCard(card);
        }
    }

    public void loadDetailCargo(String customerId, String detailCode) {
        if (StringTool.isNotNull(detailCode)) {
            OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
            QueryBuilder qb = orderDetailDao.queryBuilder();
            String userName = CommonTool.getSharePreference((Context) cargoOrderView, "curUserName");
            qb.where(OrderDetailDao.Properties.Lpn.eq(detailCode),
                    OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName));
            List<OrderDetail> orderDetailList = qb.list();
            if (orderDetailList != null && !orderDetailList.isEmpty()) {
                OrderDetail orderDetail = orderDetailList.get(0);
                if (OrderStatusEnum.UNCARGO.getStatus().equals(orderDetail.getDetailStatus())) {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                    UiTool.showToast((Context) cargoOrderView, "货物已卸车！");
                    cargoOrderView.clearRemoveText();
                    return;
                } else if (!OrderStatusEnum.CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                    UiTool.showToast((Context) cargoOrderView, "该货物无法卸车！");
                    cargoOrderView.clearRemoveText();

                    ErrOperatorLog errOperatorLog = new ErrOperatorLog();
                    errOperatorLog.setAddTime(new Date());
                    errOperatorLog.setUserName(userName);
                    errOperatorLog.setCustomerId(orderDetail.getCooperateId());
                    String driverId = CommonTool.getSharePreference((Context) cargoOrderView, "driverId");
                    errOperatorLog.setDriverId(driverId);
                    errOperatorLog.setLPdtgBatch(orderDetail.getLPdtgBatch());
                    errOperatorLog.setCooperateID(orderDetail.getCooperateId());
                    errOperatorLog.setLpn(detailCode);
                    ErrOperatorLogDao dao = LogisticsApplication.getInstance().getErrOperatorLogDao();
                    dao.insert(errOperatorLog);
                    return;
                }
                if (orderDetail.getCustomerId().equals(customerId)) {
                    updateOrderCargo(detailCode);
                } else {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();

                    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
                    Customer customer = customerDao.queryBuilder().where(CustomerDao.Properties.Id.eq(customerId)).unique();
                    UiTool.showToast((Context) cargoOrderView, "不是当前客户" + customer.getSPdtgCustfullname() + "的货物，请不要卸车！");
                    cargoOrderView.clearRemoveText();

                    ErrOperatorLog errOperatorLog = new ErrOperatorLog();
                    errOperatorLog.setAddTime(new Date());
                    errOperatorLog.setUserName(userName);
                    errOperatorLog.setCustomerId(customer.getCooperateId());
                    String driverId = CommonTool.getSharePreference((Context) cargoOrderView, "driverId");
                    errOperatorLog.setDriverId(driverId);
                    errOperatorLog.setLPdtgBatch(orderDetail.getLPdtgBatch());
                    errOperatorLog.setCooperateID(orderDetail.getCooperateId());
                    errOperatorLog.setLpn(detailCode);
                    ErrOperatorLogDao dao = LogisticsApplication.getInstance().getErrOperatorLogDao();
                    dao.insert(errOperatorLog);
                }
            } else {
                LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                UiTool.showToast((Context) cargoOrderView, "货物信息不存在！");
                cargoOrderView.clearRemoveText();

                CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
                Customer customer = customerDao.queryBuilder().where(CustomerDao.Properties.Id.eq(customerId)).unique();

                ErrOperatorLog errOperatorLog = new ErrOperatorLog();
                errOperatorLog.setAddTime(new Date());
                errOperatorLog.setUserName(userName);
                errOperatorLog.setCustomerId("");
                String driverId = CommonTool.getSharePreference((Context) cargoOrderView, "driverId");
                errOperatorLog.setDriverId(driverId);
                errOperatorLog.setLPdtgBatch(customer.getLPdtgBatch());
                errOperatorLog.setCooperateID(customer.getCooperateId());
                errOperatorLog.setLpn(detailCode);
                ErrOperatorLogDao dao = LogisticsApplication.getInstance().getErrOperatorLogDao();
                dao.insert(errOperatorLog);
            }
        } else {
            LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
            UiTool.showToast((Context) cargoOrderView, "请扫码后再卸车！");
        }
    }

    public void updateOrderCargo(String detailId) {
        OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
        QueryBuilder qb = orderDetailDao.queryBuilder();
        String userName = CommonTool.getSharePreference((Context) cargoOrderView, "curUserName");
        qb.where(OrderDetailDao.Properties.Lpn.eq(detailId), OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName))
                .orderDesc(OrderDetailDao.Properties.LPdtgBatch);
        List<OrderDetail> orderDetailList = qb.list();
        if (orderDetailList != null && !orderDetailList.isEmpty()) {
            OrderDetail orderDetail = orderDetailList.get(0);
            orderDetail.setDetailStatus(OrderStatusEnum.UNCARGO.getStatus());
            orderDetail.setEditTime(new Date());
            orderDetailDao.insertOrReplaceInTx(orderDetail);

            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            OperatorLog operatorLog = new OperatorLog();
            operatorLog.setOperType("1");
            operatorLog.setUserName(userName);
            operatorLog.setDispatchNumber(orderDetail.getDispatchNumber());
            operatorLog.setEditTime(new Date());
            operatorLog.setLatitude(0);
            operatorLog.setLongitude(0);
            operatorLog.setLPdtgBatch(orderDetail.getLPdtgBatch());
            operatorLog.setMyType("2");
            operatorLog.setOrderId(orderDetail.getOrderId());
            operatorLog.setDetailId(orderDetail.getDetailId());
            operatorLogDao.insert(operatorLog);

            LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
            OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
            OrderBatch orderBatch = orderBatchDao.queryBuilder()
                    .where(OrderBatchDao.Properties.Id.eq(orderDetail.getCooperateId()
                            + orderDetail.getLPdtgBatch()
                            + orderDetail.getDriversID()), OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName))
                    .unique();
            orderBatch.setCanEvalutaion("1");
            orderBatch.setEditTime(new Date());
            orderBatchDao.update(orderBatch);
            List<OrderDetail> evaluationDetailList = orderDetailDao.queryBuilder()
                    .where(OrderDetailDao.Properties.CooperateId.eq(orderDetail.getCooperateId()),
                            OrderDetailDao.Properties.DriversID.eq(orderDetail.getDriversID()),
                            OrderDetailDao.Properties.LPdtgBatch.eq(orderDetail.getLPdtgBatch()),
                            OrderDetailDao.Properties.DetailId.notEq(orderDetail.getDetailId()),
                            OrderDetailDao.Properties.DetailStatus.notEq(OrderStatusEnum.UNCARGO.getStatus()),
                            OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName))
                    .list();
            if (evaluationDetailList == null || evaluationDetailList.isEmpty()) {
                EvaluationDetailActivity.actionStart((Context) cargoOrderView, orderDetail.getCooperateId()
                        + orderDetail.getLPdtgBatch()
                        + orderDetail.getDriversID());
                cargoOrderView.finishActivity();
            }
            cargoOrderView.reInit();
        }
    }
}
