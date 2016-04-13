obj/Release/klbmanager/server/route.o obj/Release/klbmanager/server/route.d: src/server/route.cpp src/server/share/include.h \
 src/server/share/utility.h src/server/base.h src/server/model/model.h \
 src/server/model/share/include.h src/server/model/rpc/XmlRpc.h \
 src/server/model/rpc/XmlRpcClient.h \
 src/server/model/rpc/XmlRpcDispatch.h \
 src/server/model/rpc/XmlRpcSource.h \
 src/server/model/rpc/XmlRpcException.h \
 src/server/model/rpc/XmlRpcServer.h \
 src/server/model/rpc/XmlRpcServerMethod.h \
 src/server/model/rpc/XmlRpcValue.h src/server/model/rpc/xml/tinyxml.h \
 src/server/model/rpc/XmlRpcUtil.h src/server/model/exception.h \
 src/server/network.h src/server/model/types.h src/server/model/model.h \
 src/server/interface.h src/server/model/interface.h \
 src/server/model/types.h src/server/ethernet.h src/server/bonding.h \
 src/server/route.h src/server/model/route.h src/server/model/isp.h \
 src/server/staticroute.h src/server/policyroute.h \
 src/server/smartroute.h src/server/adsl.h src/server/model/adsl.h \
 src/server/model/interface.h
	rm -f obj/Release/klbmanager/server/route.d
	make -f make.do obj/Release/klbmanager/server/route.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/server/route.o src/server/route.cpp
%.h: fail
	
.PHONY:fail
fail:
	
