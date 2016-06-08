package FuncTest;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import domains.Cell;

public class ValidateTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();  
        Validator validator = factory.getValidator();  
        
        
		Cell cell = new	Cell("22221","3223","dd", "66", "44", "22",33.5, 66.5);
		Set<ConstraintViolation<Cell>> constraintViolations =validator.validate(cell);
		System.out.println(constraintViolations.size());
		//通过判断size()值，确定是否出错，把出错信息返回到前端
		for (ConstraintViolation<Cell> constraintViolation : constraintViolations) {  
	        System.out.println("对象属性:"+constraintViolation.getPropertyPath());  
	        System.out.println("国际化key:"+constraintViolation.getMessageTemplate());  
	        System.out.println("错误信息:"+constraintViolation.getMessage());  
	    }  
	
	}

}
