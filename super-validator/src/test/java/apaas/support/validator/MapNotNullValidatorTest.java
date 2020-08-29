package apaas.support.validator;


import javax.validation.ConstraintDeclarationException;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.springframework.util.Assert;

public class MapNotNullValidatorTest {
	
	@Test
	public void testAdd() {
		MapNotNullValidator validator = new MapNotNullValidator();
		validator.init("key1.key2");
		Assert.isTrue( ( occurException(()-> validator.isValid("{\"a\":\"\"}", null),ConstraintDeclarationException.class)) );
		String s = " {\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":\"not null\"\n" + 
				"    }\n" + 
				"} ";
	
		Assert.isTrue(validator.isValid( s,null));
		String s1 = "{\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":null\n" + 
				"    }\n" + 
				"}";
		Assert.isTrue( ( occurException(()-> validator.isValid(s1, null),ConstraintDeclarationException.class)) );
		String s2 = "[{\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":null\n" + 
				"    }\n" + 
				"}\n" + 
				"]";
		Assert.isTrue( ( occurException(()-> validator.isValid(s2, null),ConstraintDeclarationException.class)) );

		String s3 = "[{\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":1\n" + 
				"    }\n" + 
				"},{\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":1\n" + 
				"    }\n" + 
				"}\n" + 
				"]";
		Assert.isTrue( validator.isValid( s3,null));
		String s4 = "";
		Assert.isTrue( ( occurException(()-> validator.isValid(s4, null),ConstraintDeclarationException.class)) );
		String s5 = "dsa";
		Assert.isTrue( ( occurException(()-> validator.isValid(s4, null),ConstraintDeclarationException.class)) );
	}
	
	@Test
	public void testEmpty() {
		MapNotNullValidator validator = new MapNotNullValidator();
		validator.init("");
		Assert.isTrue( validator.isValid("", null));
		Assert.isTrue( ( occurException(()-> validator.isValid(null, null),ConstraintDeclarationException.class)) );
	}
	@Test
	public void testArray() {
		MapNotNullValidator validator = new MapNotNullValidator();
		validator.init("key1.key2");
		String[] objs = Arrays.array( "{\n" + 
				"    \"key1\":{\n" + 
				"        \"key3\":\"1\",\n" + 
				"        \"key2\":\"notnull\"\n" + 
				"    }\n" + 
				"}","{\n" + 
						"    \"key1\":{\n" + 
						"        \"key3\":\"1\",\n" + 
						"        \"key2\":\"notnull\"\n" + 
						"    }\n" + 
						"}" );
		Assert.isTrue( validator.isValid(objs, null));
	}
	
	
	private <T extends Exception> boolean occurException( Runnable func , Class<T > clazz  ) {
		try {
			func.run();
		}catch(Exception e) {
			if(clazz.isInstance(e)) {
				return true;
			}
		}
		return false;
		
	}
}
