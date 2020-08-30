package apaas.support.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import apaas.support.annotion.MapNotNull;

public abstract class AbstractValidator<T extends Annotation> implements ConstraintValidator<T, Object> {
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		for (String[] path : paths) {
			if (!this.valid(value, path, 0)) {
				return false; //
			}
		}
		return true;
	}

	private String[] valids;
	private String[][] paths = null;
	

	public void init(String... validPahts) {
		valids = validPahts;
		if (valids == null || valids.length == 0) {
			return;
		} else {
			paths = new String[valids.length][];
			for (int i = 0; i < valids.length; i++) {
				if (isBlank(valids[i])) {
					paths[i] = new String[] {};
				} else {
					paths[i] = valids[i].split("\\.");
				}
			}
		}
	}

	private boolean valid(String value, String[] path, int index) {
		ObjectMapper mapper = new ObjectMapper();
		if (index >= path.length) {
			return this.conditionWhen(value,path);
		} else {
			if(value==null) {
				notDeepEnough(path,index);
			}
			if(this.isBlank(path[index])) {
				return this.valid(value, path, index+1);
			}
			try {
				Map map = mapper.readValue(value, Map.class);
				return this.valid(map, path, index);
			} catch (JsonMappingException e) {
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			try {
				List list = mapper.readValue(value, List.class);
				return this.valid(list, path, index);
			} catch (JsonProcessingException e) {
			}
			
			notDeepEnough(path,index);
			return false;
		}
	}

	private boolean valid(Map value, String[] path, int index) {
		if (index >= path.length) {
			return this.conditionWhen(value,path);
		} else {
			if(value==null) {
				notDeepEnough(path,index);
			}
			String node = path[index];
			if (!this.isBlank(node)) {
				return this.valid(value.get(node), path, index+1);
			}else {
				return this.valid(value, path, index+1);
			}
		}
	}

	private boolean valid(Collection values, String[] path, int index) {
		for(Object obj : values) {
			if(!this.valid(obj, path, index)) {
				return false; 
			}
		}
		return true;
	}
	private boolean valid(Object[] values , String[] path , int index) {
		for(Object obj : values) {
			if(!this.valid(obj, path, index)) {
				return false; 
			}
		}
		return true;
	}
	
	private boolean valid(Object obj, String[] path, int index) {
		if (index >= path.length) {
			return this.conditionWhen(obj,path);
		} else {
			if(obj==null) {
				notDeepEnough(path,index);
			}
			if(this.isBlank(path[index])){
				return this.valid(obj,path,index+1);
			}
			if (obj instanceof String) {
				return this.valid((String) obj, path, index);
			}
			if (obj instanceof Map) {
				return this.valid((Map) obj, path, index);
			}
			if (obj instanceof Collection) {
				return this.valid((Collection) obj, path, index);
			}
			if(obj instanceof Object[]) {
				return this.valid((Object[]) obj, path, index);
			}
			// other
			if((obj = getField(obj,path[index]))==null){
				notDeepEnough(path,index);
				return false;
			}else {
				return this.valid(obj, path, index+1);
			}
		}
	}
	protected Object getField(Object obj,String field) {
		
		try {
			Object rs =  PropertyUtils.getProperty(obj, field);
			return rs;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			return null;
		}
		
	}
	private boolean isBlank(String s) {
		return null == s || "" == s || "".equals(s);
	}
	
	
	/**
	 * 判断该对象是否是否满足条件
	 * @param obj 对象
	 * @param path 最终的路径
	 * @return true
	 * @throws ConstraintDeclarationException
	 */
	private boolean conditionWhen(Object obj,String[] path)throws ConstraintDeclarationException {
		if(condition(obj)) {
			return true;	
		}else{
			StringBuilder builder =  new StringBuilder();
			for( int i = 0;i<path.length;i++) {
				builder.append(path[i]);
				if(i!=path.length-1) {
					builder.append(".");
				}
			}
			String msgPath = builder.toString();
			extendExceptionMessage(msgPath);
			defaultExceptionMessage(msgPath);
			return false;
		}
		
	}

	private void notDeepEnough(String[] path, int index) throws ConstraintDeclarationException{
		StringBuilder builder =  new StringBuilder();
		for( int i = 0;i<path.length;i++) {
			builder.append(path[i]);
			if(i!=path.length-1) {
				builder.append(".");
			}
		}
		String msgPath = builder.toString();
		extendExceptionMessage(msgPath);
		defaultExceptionMessage(msgPath);
	}
	private void defaultExceptionMessage(String path)throws ConstraintDeclarationException{
		throw new ConstraintDeclarationException("deep not enough:"+path );
	}
	protected void extendExceptionMessage(String path)throws ConstraintDeclarationException {
		
	}
	abstract protected boolean condition(Object obj);
	
	
}
