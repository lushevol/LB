obj/Release/klbmanager/xml/tinyxmlparser.o obj/Release/klbmanager/xml/tinyxmlparser.d: src/xml/tinyxmlparser.cpp src/xml/tinyxml.h
	rm -f obj/Release/klbmanager/xml/tinyxmlparser.d
	make -f make.do obj/Release/klbmanager/xml/tinyxmlparser.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/xml/tinyxmlparser.o src/xml/tinyxmlparser.cpp
%.h: fail
	
.PHONY:fail
fail:
	
