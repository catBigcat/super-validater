package apaas.support.validator;

import org.springframework.util.StringUtils;

import apaas.support.annotion.MapNotNull;
import apaas.support.annotion.MapStringNotNull;

public class MapStringNotNUllValidator extends AbstractValidator<MapStringNotNull>{
	
	@Override
	public void initialize(MapStringNotNull constraintAnnotation) {
		String[] valids = constraintAnnotation.value();
		super.init(valids);
	}
	@Override
	protected boolean condition(Object obj) {
		if(obj instanceof String) {
			return obj!=null && "".equals(obj);
		}
		return false;
	}

}
