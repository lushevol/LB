obj/Release/klbclient/client/main.o obj/Release/klbclient/client/main.d: src/client/main.cpp src/client/model/model.h \
 src/client/model/share/include.h src/client/model/rpc/XmlRpc.h \
 src/client/model/rpc/XmlRpcClient.h \
 src/client/model/rpc/XmlRpcDispatch.h \
 src/client/model/rpc/XmlRpcSource.h \
 src/client/model/rpc/XmlRpcException.h \
 src/client/model/rpc/XmlRpcServer.h \
 src/client/model/rpc/XmlRpcServerMethod.h \
 src/client/model/rpc/XmlRpcValue.h src/client/model/rpc/xml/tinyxml.h \
 src/client/model/rpc/XmlRpcUtil.h src/client/model/exception.h \
 src/client/share/version.h src/client/control.h src/client/parser.h \
 src/client/exception.h src/client/reader.h
	rm -f obj/Release/klbclient/client/main.d
	make -f make.do obj/Release/klbclient/client/main.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/client/main.o src/client/main.cpp
%.h: fail
	
.PHONY:fail
fail:
	
