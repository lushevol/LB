obj/Debug/klbmanager/rpc/XmlRpcServerConnection.o obj/Debug/klbmanager/rpc/XmlRpcServerConnection.d: src/rpc/XmlRpcServerConnection.cpp \
 src/rpc/XmlRpcServerConnection.h src/rpc/XmlRpcValue.h \
 src/rpc/xml/tinyxml.h src/rpc/XmlRpcSource.h src/rpc/XmlRpcSocket.h \
 src/rpc/XmlRpc.h src/rpc/XmlRpcClient.h src/rpc/XmlRpcDispatch.h \
 src/rpc/XmlRpcException.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcUtil.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcServerConnection.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcServerConnection.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcServerConnection.o src/rpc/XmlRpcServerConnection.cpp
%.h: fail
	
.PHONY:fail
fail:
	
