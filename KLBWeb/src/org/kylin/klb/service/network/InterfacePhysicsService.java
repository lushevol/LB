package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kylin.klb.assistant.InterfaceAssitant;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.entity.network.SimpleInterfaceInfo;

public class InterfacePhysicsService{
	
	private InterfaceAssitant ia = new InterfaceAssitant();
	
	public List<SimpleInterfaceInfo> getInterfaceInfoList(){
		List<InterfaceInfo> iil = new ArrayList<InterfaceInfo>();
		iil = ia.getInterfaceInfoList();
		
		List<SimpleInterfaceInfo> siil = new ArrayList<SimpleInterfaceInfo>();
		
		Iterator<InterfaceInfo> i = iil.iterator();
		while(i.hasNext()){
			InterfaceInfo ii = i.next();
			
			String addrs = ii.getIpList();
			String[] addrArray = addrs.split(";");
			/* if (addrArray[0] != "") {
				String[] imArray = addrArray[0].split("/");
				ip = imArray[0];
				mask = imArray[1];
			} */
						
			SimpleInterfaceInfo sii = new SimpleInterfaceInfo();
			sii.setName(ii.getName());
			sii.setDescription(ii.getDescription());
			sii.setMode(ii.getInterfaceMode());
			//sii.setAddr(ip + "/" + mask);
			if (addrArray.length > 1) {
				sii.setAddr(addrArray[0] + " ...");
			} else {
				sii.setAddr(addrArray[0]);
			}
			sii.setMtu(ii.getMtu());
			sii.setState(ii.getState());
			sii.setConnect(ii.getConnect());
			sii.setNegotiation(ii.getDoubleMode());
			sii.setSpeed(ii.getSpeed());
			
			siil.add(sii);
		}
		return siil;
	}
}
