package com.overthinker.cloud.ai.clientTools;

import io.swagger.v3.oas.annotations.Parameter;
//import org.springframework.ai.chat.annotation.Tool;
import org.springframework.ai.tool.annotation.Tool;
import reactor.core.publisher.Mono;

public class UserTools {

//    private final R2dbcDatabaseClient databaseClient; // R2DBC 客户端
//
//    public UserTools(R2dbcDatabaseClient databaseClient) {
//        this.databaseClient = databaseClient;
//    }
//
//    @Tool(description = "插入用户信息到数据库")
//    public Mono<String> insertUser(
//            @Parameter(name = "name", description = "用户姓名") String name,
//            @Parameter(name = "age", description = "用户年龄") Integer age,
//            @Parameter(name = "phoneNumber", description = "用户手机号码") String phoneNumber,
//            @Parameter(name = "gender", description = "用户性别 (M/F/O)") String gender
//    ) {
//        return databaseClient.sql("INSERT INTO cloud-ai.users (name, age, phone_number, gender) VALUES (?, ?, ?, ?)")
//                .bind(0, name)
//                .bind(1, age)
//                .bind(2, phoneNumber)
//                .bind(3, gender)
//                .fetch()
//                .rowsUpdated()
//                .map(rows -> "成功插入用户: " + name);
//    }
}