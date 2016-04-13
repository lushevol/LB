obj/Debug/klbmanager/rpc/XmlRpcServer.o obj/Debug/klbmanager/rpc/XmlRpcServer.d: src/rpc/XmlRpcServer.cpp src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcDispatch.h src/rpc/XmlRpcSource.h \
 src/rpc/XmlRpcServerConnection.h src/rpc/XmlRpcValue.h \
 src/rpc/xml/tinyxml.h src/rpc/XmlRpcServerMethod.h \
 src/rpc/XmlRpcSocket.h src/rpc/XmlRpcUtil.h src/rpc/XmlRpcException.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcServer.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcServer.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcServer.o src/rpc/XmlRpcServer.cpp
%.h: fail
	
.PHONY:fail
fail:
	
