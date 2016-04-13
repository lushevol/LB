package org.kylin.klb.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.util.Utils;

public class UpgradeService extends KlbClient {

	public byte[] preUpgrade(File upgrade) throws Exception {
		if (upgrade == null) {
			return null;
		}
		FileInputStream fr = new FileInputStream(upgrade);
		int length = 0;
		while(fr.read() != -1){
			length ++;
		}
		fr = new FileInputStream(upgrade);
		byte[] binary = new byte[length];
		fr.read(binary);
		return binary;
	}
	public String upgrade(File upgrade) {
		byte[] binaryUpload = null;
		try {
			byte[] binary = preUpgrade(upgrade);
			if(binary == null){
				return "导入文件不能为空";
			} else {
				binaryUpload = binary;
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		/* if(!reset){
			return "初始化失败";
		} */		
		/* if(!upload){
			return "升级文件导入失败";
		} */		
		/* if ( start == null || start != true ) {
			return "上传升级文件验证有误,请检查升级文件";
		} */
		
		String flagStr = "false";
		try {			
			Boolean reset = (Boolean)executeXml("Updater.Reset");
			Boolean upload = (Boolean)executeXml("Updater.Upload", binaryUpload);
			Boolean start = (Boolean)executeXml("Updater.Start");
			flagStr = "true";			
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;						
	}
}
