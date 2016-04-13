obj/Debug/klbclient/xml/tinyxmlparser.o obj/Debug/klbclient/xml/tinyxmlparser.d: src/xml/tinyxmlparser.cpp src/xml/tinyxml.h
	rm -f obj/Debug/klbclient/xml/tinyxmlparser.d
	make -f make.do obj/Debug/klbclient/xml/tinyxmlparser.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__DEBUG__ -c -o obj/Debug/klbclient/xml/tinyxmlparser.o src/xml/tinyxmlparser.cpp
%.h: fail
	
.PHONY:fail
fail:
	
