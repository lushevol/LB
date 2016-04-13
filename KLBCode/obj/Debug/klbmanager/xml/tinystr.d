obj/Debug/klbmanager/xml/tinystr.o obj/Debug/klbmanager/xml/tinystr.d: src/xml/tinystr.cpp
	rm -f obj/Debug/klbmanager/xml/tinystr.d
	make -f make.do obj/Debug/klbmanager/xml/tinystr.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/xml/tinystr.o src/xml/tinystr.cpp
%.h: fail
	
.PHONY:fail
fail:
	
