obj/Debug/klbmanager/rpc/XmlRpcDispatch.o obj/Debug/klbmanager/rpc/XmlRpcDispatch.d: src/rpc/XmlRpcDispatch.cpp src/rpc/XmlRpcDispatch.h \
 src/rpc/XmlRpcSource.h src/rpc/XmlRpcUtil.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcDispatch.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcDispatch.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcDispatch.o src/rpc/XmlRpcDispatch.cpp
%.h: fail
	
.PHONY:fail
fail:
	
