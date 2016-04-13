obj/Release/klbmanager/xml/tinystr.o obj/Release/klbmanager/xml/tinystr.d: src/xml/tinystr.cpp
	rm -f obj/Release/klbmanager/xml/tinystr.d
	make -f make.do obj/Release/klbmanager/xml/tinystr.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/xml/tinystr.o src/xml/tinystr.cpp
%.h: fail
	
.PHONY:fail
fail:
	
