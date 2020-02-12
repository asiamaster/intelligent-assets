package com.dili.ia.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * @author haiping
 *
 */
public class PinyinUtil {

	/**
	 * 转换为全拼简写 如不能转换原样返回
	 * @param name
	 * @return
	 */
	public static String converterToFirstSpell(String name) {
		return convert(name,true);
	}

	/**
	 * 转换为全拼 如不能转换原样返回
	 * @param name
	 * @return
	 */
	public static String converterToSpell(String name) {
		return convert(name,false);
	}

	/**
	 * 将汉字转换为拼音或者拼音缩写
	 * @param name
	 * @param first
	 * @return
	 */
	private static String convert(String name,boolean first){
		StringBuffer buffer=new StringBuffer();
		if(name==null || "".equals(name.trim())){
			return "";
		}
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		char[] arr=name.toCharArray();
		for(int i=0;i<arr.length;i++){
			char c=arr[i];
			if(c>128){
				try{
					String[] strs=PinyinHelper.toHanyuPinyinStringArray(c,defaultFormat);
					if(strs==null || strs.length==0){
						continue;
					}
					//如果忽略多音字，则可以减少操作，默认取第一个就行
					if(first){
						buffer.append(strs[0].substring(0,1));
					}else{
						buffer.append(strs[0]);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				buffer.append(String.valueOf(c));
			}
		}
		return buffer.toString();
	}
}