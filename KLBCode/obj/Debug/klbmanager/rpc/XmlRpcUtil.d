obj/Debug/klbmanager/rpc/XmlRpcUtil.o obj/Debug/klbmanager/rpc/XmlRpcUtil.d: src/rpc/XmlRpcUtil.cpp src/rpc/XmlRpcUtil.h \
 src/rpc/XmlRpc.h src/rpc/XmlRpcClient.h src/rpc/XmlRpcDispatch.h \
 src/rpc/XmlRpcSource.h src/rpc/XmlRpcException.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcValue.h src/rpc/xml/tinyxml.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcUtil.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcUtil.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcUtil.o src/rpc/XmlRpcUtil.cpp
%.h: fail
	
.PHONY:fail
fail:
	
