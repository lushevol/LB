obj/Debug/klbmanager/server/ha.o obj/Debug/klbmanager/server/ha.d: src/server/ha.cpp src/server/share/utility.h \
 src/server/share/include.h src/server/share/debug.h src/server/rpc.h \
 src/server/rpc/XmlRpc.h src/server/rpc/XmlRpcClient.h \
 src/server/rpc/XmlRpcDispatch.h src/server/rpc/XmlRpcSource.h \
 src/server/rpc/XmlRpcException.h src/server/rpc/XmlRpcServer.h \
 src/server/rpc/XmlRpcServerMethod.h src/server/rpc/XmlRpcValue.h \
 src/server/rpc/xml/tinyxml.h src/server/rpc/XmlRpcUtil.h \
 src/server/model/model.h src/server/model/share/include.h \
 src/server/model/rpc/XmlRpc.h src/server/model/exception.h \
 src/server/serialize.h src/server/base.h src/server/logger.h \
 src/server/model/types.h src/server/model/model.h \
 src/server/model/system.h src/server/model/types.h src/server/ethernet.h \
 src/server/interface.h src/server/model/interface.h src/server/bonding.h \
 src/server/ha.h src/server/model/ipvs.h src/server/model/interface.h \
 src/server/network.h src/server/services.h src/server/hvs.h \
 src/server/model/hvs.h src/server/model/ipvs.h
	rm -f obj/Debug/klbmanager/server/ha.d
	make -f make.do obj/Debug/klbmanager/server/ha.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/server/ha.o src/server/ha.cpp
%.h: fail
	
.PHONY:fail
fail:
	
