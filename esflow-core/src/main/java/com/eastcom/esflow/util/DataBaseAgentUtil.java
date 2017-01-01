package com.eastcom.esflow.util;

import com.eastcom.esflow.bean.Dict;


public class DataBaseAgentUtil {
	
	
	public static final String NAMESPACE_DICT_DATA = "DICT";
	
	private static java.util.Random chartRandom = new java.util.Random();

	/**
	 * 根据模型生成表名<br>
	 * 表名格式为：T_配置模型_表类型 如：服务器 T_FWQ_TRUNK(基线表)<br>
	 * 1. 配置模型按循序取值：简拼+3随机数<br>
	 * 2. 如果转换拼音失败使用随机3个字符代替<br>
	 * 3. 如果字母超过12时进行截取前9个字符+3个随机数
	 * 
	 * @param configBean
	 *            模型对象
	 * @return 表名
	 * @throws Exception
	 * @throws MethodNotImplemented
	 */
	public static String builTableName() {

		String name = "DICT_" + getRandomString() + "_" + getRandomNumber();
		return name;
	}

	/**
	 * 产生随机字母3个
	 * 
	 * @return
	 */
	private static String getRandomString() {
		String temp = "";
		for (int j = 0; j < 4; j++) {
			int i = chartRandom.nextInt(25);
			i += 65;
			i = i < 65 ? 65 : (i > 90 ? 90 : i);
			temp = temp + (char) i;
		}
		return temp;
	}

	/**
	 * 产生随机数字3个
	 * 
	 * @return
	 */
	private static String getRandomNumber() {
		int inc = 0;
		while (inc < 1000) {
			inc = chartRandom.nextInt(9999);
		}
		String temp = "" + inc;
		temp = temp.length() == 1 ? "000" + temp : (temp.length() == 2 ? "00"
				+ temp : temp);
		return temp;
	}
	
	public static String getDictTblName(Dict dict){
		return NAMESPACE_DICT_DATA+"."+dict.getTblName();
	}
}
