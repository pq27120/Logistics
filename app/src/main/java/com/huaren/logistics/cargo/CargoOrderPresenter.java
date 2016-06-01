package com.huaren.logistics.cargo;

import android.content.Context;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoOrderCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;

import de.greenrobot.dao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class CargoOrderPresenter {

    private ICargoOrderView cargoOrderView;

    public CargoOrderPresenter(ICargoOrderView cargoOrderView) {
        this.cargoOrderView = cargoOrderView;
    }

    public void initCargoOrder(String customerId) {
        OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
        String userName = CommonTool.getSharePreference((Context) cargoOrderView, "curUserName");
        List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
                .where(
                        OrderDetailDao.Properties.CustomerId.eq(customerId)
                        , OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.READEY_CARGO.getStatus())
                        , OrderDetailDao.Properties.Status.eq("1")
                        , OrderDetailDao.Properties.UserName.eq(userName)
                )
                .list();
        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetail orderDetail = orderDetailList.get(i);
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
            qb.where(OrderDetailDao.Properties.Lpn.eq(detailCode), OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName));
            List<OrderDetail> orderDetailList = qb.list();
            if (orderDetailList != null && !orderDetailList.isEmpty()) {
                OrderDetail orderDetail = orderDetailList.get(0);
                if (!OrderStatusEnum.READEY_CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                    UiTool.showToast((Context) cargoOrderView, "货物已装车！");
                    cargoOrderView.clearLoadText();
                    return;
                }
                if (orderDetail.getCustomerId().equals(customerId)) {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
                    updateOrderCargo(detailCode);
                } else {
                    LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                    UiTool.showToast((Context) cargoOrderView, "该货物不属于当前客户！");
                    cargoOrderView.clearLoadText();
                    //cargoOrderView.showLoadDialog("装车",
                    //    "当前客户" + customerId + "，是否切换到客户" + orderDetail.getCustomerId());
                }
            } else {
                LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                UiTool.showToast((Context) cargoOrderView, "货物信息不存在！");
                cargoOrderView.clearLoadText();
            }
        } else {
            LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
            UiTool.showToast((Context) cargoOrderView, "请扫码后再装车！");
        }
    }

    public void updateOrderCargo(String lpn) {
        OrderDetailDao orderDetailDao =
                LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
        QueryBuilder qb = orderDetailDao.queryBuilder();
        String userName = CommonTool.getSharePreference((Context) cargoOrderView, "curUserName");
        qb.where(OrderDetailDao.Properties.Lpn.eq(lpn), OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName));
        List<OrderDetail> orderDetailList = qb.list();
        if (orderDetailList != null && !orderDetailList.isEmpty()) {
            OrderDetail orderDetail = orderDetailList.get(0);
            orderDetail.setDetailStatus(OrderStatusEnum.CARGO.getStatus());
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
            operatorLog.setMyType("1");
            operatorLog.setOrderId(orderDetail.getOrderId());
            operatorLog.setDetailId(orderDetail.getDetailId());
            operatorLogDao.insert(operatorLog);
            cargoOrderView.reInit();
        }
    }
}
