obj/Debug/klbclient/client/control.o obj/Debug/klbclient/client/control.d: src/client/control.cpp src/client/rpc/XmlRpc.h \
 src/client/rpc/XmlRpcClient.h src/client/rpc/XmlRpcDispatch.h \
 src/client/rpc/XmlRpcSource.h src/client/rpc/XmlRpcException.h \
 src/client/rpc/XmlRpcServer.h src/client/rpc/XmlRpcServerMethod.h \
 src/client/rpc/XmlRpcValue.h src/client/rpc/xml/tinyxml.h \
 src/client/rpc/XmlRpcUtil.h src/client/model/system.h \
 src/client/model/model.h src/client/model/share/include.h \
 src/client/model/share/debug.h src/client/model/rpc/XmlRpc.h \
 src/client/model/exception.h src/client/model/types.h \
 src/client/control.h src/client/model/model.h src/client/parser.h \
 src/client/exception.h
	rm -f obj/Debug/klbclient/client/control.d
	make -f make.do obj/Debug/klbclient/client/control.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__ -c -o obj/Debug/klbclient/client/control.o src/client/control.cpp
%.h: fail
	
.PHONY:fail
fail:
	
