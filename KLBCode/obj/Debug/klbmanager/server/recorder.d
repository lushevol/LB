obj/Debug/klbmanager/server/recorder.o obj/Debug/klbmanager/server/recorder.d: src/server/recorder.cpp src/server/share/utility.h \
 src/server/share/include.h src/server/share/debug.h src/server/base.h \
 src/server/model/model.h src/server/model/share/include.h \
 src/server/model/rpc/XmlRpc.h src/server/model/rpc/XmlRpcClient.h \
 src/server/model/rpc/XmlRpcDispatch.h \
 src/server/model/rpc/XmlRpcSource.h \
 src/server/model/rpc/XmlRpcException.h \
 src/server/model/rpc/XmlRpcServer.h \
 src/server/model/rpc/XmlRpcServerMethod.h \
 src/server/model/rpc/XmlRpcValue.h src/server/model/rpc/xml/tinyxml.h \
 src/server/model/rpc/XmlRpcUtil.h src/server/model/exception.h \
 src/server/rpc.h src/server/rpc/XmlRpc.h src/server/logger.h \
 src/server/model/types.h src/server/model/model.h \
 src/server/model/system.h src/server/model/types.h \
 src/server/serialize.h src/server/services.h src/server/model/ipvs.h \
 src/server/model/interface.h src/server/recorder.h \
 src/server/share/include.h
	rm -f obj/Debug/klbmanager/server/recorder.d
	make -f make.do obj/Debug/klbmanager/server/recorder.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/server/recorder.o src/server/recorder.cpp
%.h: fail
	
.PHONY:fail
fail:
	
