package com.overthinker.cloud.springStudy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StreamStudy {


    //    统计每门课的人数
    @Data
    class TestClass {
        private String name;
        private Long Count;
    }


    //    学生和选课
    @Data
    class Student {

        private Long id;
        private String name;
        private Integer age;
        private List<Schoolclass> schoolclasses;
    }

    //    学生和选课对应的关系表
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class StudentSchoolclass {
        private Long studentId;
        private Long schoolclassId;
        private Integer sort;

    }


    //    课表
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Schoolclass {
        private Long id;
        private String name;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class Schools {
        private String id;
        private String name;


        private static Schools changeSchoolclass(Schoolclass schoolclass) {
            return Schools.builder()
                    .id(schoolclass.getId().toString())
                    .name(schoolclass.getName()).build();
        }
    }


    public static void main(String[] args) {
//创建一个所有课程的list
        List<Schoolclass> school = new ArrayList<>(Arrays.asList(
                new Schoolclass(1L, "语文"),
                new Schoolclass(2L, "数学"),
                new Schoolclass(3L, "英语"),
                new Schoolclass(4L, "物理"),
                new Schoolclass(5L, "化学"),
                new Schoolclass(6L, "生物"),
                new Schoolclass(7L, "历史"),
                new Schoolclass(8L, "地理"),
                new Schoolclass(9L, "政治"),
                new Schoolclass(10L, "体育"),
                new Schoolclass(11L, "音乐"),
                new Schoolclass(12L, "美术"),
                new Schoolclass(13L, "计算机"),
                new Schoolclass(14L, "电子"),
                new Schoolclass(15L, "机械")
        ));
//        这个时候我要把Schoolclass转换成Schools类型
        List<Schools> schools = school.stream().map(s -> new Schools(s.getId().toString(), s.getName())).toList();
//        使用并行流来转换
        List<Schools> schools1 = school.parallelStream()
                .map(Schools::changeSchoolclass).toList();
        List<StudentSchoolclass> studentSchoolclasses = new ArrayList<>(Arrays.asList(
                new StudentSchoolclass(1L, 1L, 1),
                new StudentSchoolclass(1L, 2L, 2),
                new StudentSchoolclass(1L, 3L, 3),
                new StudentSchoolclass(1L, 4L, 4),
                new StudentSchoolclass(1L, 5L, 5),
                new StudentSchoolclass(1L, 6L, 6),
                new StudentSchoolclass(2L, 7L, 7),
                new StudentSchoolclass(2L, 8L, 8),
                new StudentSchoolclass(2L, 9L, 9),
                new StudentSchoolclass(2L, 10L, 1),
                new StudentSchoolclass(2L, 11L, 2),
                new StudentSchoolclass(2L, 12L, 3),
                new StudentSchoolclass(3L, 1L, 1),
                new StudentSchoolclass(3L, 2L, 2),
                new StudentSchoolclass(3L, 3L, 3),
                new StudentSchoolclass(3L, 4L, 4),
                new StudentSchoolclass(3L, 5L, 5),
                new StudentSchoolclass(3L, 6L, 6),
                new StudentSchoolclass(4L, 7L, 7),
                new StudentSchoolclass(4L, 8L, 8),
                new StudentSchoolclass(4L, 9L, 9),
                new StudentSchoolclass(4L, 10L, 10),
                new StudentSchoolclass(4L, 11L, 11),
                new StudentSchoolclass(4L, 12L, 12),
                new StudentSchoolclass(4L, 13L, 13),
                new StudentSchoolclass(4L, 14L, 14),
                new StudentSchoolclass(4L, 15L, 15),
                new StudentSchoolclass(5L, 1L, 1),
                new StudentSchoolclass(5L, 2L, 2),
                new StudentSchoolclass(5L, 3L, 3),
                new StudentSchoolclass(5L, 4L, 4),
                new StudentSchoolclass(5L, 5L, 5),
                new StudentSchoolclass(5L, 6L, 6),
                new StudentSchoolclass(6L, 7L, 7),
                new StudentSchoolclass(6L, 8L, 8),
                new StudentSchoolclass(6L, 9L, 9),
                new StudentSchoolclass(6L, 10L, 10),
                new StudentSchoolclass(6L, 11L, 11),
                new StudentSchoolclass(6L, 12L, 12),
                new StudentSchoolclass(6L, 13L, 13),
                new StudentSchoolclass(6L, 14L, 14),
                new StudentSchoolclass(6L, 15L, 15)
        )
        );


        List<Student> students = new ArrayList<>();
//        现在需要用stream流来把根据studentSchoolclasses 把学生和课程关联起来
//        然后把学生和课程的关系放到student的schoolclasses中
//        最后把学生和课程的关系放到school中
//        最后把school中的课程人数放到TestClass中

//        先来点简单的统计又多少们课程
        long q = school.size();
        System.out.println(q);

//        查询是否有一门课叫做数学
        school.stream().filter(s -> s.getName().equals("数学")).findFirst().ifPresent(System.out::println);
        school.stream();
        studentSchoolclasses.stream().map(StudentSchoolclass::getSchoolclassId);


        // 1. 流的创建

        // 1.1 从集合创建流

        // 1.2 从数组创建流
        // 1.3 从文件创建流
        // 1.4 从管道创建流
        // 1.5 从随机数创建流

        // 2. 流的操作
        // 2.1 流的中间操作
        // 2.2 流的终止操作
        // 2.3 流的短路操作
        // 2.4 流的并行操作
        // 2.5 流的异常处理
        // 2.6 流的调试
        // 2.7 流的性能优化
        // 2.8 流的最佳实践

        // 3. 流的应用
        // 3.1 流的应用场景
        // 3.2 流的应用示例
        // 3.3 流的应用案例
        // 3.4 流的应用注意事项
        // 3.5 流的应用最佳实践

        // 4. 流的注意事项
        // 4.1 流的注意事项
        // 4.2 流的注意事项
        // 4.3 流的注意事项
        // 4.4 流的注意事项
        // 4.5 流的注意事项

        // 5. 流的最佳实践
        // 5.1 流的最佳实践
        // 5.2 流的最佳实践
        // 5.3 流的最佳实践
        // 5.4 流的最佳实践
        // 5.5 流的最佳实践
        // 6. 流的总结
        // 6.1 流的总结
        // 6.2 流的总结
        // 6.3 流的总结
        // 6.4 流的总结
        // 6.5 流的总结

        // 7. 流的扩展
        // 7.1 流的扩展
        // 7.2 流的扩展
        // 7.3 流的扩展
        // 7.4 流的扩展
        // 7.5 流的扩展
        // 8. 流的扩展
        // 8.1 流的扩展
        // 8.2 流的扩展
        // 8.3 流的扩展
        // 8.4 流的扩展
        // 8.5 流的扩展

        // 9. 流的扩展

        // 10. 流的扩展


    }
}
