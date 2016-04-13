obj/Release/klbmanager/server/ethernet.o obj/Release/klbmanager/server/ethernet.d: src/server/ethernet.cpp src/server/share/utility.h \
 src/server/rpc.h src/server/rpc/XmlRpc.h src/server/rpc/XmlRpcClient.h \
 src/server/rpc/XmlRpcDispatch.h src/server/rpc/XmlRpcSource.h \
 src/server/rpc/XmlRpcException.h src/server/rpc/XmlRpcServer.h \
 src/server/rpc/XmlRpcServerMethod.h src/server/rpc/XmlRpcValue.h \
 src/server/rpc/xml/tinyxml.h src/server/rpc/XmlRpcUtil.h \
 src/server/model/model.h src/server/model/share/include.h \
 src/server/model/rpc/XmlRpc.h src/server/model/exception.h \
 src/server/serialize.h src/server/base.h src/server/logger.h \
 src/server/model/types.h src/server/model/model.h \
 src/server/model/system.h src/server/model/types.h src/server/ethernet.h \
 src/server/interface.h src/server/model/interface.h
	rm -f obj/Release/klbmanager/server/ethernet.d
	make -f make.do obj/Release/klbmanager/server/ethernet.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/server/ethernet.o src/server/ethernet.cpp
%.h: fail
	
.PHONY:fail
fail:
	
