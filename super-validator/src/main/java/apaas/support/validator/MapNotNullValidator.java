package apaas.support.validator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.Enhancer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class MapNotNullValidator implements ConstraintValidator<MapNotNull, Object> {
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
	
	@Override
	public void initialize(MapNotNull constraintAnnotation) {
		valids = constraintAnnotation.value();
		this.init(valids);
	}
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
			return this.condition(value,path);
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
			return this.condition(value,path);
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
			return this.condition(obj,path);
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
			return (obj = getField(obj,path[index]))==null
					? false : this.valid(obj, path, index+1);			
		}
	}
	protected Object getField(Object obj,String field) {
		// 不验证 对象
		return null;
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
	private boolean condition(Object obj,String[] path)throws ConstraintDeclarationException {
		if( obj==null) {
			StringBuilder builder = new StringBuilder();
			builder.append("path : ");
			for(int i=0;i<path.length;i++) {
				builder.append( path[i] );
				if(i!=path.length-1)
					builder.append(".");
			}
			builder.append(" is null");
			throw new ConstraintDeclarationException(builder.toString());
		}else {
			return true;
		}
	}
	protected void notDeepEnough(String[] path, int index) throws ConstraintDeclarationException{
		StringBuilder builder = new StringBuilder();
		builder.append("deep not enough :");
		for(int i=0;i<index;i++) {
			builder.append( path[i] );
			if(i!=index) {
				builder.append(".");
			}
		}
		throw new ConstraintDeclarationException(builder.toString());
	}
	
}
