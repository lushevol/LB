obj/Debug/klbmanager/server/rpc.o obj/Debug/klbmanager/server/rpc.d: src/server/rpc.cpp src/server/share/utility.h \
 src/server/share/include.h src/server/share/debug.h \
 src/server/share/include.h src/server/model/model.h \
 src/server/model/share/include.h src/server/model/rpc/XmlRpc.h \
 src/server/model/rpc/XmlRpcClient.h \
 src/server/model/rpc/XmlRpcDispatch.h \
 src/server/model/rpc/XmlRpcSource.h \
 src/server/model/rpc/XmlRpcException.h \
 src/server/model/rpc/XmlRpcServer.h \
 src/server/model/rpc/XmlRpcServerMethod.h \
 src/server/model/rpc/XmlRpcValue.h src/server/model/rpc/xml/tinyxml.h \
 src/server/model/rpc/XmlRpcUtil.h src/server/model/exception.h \
 src/server/licence/licence.h src/server/licence/share/include.h \
 src/server/rpc.h src/server/rpc/XmlRpc.h src/server/base.h \
 src/server/logger.h src/server/model/types.h src/server/model/model.h \
 src/server/model/system.h src/server/model/types.h src/server/wdt.h
	rm -f obj/Debug/klbmanager/server/rpc.d
	make -f make.do obj/Debug/klbmanager/server/rpc.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/server/rpc.o src/server/rpc.cpp
%.h: fail
	
.PHONY:fail
fail:
	
