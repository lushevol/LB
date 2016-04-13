obj/Release/klbmanager/rpc/XmlRpcClient.o obj/Release/klbmanager/rpc/XmlRpcClient.d: src/rpc/XmlRpcClient.cpp src/rpc/XmlRpcClient.h \
 src/rpc/XmlRpcDispatch.h src/rpc/XmlRpcSource.h src/rpc/XmlRpcSocket.h \
 src/rpc/XmlRpc.h src/rpc/XmlRpcException.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcValue.h src/rpc/xml/tinyxml.h \
 src/rpc/XmlRpcUtil.h
	rm -f obj/Release/klbmanager/rpc/XmlRpcClient.d
	make -f make.do obj/Release/klbmanager/rpc/XmlRpcClient.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/rpc/XmlRpcClient.o src/rpc/XmlRpcClient.cpp
%.h: fail
	
.PHONY:fail
fail:
	
