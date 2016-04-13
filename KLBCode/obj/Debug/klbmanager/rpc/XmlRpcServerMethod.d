obj/Debug/klbmanager/rpc/XmlRpcServerMethod.o obj/Debug/klbmanager/rpc/XmlRpcServerMethod.d: src/rpc/XmlRpcServerMethod.cpp \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcDispatch.h src/rpc/XmlRpcSource.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcServerMethod.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcServerMethod.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcServerMethod.o src/rpc/XmlRpcServerMethod.cpp
%.h: fail
	
.PHONY:fail
fail:
	
