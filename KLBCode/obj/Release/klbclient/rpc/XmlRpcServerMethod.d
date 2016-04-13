obj/Release/klbclient/rpc/XmlRpcServerMethod.o obj/Release/klbclient/rpc/XmlRpcServerMethod.d: src/rpc/XmlRpcServerMethod.cpp \
 src/rpc/XmlRpcServerMethod.h src/rpc/XmlRpcServer.h \
 src/rpc/XmlRpcDispatch.h src/rpc/XmlRpcSource.h
	rm -f obj/Release/klbclient/rpc/XmlRpcServerMethod.d
	make -f make.do obj/Release/klbclient/rpc/XmlRpcServerMethod.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/rpc/XmlRpcServerMethod.o src/rpc/XmlRpcServerMethod.cpp
%.h: fail
	
.PHONY:fail
fail:
	
