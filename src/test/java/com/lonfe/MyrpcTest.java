package com.lonfe;import com.lonfe.service.PersonService;import org.junit.Test;import org.springframework.context.ApplicationContext;import org.springframework.context.support.ClassPathXmlApplicationContext;import java.util.List;public class MyrpcTest {	@Test	public void test() {		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");		PersonService service = (PersonService) context.getBean("person");		List<String> interestList = service.getInterestList("lonfe");		interestList.forEach(System.out::println);	}}