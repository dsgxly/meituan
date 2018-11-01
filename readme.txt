项目开发采用四层架构
view        --> 视图层
controller  --> 控制层，接收前端的输入，并调用service层
service     --> 业务逻辑层，处理业务逻辑，并调用dao层
dao         --> 持久层，操作数据库   Mybatis框架
接口隔离原则：-->扩展功能
 Dao接口和Dao的实现类
 service接口和service的实现类

vo  -->  view object (value object)

db --> pojo(时间戳1234564689) --> vo --> yyyy-MM-dd hh:mm:ss