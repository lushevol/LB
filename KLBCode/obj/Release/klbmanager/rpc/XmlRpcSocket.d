obj/Release/klbmanager/rpc/XmlRpcSocket.o obj/Release/klbmanager/rpc/XmlRpcSocket.d: src/rpc/XmlRpcSocket.cpp src/rpc/XmlRpcSocket.h \
 src/rpc/XmlRpcUtil.h
	rm -f obj/Release/klbmanager/rpc/XmlRpcSocket.d
	make -f make.do obj/Release/klbmanager/rpc/XmlRpcSocket.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/rpc/XmlRpcSocket.o src/rpc/XmlRpcSocket.cpp
%.h: fail
	
.PHONY:fail
fail:
	
