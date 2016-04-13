obj/Release/klbclient/rpc/XmlRpcValue.o obj/Release/klbclient/rpc/XmlRpcValue.d: src/rpc/XmlRpcValue.cpp src/rpc/XmlRpcValue.h \
 src/rpc/xml/tinyxml.h src/rpc/XmlRpcException.h src/rpc/XmlRpcUtil.h \
 src/rpc/base64.h
	rm -f obj/Release/klbclient/rpc/XmlRpcValue.d
	make -f make.do obj/Release/klbclient/rpc/XmlRpcValue.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/rpc/XmlRpcValue.o src/rpc/XmlRpcValue.cpp
%.h: fail
	
.PHONY:fail
fail:
	
