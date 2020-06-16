## 组员分工：

- 「**组长**」何飘
- 「**Axure UI 设计**」林浩仪
- 「**前端**」唐梦予
- 「**后端**」秦先富 & 宋博宇

# 社区综合治理系统 - 前端

> 本项目基于Vue.js 2.x + Vue-Router + Vuex + SCSS + Element-UI构建
> 
> 四川师范大学 2017 级 4 班软件工程课程大作业「Web 系统开发」
> 
> ​											成员：唐梦予、林浩怡

# 社区综合治理系统 - 后端

> 本项项目基于SpringBoot、Shiro、Mybatis、Log4j、Docker、Redis进行构建
>
> 四川师范大学 2017 级 4 班软件工程课程大作业「后台 系统开发」
>
> ​											成员：何飘、秦先富、宋博宇

1. ### SpringBoot

   * Mvc设计模式：controller、service、serviceImpl（有更好的代码规范）

   * 注解：@Autowired、@Resource相同点和不同点（Java bean三种注入方式）

   * application.properties和application.yml配置文件使用体验

   * 不再是傻乎乎每次新建项目都在pom.xml文件里添加阿里云镜像

   * 学会从External Libraries中了解所加入的项目依赖（管理冲突的依赖）

   * URL中PathVariable类型（数字、字母、符号）判断对数据库安全操作十分重要

   * 使用jasypt对数据库password加密，在测试类中测试确保加密后的密码没有问题

   * 更加深化了我对Java面向对象的理解

     

2. ## Shiro

   * 认识authc、anon、perms、role授权管理方式
   * 了解认证、授权代码执行流程
   * 后台获取Cookie或Authorization中JSESSIONID
   * 不同域名下后台实现跨域配置
   * 了解了session会话管理

   

3. ### Mybatis

   * Jpa过渡到Mybatis，为什么要使用Mybatis？

   * 使用Mybatis注解+Sql语句实现数据库操作

   * 在resource下新建Mapper.xml,为什么我们最终选择这种方式？

   * 实现Mybatis的动态Sql所运用的标签

   * returnType与returnMap你真的了解多少？

   * parameterType是不是每次有参数传入都必须用？

   * 你怎样传多个数量可变的参数，并且使用一条Sql将所有可变参数修改或插入

   * Mybatis中简单的二级缓存设置（*.xml和application.yml）

   * Mybatis配合Database插件，快速修改数据库数据、编写Sql

   * 在因外键而产生关联的几张表中，你的实体类改如何建？returnMap中成员类的子标签应该使用什么

     

   * ```xml
     <mapper namespace="com.example.csgs.mapper.UserMapper"> //它代表着什么
     ```

   

4. ### Log4j

   * ```java
     //使用Lombok注解@Log4j完全可以省略下方代码
     private static Logger logger = Logger.getLogger(Test.class);
     ```

   * log4j.properties创建，规定日志输出地点及类型

   * 在application.yml中将Mybatis的log-impl替换成Log4j

   * 在你需要打印INFO的地方，使用@Log4j注解，快速实现日志输出

     

5. ### Docker

   * 从手动上传jar包构建镜像到docker-plugin一键搞定，这期间我到底经历了什么？

   * 将容器中的log映射到Host：-v /web_log:/log

   * 在Dockerfile中配置容器时区配置

     ```dockerfile
     FROM java:8
     
     VOLUME /tmp
     
     COPY csgs-0.0.1-SNAPSHOT.jar csgs.jar
     
     ENV TZ=Asia/Shanghai
     
     RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone
     
     ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/csgs.jar"]
     ```

6. ### Redis

   * 用Docker直接拉一个Redis的images，run起来，it's over！！！
   * 用来存sessionId和token，这些访问频繁、单条信息量不大的内容
   
7. ## ElasticSearch（version：7.5.1）

   1. #### ElasticSearch数据导入

      * 使用elastic官方推荐工具logstash，在config目录下修改logstash-sample.conf

      ```properties
      input {
        jdbc {
          jdbc_driver_library => "/Users/hepiao/Downloads/logstash-7.5.1/mysql-connector-java-8.0.16.jar"
          #这里连接mysql可以直接在Oracle官网下载驱动，或者下载驱动插件并安装
          jdbc_driver_class => "com.mysql.cj.jdbc.Driver"
          #MysqL数据库版本不一样，driver_class也是有区别的（主要区别在于8.X以上的版本）
          jdbc_connection_string => "jdbc:mysql://112.126.85.20:5306/csgs"
          jdbc_user => "root"
          jdbc_password => "123456"
          schedule => "* * * * *"
          clean_run => true
          statement => "SELECT * FROM journal"
        }
      }
      
      output {
        elasticsearch {
          hosts => ["https://es-ppulc3rx.public.tencentelasticsearch.com:9200"]
          index => "journal"
          document_id => "%{id}"
          user => "elastic"
          password => "Joson123"
        }
      }
      ```

      * 在bin目录下用logstash运行config目录下的logstash-sample.conf配置文件

        ```shell
        ./logstash -f ../config/logstash-sample.conf
        ```

        > 在logstash-sample.conf文件中这段代码很重要：
        >
        > ```properties
        > schedule => "* * * * *"//logstash将会隔一段时间同步一次数据库中所要导入的数据，也就是我们的数据库几乎跟es中的数据时刻保持一致
        > ```

      * **坑**：

        > * 我们事先没有在es中建立相应索引，导入MySQL中的数据时候，会利用配置文件里所指定的索引名自行创建
        >
        > * 单纯通过这种方式向es中导入数据，数据内容本身没有问题，但是在后续的查询中会出较大的问题，因为字段类型除了数字、日期以外，其它字段类型都会默认是keyword。我们在查询字段类型为keyword时候，是不能实现倒排索引、模糊查询的（类型必须是text），当然后续的高亮也就不能实现。这里正确的做法是：先利用Kibana在其Console窗口创建索引，并设置mapping（相当于数据库中的建表操作，把需要进行模糊查询的字段类型设置成text）,然后进行数据库数据同步。

   2. #### 客户端（SpringBoot）对ElasticSearch数据的操作

      * 配置Bean（相当于客户端与服务端建立连接）

        ```java
        public RestHighLevelClient restHighLevelClient(){
                BasicConfigurator.configure();
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials("elastic","Joson123"));
        
                RestClientBuilder builder = RestClient.builder(
                        new HttpHost("es-ppulc3rx.public.tencentelasticsearch.com", 9200,"https"))
                        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                            }
                        });
        
                return new RestHighLevelClient(builder);
            }
        ```

      * 数据操作（增删改查）

        * 添加索引

          ```java
             /**
              * 添加索引
              */
             public void contextLoads() throws IOException {
                CreateIndexRequest request = new CreateIndexRequest("journal");
                CreateIndexResponse createIndexResponse = client
                  .indices().create(request, RequestOptions.DEFAULT);
                System.out.println(createIndexResponse);
              }
          ```

        * 查询索引是否存在

          ```java
             /**
              * 查询索引是否存在
              */
             public void existIndex() throws IOException {
                GetIndexRequest request = new GetIndexRequest("journal");
                boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
                System.out.println(exists);
              }
          ```

        * 删除索引

          ```java
             /**
              * 删除索引
              */
              public void deleteIndex() throws IOException {
                DeleteIndexRequest request = new DeleteIndexRequest("journal");
                AcknowledgedResponse delete = client.indices().delete(request, 					RequestOptions.DEFAULT);
                System.out.println(delete.isAcknowledged());
              }
          ```

        * 添加文档（也就是es从数据库中导入的数据）

          ```java
          	/**
               * 添加文档
               */
              public void addDocument(String indexName, Object object) throws IOException {
                  IndexRequest request = new IndexRequest(indexName);
                  request.id("1");
                  request.timeout(TimeValue.timeValueSeconds(1));
                  request.timeout("1s");
          
                  request.source(JSON.toJSONString(object), XContentType.JSON);
          
                  IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
                  System.out.println(indexResponse.toString());
                  System.out.println(indexResponse.status());
              }
          ```

        * 获取文档

          ```java
          		/**
               * 获取文档
               */
              public void getDocument(String indexName, String id) throws IOException {
                  GetRequest getRequest = new GetRequest(indexName, id);
                  GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
                  System.out.println(getResponse.getSourceAsString());
                  System.out.println(getResponse);
              }
          ```

        * 更新文档

          ```java
          		/**
               * 更新文档
               */
              public void updateDocument(Object object, String indexName, String id) throws IOException {
                  UpdateRequest updateRequest = new UpdateRequest(indexName, id);
                  updateRequest.timeout("1s");
          
                  updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);
          
                  UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
                  System.out.println(updateResponse.status());
              }
          ```

        * 删除文档

          ```java
          		/**
               * 删除文档
               */
              public void deleteDocument(String indexName, String id) throws IOException {
                  DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
                  deleteRequest.timeout("1s");
          
                  DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
                  System.out.println(deleteResponse.status());
              }
          ```

        * 批量插入文档

          ```java
          		/**
               * 批量插入文档
               */
              public <T> void bulkRequest(ArrayList<T> dataList, String indexName) throws IOException {
                  BulkRequest bulkRequest = new BulkRequest();
                  bulkRequest.timeout("10s");
          
                  for (int i = 0; i < dataList.size(); i++) {
                      bulkRequest.add(new IndexRequest(indexName).
                              id("" + (i + 1)).//注意id的起始值
                              source(JSON.toJSONString(dataList.get(i)), XContentType.JSON)
                      );
                  }
          
                  BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                  System.out.println(!bulkResponse.hasFailures());
              }
          ```

        * 索引中doc数量查询

          ```java
          		public CountResponse countQuery(String indexName) throws IOException {
                  CountRequest countRequest = new CountRequest(indexName);
                  return client.count(countRequest, RequestOptions.DEFAULT);
              }
          ```

        * 独立出的方法（后面代码中会用到）

          ```java
          		private void buildSearchRequest() {
                  sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
                  searchRequest.source(sourceBuilder);
              }
          
              private void createSearchObject(String indexName) {
                  searchRequest = new SearchRequest(indexName);
                  sourceBuilder = new SearchSourceBuilder();
              }
          ```

        * 文档精确查询

          ```java
          		/**
               * 文档精确查询
               */
              public SearchResponse termQuery(String indexName, TermQueryBuilder termQueryBuilder, String page) throws IOException {
                  createSearchObject(indexName);
                  sourceBuilder.from((Integer.parseInt(page) - 1) * 20);
                  sourceBuilder.size(20);
                  sourceBuilder.query(termQueryBuilder);
                  buildSearchRequest();
                  return client.search(searchRequest, RequestOptions.DEFAULT);
              }
          	 /**
          		* 其余的TermsQueryBuilder、MultiMatchQueryBuilder、matchAllQuery都是类似的操作
          		*/
          ```

   3. #### ElasticSearch落地于SpringBoot业务

      ```java
      @Service
      public class LuceneServiceImpl implements LuceneService {
          @Resource
          RestHighLevelClient client;
          private SearchRequest searchRequest;
          private SearchSourceBuilder sourceBuilder;
      
          /**
           * 在ElasticSearch中复合查询
           * @param indexName 索引名
           * @param matchQueryBuilder 复合查询条件
           * @param page 页数
           * @return
           * @throws IOException
           */
          @Override
          public List<Map<String, Object>> multiMatchQuery(String indexName, MultiMatchQueryBuilder matchQueryBuilder,
                                                           String page) throws IOException {
              createSearchObject(indexName);
              sourceBuilder.from((Integer.parseInt(page) - 1) * 15);
              sourceBuilder.size(15);
              sourceBuilder.query(matchQueryBuilder);
              buildSearchRequest(); //构建SearchRequest
              setKeywordHighlight();//设置相应字段高亮
              SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
              ArrayList<Map<String, Object>> list = null;
              if (searchResponse.getHits().getHits().length != 0) {
                  list = new ArrayList<>();
                  for (SearchHit documentFields : searchResponse.getHits().getHits()) {
                      //替换成高亮结果
                      analysisHighlight(documentFields,"content");
                      analysisHighlight(documentFields,"title_name");
                      list.add(documentFields.getSourceAsMap());
                  }
              }
              return list;
          }
      
          private void analysisHighlight(SearchHit documentFields,String fieldName) {
              Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
              HighlightField field = highlightFields.get(fieldName);
              Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
              if (field != null) {
                  Text[] fragments = field.fragments();
                  StringBuilder newField = new StringBuilder();
                  for (Text fragment : fragments) {
                      newField.append(fragment);
                  }
                  sourceAsMap.put(fieldName,newField);
              }
          }
      
          private void setKeywordHighlight() {
              HighlightBuilder highlightBuilder = new HighlightBuilder();
              highlightBuilder.field("title_name");
              highlightBuilder.field("content");
              highlightBuilder.requireFieldMatch(false);
              highlightBuilder.preTags("<span style='color:red'>");
              highlightBuilder.postTags("</span>");
              sourceBuilder.highlighter(highlightBuilder);
          }
      
          private void buildSearchRequest() {
              sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
              searchRequest.source(sourceBuilder);
          }
      
          private void createSearchObject(String indexName) {
              searchRequest = new SearchRequest(indexName);
              sourceBuilder = new SearchSourceBuilder();
          }
      }
      ```

      * 业务Service实现说明

        > （查询操作之前已经进行了说明，这里就跳过查询）
        >
        > * 高亮：主要对项目的两张表（Journal、Announcement）进行全文搜索（其余表搜索意义不大，实现方式都一样的），对title_name、content字段进行高亮处理，同时这两字段也是全文搜索中主要被查询的字段。代码逻辑：在进行search之前告诉sourceBuilder哪些字段需要被高亮，然后再查询返回的数据中提取出事先规定可能被高亮的字段信息，替换掉Map中没有被高亮的相同数据。前端得到后端处理后的数据，用Vue渲染出高亮效果。
        > * 分页：ElasticSearch中查询数据时，每次查询默认最多显示10条数据，也就是当你查出的数据本应该是多余10条的，但你最多只能看到10条。当你的业务需求一页想显示更多数据时，可以手动修改。本段代码，我利用前端需求（传入的page），来选择搜索数据范围（『.from((Integer.parseInt(page) - 1) * 15)』、『.size(15)』）。

   4. #### 总结（待完善）

      * ElasticSearch集群搭建，目前所使用的是腾讯云ElasticSearch Service免费版，服务器只有两个节点，与实际开发还是有很大差距，我个人觉得使用Docker搭建ElasticSearch集群非常不错（目前Docker可以说已经是后端开发者必备的一项技能），成本低、效率高，它能不香吗？

        > 原因：我自己的服务器配置较低，不足以建议三个以上的Es集群，有钱了买个好的服务器再弄了。

      * 在Docker运行Logstash，实时数据库和Es数据实时同步。虽然本地也能运行Logstash，但是你一旦关闭了Dos窗口或者电脑关机，就不能继续同步数据，这样显然这不是最优的办法。

        > 原因：自己时间比较紧张、有一些问题也还没想明白（这个Logstash镜像在Docker中运行与其它镜像run还是有一些区别的，卷轴映射和配置文件添加等）

   


