obj/Release/klbclient/rpc/XmlRpcDispatch.o obj/Release/klbclient/rpc/XmlRpcDispatch.d: src/rpc/XmlRpcDispatch.cpp src/rpc/XmlRpcDispatch.h \
 src/rpc/XmlRpcSource.h src/rpc/XmlRpcUtil.h
	rm -f obj/Release/klbclient/rpc/XmlRpcDispatch.d
	make -f make.do obj/Release/klbclient/rpc/XmlRpcDispatch.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/rpc/XmlRpcDispatch.o src/rpc/XmlRpcDispatch.cpp
%.h: fail
	
.PHONY:fail
fail:
	
