obj/Release/klbclient/client/staticroute.o obj/Release/klbclient/client/staticroute.d: src/client/staticroute.cpp src/client/model/route.h \
 src/client/model/share/include.h src/client/model/model.h \
 src/client/model/rpc/XmlRpc.h src/client/model/rpc/XmlRpcClient.h \
 src/client/model/rpc/XmlRpcDispatch.h \
 src/client/model/rpc/XmlRpcSource.h \
 src/client/model/rpc/XmlRpcException.h \
 src/client/model/rpc/XmlRpcServer.h \
 src/client/model/rpc/XmlRpcServerMethod.h \
 src/client/model/rpc/XmlRpcValue.h src/client/model/rpc/xml/tinyxml.h \
 src/client/model/rpc/XmlRpcUtil.h src/client/model/exception.h \
 src/client/model/types.h src/client/model/isp.h src/client/control.h \
 src/client/model/model.h src/client/parser.h src/client/route.h \
 src/client/tools.h
	rm -f obj/Release/klbclient/client/staticroute.d
	make -f make.do obj/Release/klbclient/client/staticroute.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/client/staticroute.o src/client/staticroute.cpp
%.h: fail
	
.PHONY:fail
fail:
	
