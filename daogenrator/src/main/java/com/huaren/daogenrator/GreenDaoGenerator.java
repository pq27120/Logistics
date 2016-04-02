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
    customer.addStringProperty("cooperateId").primaryKey();
    // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
    // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
    customer.addStringProperty("cooperateName").notNull();
    customer.addDateProperty("addTime").notNull();
    customer.addDateProperty("editTime");

    Entity orderInfo = schema.addEntity("LogisticsOrder");
    orderInfo.implementsSerializable();
    orderInfo.addStringProperty("ordered").primaryKey(); //订单编号
    orderInfo.addStringProperty("dispatchNumber").notNull(); //调度单号
    orderInfo.addStringProperty("dPdtgDate").notNull(); //调度时间
    orderInfo.addStringProperty("lPdtgBatch").notNull(); //批次
    orderInfo.addStringProperty("lPdtgMerdcategOldkey").notNull(); //冷藏类型
    orderInfo.addIntProperty("boxNumber").notNull(); //箱数
    orderInfo.addStringProperty("pathName").notNull(); //线路
    orderInfo.addStringProperty("orderStatus").notNull(); //订单状态
    orderInfo.addStringProperty("evaluation"); //用户评价值
    orderInfo.addStringProperty("cooperateID").notNull(); //客户ID
    orderInfo.addDateProperty("addTime").notNull();
    orderInfo.addDateProperty("editTime");

    Entity orderDetail = schema.addEntity("OrderDetail");//订单详情
    orderDetail.implementsSerializable();
    orderDetail.addStringProperty("goodsId").notNull();//货品ID
    orderDetail.addStringProperty("ordered").notNull();//订单ID
    orderDetail.addStringProperty("goodsName").notNull();//品名
    orderDetail.addStringProperty("dispatchType").notNull();//冷藏类型
    orderDetail.addStringProperty("iGroiValunum").notNull(); //件数
    orderDetail.addStringProperty("lpn").primaryKey(); //箱号条码
    orderDetail.addStringProperty("detailStatus").notNull();
    orderDetail.addDateProperty("addTime").notNull();
    orderDetail.addDateProperty("editTime");

    Entity recycleInput = schema.addEntity("RecycleInput"); //回收录入信息
    recycleInput.addIdProperty();
    recycleInput.addIntProperty("recycleNum").notNull();
    recycleInput.addDateProperty("recycleTime").notNull();//回收录入时间
    recycleInput.addDateProperty("editTime");

    Entity recycleScan = schema.addEntity("RecycleScan"); //回收扫描信息
    recycleScan.addIdProperty();
    recycleScan.addDateProperty("recycleScanTime").notNull();
    recycleScan.addStringProperty("scanCode").notNull();
    recycleScan.addDateProperty("editTime");
  }
}
