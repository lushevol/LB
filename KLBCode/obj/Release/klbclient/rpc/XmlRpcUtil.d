obj/Release/klbclient/rpc/XmlRpcUtil.o obj/Release/klbclient/rpc/XmlRpcUtil.d: src/rpc/XmlRpcUtil.cpp src/rpc/XmlRpcUtil.h \
 src/rpc/XmlRpc.h src/rpc/XmlRpcClient.h src/rpc/XmlRpcDispatch.h \
 src/rpc/XmlRpcSource.h src/rpc/XmlRpcException.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcValue.h src/rpc/xml/tinyxml.h
	rm -f obj/Release/klbclient/rpc/XmlRpcUtil.d
	make -f make.do obj/Release/klbclient/rpc/XmlRpcUtil.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/rpc/XmlRpcUtil.o src/rpc/XmlRpcUtil.cpp
%.h: fail
	
.PHONY:fail
fail:
	
