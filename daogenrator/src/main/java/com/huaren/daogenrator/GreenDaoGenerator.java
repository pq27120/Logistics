package com.huaren.daogenrator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
  public static void main(String[] args) throws Exception {
    // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
    // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
    Schema schema = new Schema(1, "com.huaren.logistics.bean");
    //      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
    //      Schema schema = new Schema(1, "me.itangqi.bean");
    schema.setDefaultJavaPackageDao("com.huaren.logistics.dao");

    // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
    // schema2.enableActiveEntitiesByDefault();
    // schema2.enableKeepSectionsByDefault();

    // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
    addTable(schema);
    // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
    // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
    new DaoGenerator().generateAll(schema, "./daogenrator/src/main/java");
  }

  /**
   * @param schema
   */
  private static void addTable(Schema schema) {
    Entity userEntity = schema.addEntity("LogisticsUser");
    userEntity.addLongProperty("id").primaryKey();
    userEntity.addStringProperty("userName").notNull();
    userEntity.addStringProperty("pwd").notNull();
    userEntity.addStringProperty("driverId"); //司机ID，有可能为空

    Entity sysDicEntity = schema.addEntity("SysDic"); //字典表
    sysDicEntity.addLongProperty("id").primaryKey();
    sysDicEntity.addStringProperty("myName").notNull();
    sysDicEntity.addStringProperty("myState");
    sysDicEntity.addStringProperty("note");

    Entity sysDicValueEntity = schema.addEntity("SysDicValue"); //字典值表
    sysDicValueEntity.addLongProperty("id").primaryKey();
    sysDicValueEntity.addStringProperty("myDisplayValue").notNull();
    sysDicValueEntity.addIntProperty("dicId").notNull();
    sysDicValueEntity.addStringProperty("myName").notNull();
    sysDicValueEntity.addStringProperty("note");

    // 一个实体（类）就关联到数据库中的一张表，此处表名为「Customer」（既类名）
    Entity customer = schema.addEntity("Customer");
    // 你也可以重新给表命名
    // note.setTableName("NODE");
    customer.implementsSerializable();
    // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
    // 接下来你便可以设置表中的字段：
    customer.addStringProperty("id").primaryKey();
    customer.addStringProperty("cooperateId").notNull();
    customer.addIntProperty("lPdtgBatch").notNull();//当前批次
    customer.addStringProperty("sPdtgCustfullname").notNull();//客户姓名
    customer.addStringProperty("coopPwd").notNull();
    customer.addStringProperty("status").notNull();//0 停用 1 启用
    customer.addDateProperty("addTime").notNull();
    customer.addDateProperty("editTime");
    customer.addStringProperty("userName").notNull();

    Entity downBatchInfo = schema.addEntity("DownBatchInfo");
    downBatchInfo.implementsSerializable();
    downBatchInfo.addLongProperty("lPdtgBatch").primaryKey();//当前批次
    downBatchInfo.addDateProperty("addTime").notNull();
    downBatchInfo.addStringProperty("userName").notNull();

    Entity batchInfo = schema.addEntity("OrderBatch");
    batchInfo.implementsSerializable();
    batchInfo.addStringProperty("id").primaryKey();
    batchInfo.addStringProperty("cooperateId").notNull();//客户编码
    batchInfo.addStringProperty("sPdtgCustfullname");//客户名称
    batchInfo.addIntProperty("lPdtgBatch").notNull();//批次
    batchInfo.addStringProperty("driversID").notNull();//司机编码
    batchInfo.addStringProperty("evaluation");//评价
    batchInfo.addStringProperty("canEvalutaion");//是否可以评价 0 不可以 1 可以
    batchInfo.addStringProperty("status").notNull();//0 停用 1 启用
    batchInfo.addDateProperty("addTime").notNull();
    batchInfo.addDateProperty("editTime");
    batchInfo.addStringProperty("userName").notNull();

    Entity orderDetail = schema.addEntity("OrderDetail");//订单详情
    orderDetail.implementsSerializable();
    orderDetail.addStringProperty("detailId").primaryKey();//明细ID
    orderDetail.addStringProperty("customerId").notNull(); //客户主表
    orderDetail.addStringProperty("cooperateId").notNull(); //客户ID
    orderDetail.addStringProperty("dispatchNumber").notNull(); //调度单号
    orderDetail.addStringProperty("dispatchCreatTime").notNull(); //调度时间
    orderDetail.addStringProperty("driversID").notNull(); //司机编码
    orderDetail.addStringProperty("sPdtgEmplname").notNull(); //司机名称
    orderDetail.addStringProperty("sPdtgEmplname2").notNull(); //随行人员
    orderDetail.addStringProperty("suicherenyuanID").notNull(); //随行人员ID
    orderDetail.addStringProperty("suicherenyuanID2").notNull(); //随行人员2ID
    orderDetail.addStringProperty("sPdtgEmplname3").notNull(); //随行人员2名称
    orderDetail.addStringProperty("sPdtgVehicleno").notNull(); //运输车辆
    orderDetail.addStringProperty("countPieces").notNull(); //件数
    orderDetail.addIntProperty("lPdtgBatch").notNull(); //批次
    orderDetail.addStringProperty("ordered").notNull();//订单ID
    orderDetail.addStringProperty("orderId").notNull();
    orderDetail.addStringProperty("waveKey").notNull();//波次
    orderDetail.addStringProperty("lpn").notNull(); //箱号条码
    orderDetail.addStringProperty("mtype").notNull();//类型
    orderDetail.addStringProperty("uom").notNull();//
    orderDetail.addStringProperty("detailStatus").notNull();
    orderDetail.addStringProperty("status").notNull();//0 停用 1 启用
    orderDetail.addStringProperty("recordUpload");//0 未上传 1 已上传
    orderDetail.addStringProperty("evaluationUpload");//0 未上传 1 已上传
    orderDetail.addDateProperty("addTime").notNull();
    orderDetail.addDateProperty("editTime");
    orderDetail.addStringProperty("userName").notNull();
    orderDetail.addStringProperty("evaluation"); //评价信息冗余

    Entity recycleInput = schema.addEntity("RecycleInput"); //回收录入信息
    recycleInput.addIdProperty();
    recycleInput.addStringProperty("orderBatchId").notNull();
    recycleInput.addStringProperty("cooperateId").notNull();//客户编码
    recycleInput.addIntProperty("lPdtgBatch").notNull();//批次
    recycleInput.addStringProperty("driversID").notNull();//司机编号
    recycleInput.addIntProperty("recycleNum").notNull();//回收数量
    recycleInput.addLongProperty("recycleType").notNull();//回收类型
    recycleInput.addStringProperty("recycleTypeValue").notNull();//回收类型名称
    recycleInput.addStringProperty("status").notNull();//0 停用 1 启用
    recycleInput.addDateProperty("recycleTime").notNull();//回收录入时间
    recycleInput.addDateProperty("editTime");
    recycleInput.addStringProperty("userName").notNull();

    Entity recycleScan = schema.addEntity("RecycleScan"); //回收扫描信息
    recycleScan.addIdProperty();
    recycleScan.addIntProperty("lPdtgBatch").notNull();//批次
    recycleScan.addDateProperty("recycleScanTime").notNull();
    recycleScan.addStringProperty("scanCode").notNull();
    recycleScan.addStringProperty("status").notNull();//0 停用 1 启用
    recycleScan.addDateProperty("editTime");
    recycleScan.addStringProperty("userName").notNull();
    recycleScan.addLongProperty("recycleType").notNull();//回收类型
    recycleScan.addStringProperty("recycleTypeValue").notNull();//回收类型名称

    Entity OperatorLog = schema.addEntity("OperatorLog"); //操作日志信息
    OperatorLog.addIdProperty();
    OperatorLog.addStringProperty("userName").notNull();//用户名
    OperatorLog.addStringProperty("myType").notNull();//操作状态
    OperatorLog.addFloatProperty("longitude").notNull();//经度
    OperatorLog.addFloatProperty("latitude").notNull();//纬度
    OperatorLog.addIntProperty("lPdtgBatch").notNull();//批次
    OperatorLog.addStringProperty("myNote");//备注
    OperatorLog.addStringProperty("dispatchNumber").notNull(); //调度单号
    OperatorLog.addStringProperty("orderId").notNull(); //订单号
    OperatorLog.addDateProperty("editTime");
    OperatorLog.addStringProperty("operType").notNull(); // 1 操作 2 评价
    OperatorLog.addStringProperty("pingjianeirong"); // 评价内容
    OperatorLog.addStringProperty("detailId"); // 明细ID

    Entity ErrOperatorLog = schema.addEntity("ErrOperatorLog"); //操作日志信息
    ErrOperatorLog.addIdProperty();
    ErrOperatorLog.addStringProperty("customerId").notNull();//当前客户ID
    ErrOperatorLog.addStringProperty("userName").notNull();//用户名
    ErrOperatorLog.addIntProperty("lPdtgBatch").notNull();//批次
    ErrOperatorLog.addStringProperty("driverId").notNull(); //司机ID
    ErrOperatorLog.addStringProperty("cooperateID").notNull(); //正确客户ID
    ErrOperatorLog.addStringProperty("lpn"); // 条码
    ErrOperatorLog.addDateProperty("addTime");//新增时间
  }
}
