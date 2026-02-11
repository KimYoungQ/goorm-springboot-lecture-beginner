package com.study.my_spring_study_diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MySpringStudyDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringStudyDiaryApplication.class, args);

		// 등록된 모든 Bean 이름 출력
//		ApplicationContext context = SpringApplication.run(MySpringStudyDiaryApplication.class, args);

//		String[] beanNames = context.getBeanDefinitionNames();
//		System.out.println("========== 등록된 Bean 목록 ==========");
//		for (String beanName : beanNames) {
//			if (beanName.contains("studyLog")) {  // 우리가 만든 Bean만 필터링
//				System.out.println("✅ " + beanName);
//			}
//		}
//		System.out.println("=====================================");
	}

}
