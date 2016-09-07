package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javabeans.ServletMapper;

import org.junit.Test;

public class ExecProperties {
	@Test
	public void init(){
		String basePath = Config.getConfig().getProperty("path");
		System.out.println(basePath);
		
		
	}
}
