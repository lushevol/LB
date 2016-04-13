obj/Debug/klbclient/xml/tinystr.o obj/Debug/klbclient/xml/tinystr.d: src/xml/tinystr.cpp
	rm -f obj/Debug/klbclient/xml/tinystr.d
	make -f make.do obj/Debug/klbclient/xml/tinystr.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__ -c -o obj/Debug/klbclient/xml/tinystr.o src/xml/tinystr.cpp
%.h: fail
	
.PHONY:fail
fail:
	
