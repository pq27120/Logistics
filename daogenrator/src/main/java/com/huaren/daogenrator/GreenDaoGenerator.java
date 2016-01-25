package com.huaren.daogenrator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

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
    new DaoGenerator().generateAll(schema,
        "./daogenrator/src/main/java");
  }

  /**
   * @param schema
   */
  private static void addTable(Schema schema) {
    // 一个实体（类）就关联到数据库中的一张表，此处表名为「Customer」（既类名）
    Entity customer = schema.addEntity("Customer");
    // 你也可以重新给表命名
    // note.setTableName("NODE");
    customer.implementsSerializable();
    // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
    // 接下来你便可以设置表中的字段：
    customer.addIdProperty();
    customer.addStringProperty("code").notNull();
    // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
    // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
    customer.addStringProperty("name").notNull();
    customer.addStringProperty("address");

    Entity goodsInfo = schema.addEntity("Goods");
    goodsInfo.implementsSerializable();
    goodsInfo.addIdProperty().primaryKey();
    goodsInfo.addStringProperty("goodsName").notNull();
    goodsInfo.addBooleanProperty("isLoad").notNull();
    goodsInfo.addBooleanProperty("isRemove").notNull();
    goodsInfo.addStringProperty("barCode").notNull();
    Property customerId = goodsInfo.addLongProperty("customerId").getProperty();
    goodsInfo.addToOne(customer, customerId);

    ToMany addToMany = customer.addToMany(goodsInfo, customerId);
    addToMany.setName("goods");

  }
}
