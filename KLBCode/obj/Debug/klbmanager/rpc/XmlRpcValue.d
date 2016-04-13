obj/Debug/klbmanager/rpc/XmlRpcValue.o obj/Debug/klbmanager/rpc/XmlRpcValue.d: src/rpc/XmlRpcValue.cpp src/rpc/XmlRpcValue.h \
 src/rpc/xml/tinyxml.h src/rpc/XmlRpcException.h src/rpc/XmlRpcUtil.h \
 src/rpc/base64.h
	rm -f obj/Debug/klbmanager/rpc/XmlRpcValue.d
	make -f make.do obj/Debug/klbmanager/rpc/XmlRpcValue.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/rpc/XmlRpcValue.o src/rpc/XmlRpcValue.cpp
%.h: fail
	
.PHONY:fail
fail:
	
