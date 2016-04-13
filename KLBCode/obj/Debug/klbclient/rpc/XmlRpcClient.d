obj/Debug/klbclient/rpc/XmlRpcClient.o obj/Debug/klbclient/rpc/XmlRpcClient.d: src/rpc/XmlRpcClient.cpp src/rpc/XmlRpcClient.h \
 src/rpc/XmlRpcDispatch.h src/rpc/XmlRpcSource.h src/rpc/XmlRpcSocket.h \
 src/rpc/XmlRpc.h src/rpc/XmlRpcException.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcValue.h src/rpc/xml/tinyxml.h \
 src/rpc/XmlRpcUtil.h
	rm -f obj/Debug/klbclient/rpc/XmlRpcClient.d
	make -f make.do obj/Debug/klbclient/rpc/XmlRpcClient.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__ -c -o obj/Debug/klbclient/rpc/XmlRpcClient.o src/rpc/XmlRpcClient.cpp
%.h: fail
	
.PHONY:fail
fail:
	
