obj/Debug/klbmanager/server/network.o obj/Debug/klbmanager/server/network.d: src/server/network.cpp src/server/share/include.h \
 src/server/share/debug.h src/server/share/utility.h \
 src/server/share/include.h src/server/model/network.h \
 src/server/model/model.h src/server/model/share/include.h \
 src/server/model/rpc/XmlRpc.h src/server/model/rpc/XmlRpcClient.h \
 src/server/model/rpc/XmlRpcDispatch.h \
 src/server/model/rpc/XmlRpcSource.h \
 src/server/model/rpc/XmlRpcException.h \
 src/server/model/rpc/XmlRpcServer.h \
 src/server/model/rpc/XmlRpcServerMethod.h \
 src/server/model/rpc/XmlRpcValue.h src/server/model/rpc/xml/tinyxml.h \
 src/server/model/rpc/XmlRpcUtil.h src/server/model/exception.h \
 src/server/rpc.h src/server/rpc/XmlRpc.h src/server/model/model.h \
 src/server/serialize.h src/server/base.h src/server/logger.h \
 src/server/model/types.h src/server/model/system.h \
 src/server/model/types.h src/server/network.h src/server/ethernet.h \
 src/server/interface.h src/server/model/interface.h src/server/bonding.h \
 src/server/adsl.h src/server/model/adsl.h src/server/model/interface.h \
 src/server/ha.h src/server/model/ipvs.h
	rm -f obj/Debug/klbmanager/server/network.d
	make -f make.do obj/Debug/klbmanager/server/network.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/server/network.o src/server/network.cpp
%.h: fail
	
.PHONY:fail
fail:
	
