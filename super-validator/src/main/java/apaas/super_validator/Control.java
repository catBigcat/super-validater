package apaas.super_validator;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import apaas.support.annotion.MapNotNull;


@Controller("/")
@Validated
public class Control {
	@PostMapping("say")
	@ResponseBody
	public String index(@RequestBody  
			@MapNotNull( {"key.key2"})
			String body
			) {
		return "index";
	}

}
