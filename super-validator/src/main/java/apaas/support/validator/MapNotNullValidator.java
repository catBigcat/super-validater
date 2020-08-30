package apaas.support.validator;

import apaas.support.annotion.MapNotNull;

public class MapNotNullValidator extends AbstractValidator<MapNotNull>{
	@Override
	public void initialize(MapNotNull constraintAnnotation) {
		String[] valids = constraintAnnotation.value();
		super.init(valids);
	}
	protected boolean condition(Object obj){
		return obj!=null;
	}
	
}
