obj/Debug/klbmanager/rpc/XmlRpcSource.o obj/Debug/klbmanager/rpc/XmlRpcSource.d: src/rpc/XmlRpcSource.cpp src/rpc/XmlRpcSource.h \
 src/rpc/XmlRpcSocket.h src/rpc/XmlRpcUtil.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcSource.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcSource.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcSource.o src/rpc/XmlRpcSource.cpp
%.h: fail
	
.PHONY:fail
fail:
	
