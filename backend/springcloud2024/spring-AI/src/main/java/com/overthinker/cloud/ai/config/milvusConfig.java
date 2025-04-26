//package com.overthinker.cloud.ai.config;
//
//
//import io.milvus.client.MilvusServiceClient;
//import io.milvus.param.ConnectParam;
//import io.milvus.param.R;
//import io.milvus.param.collection.CreateCollectionParam;
//import io.milvus.param.collection.FieldType;
//import io.milvus.param.collection.HasCollectionParam;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class milvusConfig {
//
//    @Value("${spring.data.milvus.host}")
//    private String host;
//
//    @Value("${spring.data.milvus.port}")
//    private int port;
//
//    @Value("${spring.data.milvus.username}")
//    private String username;
//
//    @Value("${spring.data.milvus.password}")
//    private String password;
//
//    @Value("${spring.data.milvus.databaseName}")
//    private String databaseName;
//    @Value("${spring.data.milvus.collectionName}")
//    private String collectionName;
//
//    @Bean
//    public MilvusServiceClient milvusClient() {
//        ConnectParam connectParam = ConnectParam.newBuilder()
//                .withHost(host)
//                .withPort(port)
//                .withDatabaseName(databaseName)
//                .withAuthorization(username, password)
//                .build();
//        MilvusServiceClient milvusServiceClient = new MilvusServiceClient(connectParam);
//
//        HasCollectionParam build = HasCollectionParam.newBuilder()
//                .withCollectionName(collectionName)
//                .build();
//        R<Boolean> booleanR = milvusServiceClient.hasCollection(build);
//        System.out.println(booleanR.getMessage());
//
//        if (!booleanR.getData()) {
//            System.out.println("集合 " + collectionName + " 不存在，正在创建集合");
//            // 定义字段类型列表
//            java.util.List<FieldType> fieldTypes = new java.util.ArrayList<>();
//
//            // 定义主键字段
//            FieldType idField = FieldType.newBuilder()
//                    .withName("id")
//                    .withDataType(io.milvus.grpc.DataType.Int64)
//                    .withPrimaryKey(true)
//                    .withAutoID(true)
//                    .build();
//            fieldTypes.add(idField);
//
//            // 定义向量字段
//            FieldType vectorField = FieldType.newBuilder()
//                    .withName("vector")
//                    .withDataType(io.milvus.grpc.DataType.FloatVector)
//                    .withDimension(1536)  // 根据实际情况设置向量维度
//                    .build();
//            fieldTypes.add(vectorField);
//
//            // 创建集合参数
//            CreateCollectionParam createCollectionParam = CreateCollectionParam.newBuilder()
//                    .withCollectionName(collectionName)
//                    .withFieldTypes(fieldTypes)
//                    .build();
//
//            milvusServiceClient.createCollection(createCollectionParam);
//        }
//        return milvusServiceClient;
//    }
//}
